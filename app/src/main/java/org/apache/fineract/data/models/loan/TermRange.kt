package org.apache.fineract.data.models.loan

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author Rajan Maurya
 * On 12/07/17.
 */
@Parcelize
data class TermRange(
    @SerializedName("temporalUnit") var temporalUnit: String?,
    @SerializedName("maximum") var maximum: Double?
) : Parcelable
