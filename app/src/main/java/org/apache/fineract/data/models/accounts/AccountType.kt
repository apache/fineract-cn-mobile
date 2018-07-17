package org.apache.fineract.data.models.accounts

import com.google.gson.annotations.SerializedName

enum class AccountType {

    @SerializedName("ASSET")
    ASSET,

    @SerializedName("LIABILITY")
    LIABILITY,

    @SerializedName("EQUITY")
    EQUITY,

    @SerializedName("REVENUE")
    REVENUE,

    @SerializedName("EXPENSE")
    EXPENSE
}