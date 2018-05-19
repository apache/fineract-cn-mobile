package org.apache.fineract.data.models.customer.identification

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author Rajan Maurya
 * On 31/07/17.
 */
@Parcelize
data class ExpirationDate(
    @SerializedName("year") var year: Int? = null,
    @SerializedName("month") var month: Int? = null,
    @SerializedName("day") var day: Int? = null
) : Parcelable
