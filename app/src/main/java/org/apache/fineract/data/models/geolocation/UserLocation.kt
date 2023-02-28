package org.apache.fineract.data.models.geolocation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by Ahmad Jawid Muhammadi on 15/7/20.
 */

@Parcelize
data class UserLocation(
        @SerializedName("user_id")
        var userName: String? = null,

        @SerializedName("client_name")
        var clientName: String? = null,

        @SerializedName("address")
        var address: String? = null,

        @SerializedName("geo_point")
        var geoPoint: List<GeoPoint>? = null,

        @SerializedName("start_time")
        var startTime: String? = null,

        @SerializedName("stop_time")
        var stopTime: String? = null,

        @SerializedName("date")
        var date: String = Date().time.toString()

) : Parcelable {
    var dateFormat = "dd MMMM yyyy HH:mm"
    var locale = "en"
}

