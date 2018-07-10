package org.apache.fineract.data.models.teller

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class Teller(

        @SerializedName("code") val code: String? = null,
        @SerializedName("password") val password: String? = null,
        @SerializedName("cashdrawLimit") val cashdrawLimit: BigDecimal? = null,
        @SerializedName("tellerAccountIdentifier") val tellerAccountIdentifier: String? = null,
        @SerializedName("vaultAccountIdentifier") val vaultAccountIdentifier: String? = null,
        @SerializedName("chequesReceivableAccount") val chequesReceivableAccount: String? = null,
        @SerializedName("cashOverShortAccount") val cashOverShortAccount: String? = null,
        @SerializedName("denominationRequired") val denominationRequired: Boolean = false,
        @SerializedName("assignedEmployee") val assignedEmployee: String? = null,
        @SerializedName("state") val state: State? = null,
        @SerializedName("createdBy") val createdBy: String? = null,
        @SerializedName("createdOn") val createdOn: String? = null,
        @SerializedName("lastModifiedBy") val lastModifiedBy: String? = null,
        @SerializedName("lastModifiedOn") val lastModifiedOn: String? = null,
        @SerializedName("lastOpenedBy") val lastOpenedBy: String? = null,
        @SerializedName("lastOpenedOn") val lastOpenedOn: String? = null

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