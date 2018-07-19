package org.apache.fineract.data.models.accounts

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account(

        @SerializedName("type") val type: AccountType? = null,
        @SerializedName("identifier") val identifier: String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("holders") val holders: Set<String>? = null,
        @SerializedName("signatureAuthorities") val signatureAuthorities: Set<String>? = null,
        @SerializedName("balance") val balance: Double? = null,
        @SerializedName("referenceAccount") val referenceAccount: String? = null,
        @SerializedName("ledger") val ledger: String? = null,
        @SerializedName("state") val state: State? = null,
        @SerializedName("alternativeAccountNumber") val alternativeAccountNumber: String? = null,
        @SerializedName("createdOn") val createdOn: String? = null,
        @SerializedName("createdBy") val createdBy: String? = null,
        @SerializedName("lastModifiedOn") val lastModifiedOn: String? = null,
        @SerializedName("lastModifiedBy") val lastModifiedBy: String? = null

) : Parcelable {

    enum class State {

        @SerializedName("OPEN")
        OPEN,

        @SerializedName("LOCKED")
        LOCKED,

        @SerializedName("CLOSED")
        CLOSED
    }
}

