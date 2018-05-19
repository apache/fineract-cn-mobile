package org.apache.fineract.data.models.deposit

import com.google.gson.annotations.SerializedName

data class Charge(
    @SerializedName("actionIdentifier") var actionIdentifier: String? = null,
    @SerializedName("incomeAccountIdentifier") var incomeAccountIdentifier: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("proportional") var proportional: Boolean? = null,
    @SerializedName("amount") var amount: Double? = null
)