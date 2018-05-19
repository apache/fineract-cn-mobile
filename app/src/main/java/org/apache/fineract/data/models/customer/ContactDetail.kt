package org.apache.fineract.data.models.customer

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactDetail(
    @SerializedName("type") var type: Type? = null,
    @SerializedName("value") var value: String? = null,
    @SerializedName("preferenceLevel") var preferenceLevel: Int? = null,
    @SerializedName("validated") var validated: Boolean? = null,
    @SerializedName("group") var group: Group? = null
) : Parcelable {

    enum class Type {
        @SerializedName("EMAIL")
        EMAIL,

        @SerializedName("PHONE")
        PHONE,

        @SerializedName("MOBILE")
        MOBILE
    }

    enum class Group {

        @SerializedName("BUSINESS")
        BUSINESS,

        @SerializedName("PRIVATE")
        PRIVATE
    }

}