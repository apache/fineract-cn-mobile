package org.apache.fineract.data.models.accounts

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class Ledger(

        @SerializedName("type") val type: AccountType,
        @SerializedName("identifier") val identifier: String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("description") val description: String? = null,
        @SerializedName("parentLedgerIdentifier") val parentLedgerIdentifier: String? = null,
        @SerializedName("subLedgers") val subLedgers: List<Ledger>? = null,
        @SerializedName("totalValue") val totalValue: BigDecimal? = null,
        @SerializedName("createdOn") val createdOn: String? = null,
        @SerializedName("createdBy") val createdBy: String? = null,
        @SerializedName("lastModifiedOn") val lastModifiedOn: String? = null,
        @SerializedName("lastModifiedBy") val lastModifiedBy: String? = null,
        @SerializedName("showAccountsInChart") val showAccountsInChart: Boolean

) : Parcelable

