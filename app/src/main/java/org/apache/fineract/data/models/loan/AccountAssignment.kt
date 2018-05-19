package org.apache.fineract.data.models.loan

import com.google.gson.annotations.SerializedName

/**
 * @author Rajan Maurya
 * On 09/07/17.
 */
data class AccountAssignment (
    @SerializedName("designator") var designator: String? = null,
    @SerializedName("accountIdentifier") var accountIdentifier: String? = null,
    @SerializedName("ledgerIdentifier") var ledgerIdentifier: String? = null
)