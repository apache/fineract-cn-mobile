package org.apache.fineract.data.models.payment

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

/**
 * @author Rajan Maurya
 * On 13/07/17.
 */
data class PlannedPayment (
    @SerializedName("interestRate") var interestRate: Double? = null,
    @SerializedName("costComponents") var costComponents: List<CostComponent> = ArrayList(),
    @SerializedName("remainingPrincipal") var remainingPrincipal: Double? = null,
    @SerializedName("date") var date: String? = null
)
