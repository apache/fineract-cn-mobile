package org.apache.fineract.data.models.accounts

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class Ledger(

        @SerializedName("type") var type: AccountType,
        @SerializedName("identifier") var identifier: String? = null,
        @SerializedName("name") var name: String? = null,
        @SerializedName("description") var description: String? = null,
        @SerializedName("parentLedgerIdentifier") val parentLedgerIdentifier: String? = null,
        @SerializedName("subLedgers") var subLedgers: List<Ledger>? = null,
        @SerializedName("totalValue") var totalValue: BigDecimal? = null,
        @SerializedName("createdOn") var createdOn: String? = null,
        @SerializedName("createdBy") var createdBy: String? = null,
        @SerializedName("lastModifiedOn") var lastModifiedOn: String? = null,
        @SerializedName("lastModifiedBy") var lastModifiedBy: String? = null,
        @SerializedName("showAccountsInChart") var showAccountsInChart: Boolean

) : Parcelable

