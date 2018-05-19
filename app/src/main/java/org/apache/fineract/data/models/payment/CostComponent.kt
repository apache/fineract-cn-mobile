package org.apache.fineract.data.models.payment

import com.google.gson.annotations.SerializedName

/**
 * @author Rajan Maurya
 * On 13/07/17.
 */

data class CostComponent (
    @SerializedName("chargeIdentifier") var chargeIdentifier: String? = null,
    @SerializedName("amount") var amount: Double? = null
)
