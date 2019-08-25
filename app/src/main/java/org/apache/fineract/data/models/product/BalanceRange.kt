package org.apache.fineract.data.models.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author Rajan Maurya
 * On 20/07/17.
 */

@Parcelize
data class BalanceRange(
        @SerializedName("minimum") var minimum: Double? = null,
        @SerializedName("maximum") var maximum: Double? = null
) : Parcelable
