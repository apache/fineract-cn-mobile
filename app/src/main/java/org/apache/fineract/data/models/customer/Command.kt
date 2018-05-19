package org.apache.fineract.data.models.customer

import com.google.gson.annotations.SerializedName

data class Command(
    @SerializedName("action") var action: Action? = null,
    @SerializedName("comment") var comment: String? = null,
    @SerializedName("createdOn") var createdOn: String? = null,
    @SerializedName("createdBy") var createdBy: String? = null
) {
    enum class Action {

        @SerializedName("ACTIVATE")
        ACTIVATE,

        @SerializedName("LOCK")
        LOCK,

        @SerializedName("UNLOCK")
        UNLOCK,

        @SerializedName("CLOSE")
        CLOSE,

        @SerializedName("REOPEN")
        REOPEN
    }
}