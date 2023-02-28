package org.apache.fineract.ui.online.geo_location.visit_customer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.button.MaterialButton
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.internal.PolylineEncoding
import com.google.maps.model.DirectionsResult
import kotlinx.android.synthetic.main.activity_visit_customers2.*
import org.apache.fineract.R
import org.apache.fineract.data.models.customer.Customer
import org.apache.fineract.data.models.geolocation.PolylineData
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.utils.Constants.AUTOCOMPLETE_REQUEST_CODE
import org.apache.fineract.utils.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
import org.apache.fineract.utils.Constants.PERMISSIONS_REQUEST_ENABLE_GPS
import org.apache.fineract.utils.Utils
import org.apache.fineract.utils.addMarkerOnMap
import org.apache.fineract.utils.addMarkerOnNearByCustomers
import org.apache.fineract.utils.isMapsEnabled
import org.apache.fineract.worker.PathTrackerWorker
import org.apache.fineract.worker.PathTrackerWorker.Companion.KEY_CLIENT_NAME
import java.util.*
import javax.inject.Inject


class VisitCustomersActivity : FineractBaseActivity(),
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener, GoogleMap.OnInfoWindowClickListener {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastKnowLocation: Location? = null
    private var geoApiContext: GeoApiContext? = null
    private var polyLinesData = ArrayList<PolylineData>()
    private var googleMap: GoogleMap? = null
    private lateinit var places: PlacesClient
    private var customers: List<Customer>? = null

    @Inject
    lateinit var factory: VisitCustomerViewModelFactory
    private var viewModel: VisitCustomerViewModel? = null


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_customers2)
        activityComponent.inject(this)
        showBackButton()
        setToolbarTitle(getString(R.string.visit_customer))
        setupMapView(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(VisitCustomerViewModel::class.java)
        changeBackgroundColor(fabEnterManually, false)
        subscribeUI()
        geoApiContext = GeoApiContext.Builder()
                .apiKey(getString(R.string.google_api_key))
                .build()
        places = Places.createClient(this)
        getLastKnownLocation()
        viewModel?.fetchCustomers(0, 10)

    }

    private fun subscribeUI() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fabEnterManually?.setOnClickListener {
            changeBackgroundColor(fabEnterManually, true)
            changeBackgroundColor(fabNearbyCustomers, false)
            googleMap?.clear()
            startSearchFragment()
        }
        fabNearbyCustomers?.setOnClickListener {
            changeBackgroundColor(fabEnterManually, false)
            changeBackgroundColor(fabNearbyCustomers, true)
            customers?.let { addMarkerOnNearByCustomers(googleMap, lastKnowLocation, it, this) }
        }
        fabNavigate?.setOnClickListener {
            if (fabNavigate.text == getString(R.string.start_tracking_path)) {
                fabNavigate.text = getString(R.string.stop_tracking)
                val data = workDataOf(KEY_CLIENT_NAME to "Ahmad Jawid")
                val trackerWorker = OneTimeWorkRequestBuilder<PathTrackerWorker>()
                        .setInputData(data)
                        .build()

                with(WorkManager.getInstance(this)) {
                    enqueue(trackerWorker)
                }

            } else {
                fabNavigate.text = getString(R.string.start_tracking_path)
                val intent = Intent().setAction(PathTrackerWorker.STOP_TRACKING)
                sendBroadcast(intent)
            }
        }

        viewModel?.customerList?.observe(this, androidx.lifecycle.Observer {
            it?.let {
                customers = it
                lastKnowLocation?.let {
                    addMarkerOnNearByCustomers(googleMap, it, customers!!, this)
                }
            }
        })
    }


    private fun changeBackgroundColor(button: MaterialButton, isToPrimary: Boolean) {
        if (isToPrimary) {
            button.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            button.setTextColor(resources.getColor(R.color.white))
            button.setIconTintResource(R.color.white)
        } else {
            button.setBackgroundColor(Color.WHITE)
            button.setTextColor(resources.getColor(R.color.black))
            button.setIconTintResource(R.color.black)
        }
    }

    private fun setupMapView(bundle: Bundle?) {
        mapView.onCreate(bundle)
        mapView?.getMapAsync(this)
    }


    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getLocationPermission()
            return
        }
        fusedLocationClient?.lastLocation?.addOnCompleteListener {
            lastKnowLocation = it.result
            customers?.let { it1 -> addMarkerOnNearByCustomers(googleMap, lastKnowLocation, it1, this) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)
        Utils.setToolbarIconColor(this, menu, R.color.white)
        return true
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        if (checkMapServices()) {
            getLocationPermission()
        }
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    private fun checkMapServices(): Boolean {
        if (isMapsEnabled(this)) {
            return true
        }
        return false
    }

    override fun onMapReady(map: GoogleMap?) {
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        googleMap = map
        map?.isMyLocationEnabled = true
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        onLowMemory()
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //show current location of the user on map
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //show current location
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PERMISSIONS_REQUEST_ENABLE_GPS -> {

            }
            AUTOCOMPLETE_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.let {
                            val place = Autocomplete.getPlaceFromIntent(data)
                            addMarkerOnMap(googleMap, place.latLng!!, place.address!!)
                        }
                    }
                    AutocompleteActivity.RESULT_ERROR -> {
                        data?.let {
                            val status = Autocomplete.getStatusFromIntent(data)
                            Log.e(TAG, status.statusMessage)
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                        // The user canceled the operation.
                    }
                }
            }
        }
        if (requestCode == PERMISSIONS_REQUEST_ENABLE_GPS) {
            getLocationPermission()
        }
    }

    override fun onPolylineClick(polyline: Polyline?) {
        var index = 0
        for (polylineData in polyLinesData) {
            index++
            if (polyline!!.id == polylineData.polyline.id) {
                polylineData.polyline.color = ContextCompat.getColor(this, R.color.blue)
                polylineData.polyline.zIndex = 1F
                val endLocation = LatLng(
                        polylineData.leg.endLocation.lat,
                        polylineData.leg.endLocation.lng
                )
                val marker: Marker = googleMap!!.addMarker(MarkerOptions()
                        .position(endLocation)
                        .title(getString(R.string.gmtrip_no, index))
                        .snippet(getString(R.string.duration, polylineData.leg.duration.humanReadable)
                        ))
                marker.showInfoWindow()
            } else {
                polylineData.polyline.color = ContextCompat.getColor(this, R.color.grey_500)
                polylineData.polyline.zIndex = 1F
            }
        }
    }

    private fun calculateDirections(marker: Marker, currentPosition: com.google.maps.model.LatLng) {
        val destination = com.google.maps.model.LatLng(
                marker.position.latitude,
                marker.position.longitude
        )
        val directions = DirectionsApiRequest(geoApiContext)
        directions.alternatives(true)
        directions.origin(currentPosition)
        directions.destination(destination).setCallback(object : PendingResult.Callback<DirectionsResult?> {
            override fun onResult(result: DirectionsResult?) {
                addPolyLinesToMap(result)
            }

            override fun onFailure(e: Throwable) {
                Log.e(TAG, e.printStackTrace().toString())
            }
        })
    }

    fun addPolyLinesToMap(result: DirectionsResult?) {
        Handler(Looper.getMainLooper()).post {
            if (polyLinesData.size > 0) {
                for (polylineData in polyLinesData) {
                    polylineData.polyline.remove()
                }
                polyLinesData.clear()
                polyLinesData = ArrayList()
            }
            var duration = 999999999.0
            for (route in result!!.routes) {
                val decodedPath = PolylineEncoding.decode(route.overviewPolyline.encodedPath)
                val newDecodedPath: MutableList<LatLng> = ArrayList()
                // This loops through all the LatLng coordinates of ONE polyline.
                for (latLng in decodedPath) {
                    newDecodedPath.add(LatLng(
                            latLng.lat,
                            latLng.lng
                    ))
                }
                val polyline: Polyline = googleMap!!.addPolyline(PolylineOptions().addAll(newDecodedPath))
                polyline.color = ContextCompat.getColor(this, R.color.grey_500)
                polyline.isClickable = true
                polyLinesData.add(PolylineData(polyline, route.legs[0]))

                // highlight the fastest route and adjust camera
                val tempDuration = route.legs[0].duration.inSeconds.toDouble()
                if (tempDuration < duration) {
                    duration = tempDuration
                    onPolylineClick(polyline)
                    zoomRoute(polyline.points)
                }
            }
        }
    }

    private fun startSearchFragment() {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    private fun zoomRoute(lstLatLngRoute: List<LatLng?>?) {
        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return
        val boundsBuilder = LatLngBounds.Builder()
        for (latLngPoint in lstLatLngRoute) boundsBuilder.include(latLngPoint)
        val routePadding = 50
        val latLngBounds = boundsBuilder.build()
        googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                600,
                null
        )
    }

    override fun onInfoWindowClick(marker: Marker?) {
        marker?.let {
            calculateDirections(
                    it,
                    com.google.maps.model.LatLng(
                            lastKnowLocation?.latitude!!,
                            lastKnowLocation?.longitude!!
                    )
            )
        }
    }

    companion object {
        val TAG = VisitCustomersActivity::class.simpleName
    }

}