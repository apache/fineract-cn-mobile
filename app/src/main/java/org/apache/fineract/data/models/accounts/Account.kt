package org.apache.fineract.data.models.accounts

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.apache.fineract.couchbase.DocumentType

@Parcelize
data class Account(

        var type: AccountType? = null,
        var identifier: String? = null,
        var name: String? = null,
        var holders: List<String>? = null,
        var signatureAuthorities: List<String>? = null,
        var balance: Double? = null,
        var referenceAccount: String? = null,
        var ledger: String? = null,
        var state: State? = null,
        var alternativeAccountNumber: String? = null,
        var createdOn: String? = null,
        var createdBy: String? = null,
        var lastModifiedOn: String? = null,
        var lastModifiedBy: String? = null,
        var documentType: String = DocumentType.ACCOUNT.value
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

