package org.apache.fineract.data.models.geolocation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ahmad Jawid Muhammadi on 15/7/20.
 */

@Parcelize
data class GeoPoint(
        @SerializedName("lat")
        var lat: Double? = null,

        @SerializedName("lng")
        var lng: Double? = null
) : Parcelable