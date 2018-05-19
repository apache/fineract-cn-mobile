package org.apache.fineract.data.models.loan

import com.google.gson.annotations.SerializedName

/**
 * @author Rajan Maurya
 * On 12/07/17.
 */

data class PaymentCycle (
    @SerializedName("temporalUnit") var temporalUnit: String? = null,
    @SerializedName("period") var period: Int? = null,
    @SerializedName("alignmentDay") var alignmentDay: Int? = null,
    @SerializedName("alignmentWeek") var alignmentWeek: Int? = null,
    @SerializedName("alignmentMonth") var alignmentMonth: Int? = null
)
