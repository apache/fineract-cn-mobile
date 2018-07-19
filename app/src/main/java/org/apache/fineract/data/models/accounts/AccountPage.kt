package org.apache.fineract.data.models.accounts

import com.google.gson.annotations.SerializedName

data class AccountPage(
        @SerializedName("accounts") val accounts: List<Account>? = null,
        @SerializedName("totalPages") val totalPages: Int? = null,
        @SerializedName("totalElements") val totalElements: Long? = null
)