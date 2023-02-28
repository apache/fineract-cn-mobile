package org.apache.fineract.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.fineract.R
import org.apache.fineract.data.models.customer.Customer
import org.apache.fineract.data.models.geolocation.GeoPoint

/**
 * Created by Ahmad Jawid Muhammadi on 22/7/20.
 */

fun buildAlertMessageNoGps(activity: Activity) {
    MaterialDialog.Builder()
            .init(activity)
            .setCancelable(true)
            .setMessage(R.string.application_require_gps_msg)
            .setPositiveButton(R.string.yes) { di: DialogInterface, _ ->
                di.dismiss()
                val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                activity.startActivityForResult(enableGpsIntent, Constants.PERMISSIONS_REQUEST_ENABLE_GPS)
            }.createMaterialDialog()
            .show()
}

fun addMarkerOnMap(map: GoogleMap?, latLng: LatLng, title: String?) {
    map?.addMarker(MarkerOptions().position(latLng).title(title))
    val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(15f)
            .bearing(90f)
            .tilt(30f)
            .build()
    map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
}

fun addMarkerOnNearByCustomers(map: GoogleMap?,
                               lastKnownLocation: Location?,
                               customers: List<Customer>,
                               onInfoWindowClickListener: GoogleMap.OnInfoWindowClickListener) {

    var index = 0
    for (customer in customers) {
        val address = customer.address

        val geoPoint = address?.geoPoint?.let {
            getGeoPoint(it)
        } ?: getMockLocationsLatLng()[index++]

        val location = LatLng(geoPoint.lat!!, geoPoint.lng!!)
        map?.addMarker(
                MarkerOptions()
                        .position(location)
                        .title(customer.givenName)
                        .snippet(address?.city)
        )

        if (index == 2) index = 0
    }
    map?.setOnInfoWindowClickListener(onInfoWindowClickListener)
    val cameraPosition = CameraPosition.Builder()
            .target(LatLng(lastKnownLocation?.latitude!!, lastKnownLocation.longitude))
            .zoom(15f) // Sets the zoom
            .bearing(90f) // Sets the orientation of the camera to east
            .tilt(30f) // Sets the tilt of the camera to 30 degrees
            .build()
    map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

}


fun isMapsEnabled(activity: Activity): Boolean {
    val manager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        buildAlertMessageNoGps(activity)
        return false
    }
    return true
}

fun getGeoPoint(geoPoint: String?): GeoPoint? {
    return Gson().fromJson<GeoPoint>(geoPoint,
            object : TypeToken<GeoPoint?>() {}.type)
}

fun getMockLocationsLatLng(): List<GeoPoint> {
    return arrayListOf(
            GeoPoint(37.423997, -122.096476),
            GeoPoint(37.417044, -122.079481),
            GeoPoint(37.430097, -122.073774)
    )
}