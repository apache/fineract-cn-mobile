package org.apache.fineract.data.models.teller

import com.google.gson.annotations.SerializedName

data class TellerCommand (
    @SerializedName("action") var action: String? = null,
    @SerializedName("adjustment") var adjustment: String? = "NONE",
    @SerializedName("assignedEmployeeIdentifier") var assignedEmployeeIdentifier: String? = null
) {
    enum class TellerAction {

        @SerializedName("CLOSE")
        CLOSE,

        @SerializedName("ACTIVATE")
        ACTIVATE,

        @SerializedName("REOPEN")
        REOPEN,

    }
}