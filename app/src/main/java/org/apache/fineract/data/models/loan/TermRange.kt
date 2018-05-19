package org.apache.fineract.data.models.loan

import com.google.gson.annotations.SerializedName

/**
 * @author Rajan Maurya
 * On 12/07/17.
 */
data class TermRange(
    @SerializedName("temporalUnit") var temporalUnit: String?,
    @SerializedName("maximum") var maximum: Double?
)
