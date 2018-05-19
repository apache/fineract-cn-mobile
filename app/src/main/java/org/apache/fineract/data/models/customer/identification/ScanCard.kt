package org.apache.fineract.data.models.customer.identification

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author Rajan Maurya
 * On 01/08/17.
 */
@Parcelize
data class ScanCard(
    @SerializedName("description") var description: String,
    @SerializedName("identifier") var identifier: String
) : Parcelable