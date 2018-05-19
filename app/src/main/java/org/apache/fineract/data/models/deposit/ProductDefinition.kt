package org.apache.fineract.data.models.deposit

import com.google.gson.annotations.SerializedName

data class ProductDefinition(
    @SerializedName("identifier") var identifier: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("currency") var currency: Currency? = null,
    @SerializedName("minimumBalance") var minimumBalance: Double? = null,
    @SerializedName("equityLedgerIdentifier") var equityLedgerIdentifier: String? = null,
    @SerializedName("cashAccountIdentifier") var cashAccountIdentifier: String? = null,
    @SerializedName("expenseAccountIdentifier") var expenseAccountIdentifier: String? = null,
    @SerializedName("accrueAccountIdentifier") var accrueAccountIdentifier: String? = null,
    @SerializedName("interest") var interest: Double? = null,
    @SerializedName("term") var term: Term? = null,
    @SerializedName("charges") var charges: List<Charge>? = null,
    @SerializedName("flexible") var flexible: Boolean? = null,
    @SerializedName("active") var active: Boolean? = null
) {
    private var type: Type? = null

    fun getType(): String {
        return this.type!!.name
    }

    fun setType(type: String) {
        this.type = Type.valueOf(type)
    }
}