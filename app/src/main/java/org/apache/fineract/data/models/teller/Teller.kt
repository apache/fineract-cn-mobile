package org.apache.fineract.data.models.teller

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.apache.fineract.couchbase.DocumentType
import java.math.BigDecimal

@Parcelize
data class Teller(

    var code: String? = null,
    var password: String? = null,
    var cashdrawLimit: BigDecimal? = null,
    var tellerAccountIdentifier: String? = null,
    var vaultAccountIdentifier: String? = null,
    var chequesReceivableAccount: String? = null,
    var cashOverShortAccount: String? = null,
    var denominationRequired: Boolean = false,
    var assignedEmployee: String? = null,
    var state: State? = State.OPEN,
    var createdBy: String? = null,
    var createdOn: String? = null,
    var lastModifiedBy: String? = null,
    var lastModifiedOn: String? = null,
    var lastOpenedBy: String? = null,
    var lastOpenedOn: String? = null,
    var documentType: String = DocumentType.TELLER.value
) : Parcelable {

    enum class State {

        @SerializedName("ACTIVE")
        ACTIVE,

        @SerializedName("CLOSED")
        CLOSED,

        @SerializedName("OPEN")
        OPEN,

        @SerializedName("PAUSED")
        PAUSED
    }
}