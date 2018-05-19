package org.apache.fineract.data.models.payment

import com.google.gson.annotations.SerializedName

/**
 * @author Rajan Maurya
 * On 13/07/17.
 */

data class ChargeName (
    @SerializedName("identifier") var identifier: String? = null,
    @SerializedName("name") var name: String? = null
)
