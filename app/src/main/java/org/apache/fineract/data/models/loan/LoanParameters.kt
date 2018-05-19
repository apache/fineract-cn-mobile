package org.apache.fineract.data.models.loan

import com.google.gson.annotations.SerializedName

/**
 * @author Rajan Maurya
 * On 12/07/17.
 */

data class LoanParameters(
    @SerializedName("customerIdentifier") var customerIdentifier: String? = null,
    @SerializedName("creditWorthinessSnapshots") var creditWorthinessSnapshots:
    List<CreditWorthinessSnapshot>? = null,
    @SerializedName("maximumBalance") var maximumBalance: Double? = null,
    @SerializedName("termRange") var termRange: TermRange? = null,
    @SerializedName("paymentCycle") var paymentCycle: PaymentCycle? = null
)