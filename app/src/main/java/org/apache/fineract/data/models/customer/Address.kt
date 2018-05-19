package org.apache.fineract.data.models.customer

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    @SerializedName("street") var street: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("region") var region: String? = null,
    @SerializedName("postalCode") var postalCode: String? = null,
    @SerializedName("countryCode") var countryCode: String? = null,
    @SerializedName("country") var country: String? = null
) : Parcelable