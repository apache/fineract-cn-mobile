package org.apache.fineract.data.models.customer

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DateOfBirth(
    @SerializedName("year") var year: Int? = null,
    @SerializedName("month")  var month: Int? = null,
    @SerializedName("day") var day: Int? = null
) : Parcelable