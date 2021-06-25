package org.apache.fineract.data.models.teller

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class Teller(

        @SerializedName("code") var code: String? = null,
        @SerializedName("password") var password: String? = null,
        @SerializedName("cashdrawLimit") var cashdrawLimit: BigDecimal? = null,
        @SerializedName("tellerAccountIdentifier") var tellerAccountIdentifier: String? = null,
        @SerializedName("vaultAccountIdentifier") var vaultAccountIdentifier: String? = null,
        @SerializedName("chequesReceivableAccount") var chequesReceivableAccount: String? = null,
        @SerializedName("cashOverShortAccount") var cashOverShortAccount: String? = null,
        @SerializedName("denominationRequired") var denominationRequired: Boolean = false,
        @SerializedName("assignedEmployee") var assignedEmployee: String? = null,
        @SerializedName("state") var state: State? = null,
        @SerializedName("createdBy") var createdBy: String? = null,
        @SerializedName("createdOn") var createdOn: String? = null,
        @SerializedName("lastModifiedBy") var lastModifiedBy: String? = null,
        @SerializedName("lastModifiedOn") var lastModifiedOn: String? = null,
        @SerializedName("lastOpenedBy") var lastOpenedBy: String? = null,
        @SerializedName("lastOpenedOn") var lastOpenedOn: String? = null

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