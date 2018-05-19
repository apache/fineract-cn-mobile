package org.apache.fineract.data.models.error

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author Rajan Maurya
 * On 18/06/17.
 */

@Parcelize
data class MifosError(
    @SerializedName("timestamp") var timestamp: String = "",
    @SerializedName("status") var status: Int? = null,
    @SerializedName("error") var error: String = "",
    @SerializedName("message") var message: String = "",
    @SerializedName("path") var path: String = ""
) : Parcelable