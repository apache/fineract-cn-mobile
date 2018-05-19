package org.apache.fineract.data.models.payment

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

/**
 * @author Rajan Maurya
 * On 13/07/17.
 */
data class PlannedPaymentPage (
    @SerializedName("chargeNames") var chargeNames: List<ChargeName> = ArrayList(),
    @SerializedName("elements") var elements: List<PlannedPayment> = ArrayList(),
    @SerializedName("totalPages") var totalPages: Int? = null,
    @SerializedName("totalElements") var totalElements: Long? = null
)
