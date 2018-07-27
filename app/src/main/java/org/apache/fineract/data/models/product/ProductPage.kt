package org.apache.fineract.data.models.product

import com.google.gson.annotations.SerializedName

/**
 * @author Rajan Maurya
 * On 20/07/17.
 */

data class ProductPage (
        @SerializedName("elements") val elements: List<Product>? = null,
        @SerializedName("totalPages") val totalPages: Int? = null,
        @SerializedName("totalElements") val totalElements: Long? = null
)
