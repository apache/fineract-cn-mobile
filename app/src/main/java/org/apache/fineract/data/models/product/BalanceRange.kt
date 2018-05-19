package org.apache.fineract.data.models.product

import com.google.gson.annotations.SerializedName

/**
 * @author Rajan Maurya
 * On 20/07/17.
 */

data class BalanceRange (
    @SerializedName("minimum") var minimum: Double? = null,
    @SerializedName("maximum") var maximum: Double? = null
)
