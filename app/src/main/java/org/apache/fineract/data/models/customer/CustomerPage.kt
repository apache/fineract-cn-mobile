package org.apache.fineract.data.models.customer

import com.google.gson.annotations.SerializedName

data class CustomerPage(
    @SerializedName("customers") var customers: List<Customer>? = null,
    @SerializedName("totalPages") var totalPages: Int? = null,
    @SerializedName("totalElements") var totalElements: Long? = null
)