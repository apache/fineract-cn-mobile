package org.apache.fineract.data.models.customer.identification

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author Rajan Maurya
 * On 31/07/17.
 */
@Parcelize
data class Identification(
    @SerializedName("type") var type: String? = "",
    @SerializedName("number") var number: String? = "",
    @SerializedName("expirationDate") var expirationDate: ExpirationDate? = null,
    @SerializedName("issuer") var issuer: String? = "",
    @SerializedName("createdBy") var createdBy: String? = "",
    @SerializedName("createdOn") var createdOn: String? = "",
    @SerializedName("lastModifiedBy") var lastModifiedBy: String? = "",
    @SerializedName("lastModifiedOn") var lastModifiedOn: String? = ""
) : Parcelable