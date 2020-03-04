package org.apache.fineract.data.models.loan

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

/**
 * @author Rajan Maurya
 * On 12/07/17.
 */

@Parcelize
data class LoanParameters(
        @SerializedName("customerIdentifier") var customerIdentifier: String? = null,
        @SerializedName("creditWorthinessSnapshots") var creditWorthinessSnapshots:
        List<CreditWorthinessSnapshot>? = null,
        @SerializedName("maximumBalance") var maximumBalance: Double? = null,
        @SerializedName("termRange") var termRange: @RawValue TermRange? = null,
        @SerializedName("paymentCycle") var paymentCycle: @RawValue PaymentCycle? = null
) : Parcelable