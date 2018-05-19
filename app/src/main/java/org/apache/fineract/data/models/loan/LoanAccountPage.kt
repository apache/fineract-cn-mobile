package org.apache.fineract.data.models.loan

import com.google.gson.annotations.SerializedName

/**
 * @author Rajan Maurya
 * On 09/07/17.
 */

data class LoanAccountPage(
    @SerializedName("elements") var loanAccounts: List<LoanAccount>? = null,
    @SerializedName("totalPages") var totalPages: Int? = null,
    @SerializedName("totalElements") var totalElements: Long? = null
)