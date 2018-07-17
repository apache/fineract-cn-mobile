package org.apache.fineract.data.models.accounts

import com.google.gson.annotations.SerializedName

data class LedgerPage(
        @SerializedName("ledgers") val ledgers: List<Ledger>? = null,
        @SerializedName("totalPages") val totalPages: Int? = null,
        @SerializedName("totalElements") val totalElements: Long? = null
)