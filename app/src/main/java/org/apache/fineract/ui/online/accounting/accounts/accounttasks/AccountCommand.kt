package org.apache.fineract.ui.online.accounting.accounts.accounttasks

import com.google.gson.annotations.SerializedName


/**
 * Created by Varun Jain on 23/July/2021
 */

data class AccountCommand(
    @SerializedName("action") var action: AccountTaskAction? = null,
    @SerializedName("comment") var comment: String? = null
) {
    enum class AccountTaskAction {
        @SerializedName("LOCK")
        LOCK,

        @SerializedName("UNLOCK")
        UNLOCK,

        @SerializedName("REOPEN")
        REOPEN,

        @SerializedName("CLOSE")
        CLOSE,
    }
}