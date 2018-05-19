package org.apache.fineract.data.models.product

import com.google.gson.annotations.SerializedName

/**
 * @author Rajan Maurya
 * On 20/07/17.
 */

data class ProductPage (
        @SerializedName("elements") var elements: List<Product>? = null,
        @SerializedName("totalPages") var totalPages: Int? = null,
        @SerializedName("totalElements") var totalElements: Long? = null
)
