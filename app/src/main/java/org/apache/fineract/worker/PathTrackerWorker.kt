package org.apache.fineract.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.fineract.R
import org.apache.fineract.data.datamanager.api.DataManagerGeolocation
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.data.models.geolocation.GeoPoint
import org.apache.fineract.data.models.geolocation.UserLocation
import org.apache.fineract.ui.online.geo_location.visit_customer.VisitCustomersActivity
import org.apache.fineract.utils.Constants
import org.apache.fineract.utils.DateUtils
import java.util.*
import javax.inject.Inject

/**
 * Created by Ahmad Jawid Muhammadi on 22/7/20.
 */

class PathTrackerWorker @Inject constructor(context: Context,
                                            parameters: WorkerParameters) :
        CoroutineWorker(context, parameters) {

    private var broadcastReceiver: BroadcastReceiver? = null
    private val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as
                    NotificationManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var geoPointList = arrayListOf<GeoPoint>()
    private var startedTime: String? = null
    private var showNotifications = true
    private lateinit var locationCallback: LocationCallback


    @Inject
    lateinit var dataManagerGeolocation: DataManagerGeolocation

    override suspend fun doWork(): Result {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        val clientName = inputData.getString(KEY_CLIENT_NAME)
                ?: return Result.failure()
        startedTime = DateUtils.getCurrentDate()

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                if (p1?.action == STOP_TRACKING) {
                    notificationManager.cancelAll()
                    saveUserLocation(createUserLocation())
                    showNotifications = false
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                    applicationContext.unregisterReceiver(broadcastReceiver)
                    Log.e(TAG, "onReceive has been called")
                }
            }

        }
        applicationContext.registerReceiver(broadcastReceiver, IntentFilter(STOP_TRACKING))


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                if (location != null) {
                    val geoPoint = GeoPoint(location.latitude, location.longitude)
                    geoPointList.add(geoPoint)
                    Log.e(TAG, "onLocationResult")
                }
            }
        }
        getLocation()

        while (showNotifications)
            setForeground(createForegroundInfo(clientName, applicationContext))

        return Result.success()
    }

    private fun createUserLocation(): UserLocation {
        return UserLocation().apply {
            userName = PreferencesHelper(applicationContext).userName
            startTime = startedTime
            stopTime = DateUtils.getCurrentDate()
            date = Calendar.getInstance().timeInMillis.toString()
            geoPoint = geoPointList
        }
    }

    // Creates an instance of ForegroundInfo which can be used to update the
    // ongoing notification.
    private fun createForegroundInfo(clientName: String, context: Context): ForegroundInfo {
        val id = applicationContext.getString(R.string.notification_channel_id)
        // This PendingIntent can be used to cancel the worker
        val intent = Intent().setAction(STOP_TRACKING)
        val intentBroadcast = PendingIntent.getBroadcast(
                applicationContext, 0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(context)
        }

        val contentIntent = PendingIntent.getActivity(
                applicationContext, 1,
                Intent(applicationContext, VisitCustomersActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(applicationContext, id)
                .setContentTitle(applicationContext.getString(R.string.notification_title))
                .setContentText(applicationContext.getString(R.string.navigating_to, clientName))
                .setSmallIcon(R.drawable.ic_baseline_location_on_24)
                .setOngoing(true)
                .setAutoCancel(false)
                .setContentIntent(contentIntent)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel,
                        applicationContext.getString(R.string.stop_tracking),
                        intentBroadcast)
                .build()

        return ForegroundInfo(ONGOING_NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(context: Context) {
        val channel = NotificationChannel(
                context.getString(R.string.notification_channel_id),
                context.getString(R.string.tracking_user_location),
                NotificationManager.IMPORTANCE_DEFAULT
        )
        with(notificationManager) {
            createNotificationChannel(channel)
        }
    }

    private fun getLocation() {
        // Create the location request to start receiving updates
        val mLocationRequestHighAccuracy = LocationRequest()
        mLocationRequestHighAccuracy.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequestHighAccuracy.interval = Constants.UPDATE_INTERVAL
        mLocationRequestHighAccuracy.fastestInterval = Constants.FASTEST_INTERVAL

        if (ActivityCompat.checkSelfPermission(applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
                mLocationRequestHighAccuracy,
                locationCallback,
                Looper.getMainLooper())
    }

    fun saveUserLocation(userLocation: UserLocation) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                dataManagerGeolocation.saveLocationPathAsync(userLocation).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val KEY_CLIENT_NAME = "KEY_CLIENT_NAME"
        const val ONGOING_NOTIFICATION_ID = 10
        const val STOP_TRACKING = "stop_tracking"
        val TAG = PathTrackerWorker::class.simpleName
    }
}