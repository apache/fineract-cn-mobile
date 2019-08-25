package org.apache.fineract.data.models.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.apache.fineract.data.models.loan.AccountAssignment
import org.apache.fineract.data.models.loan.TermRange
import java.util.ArrayList

/**
 * @author Rajan Maurya
 * On 20/07/17.
 */
@Parcelize
data class Product(
        @SerializedName("identifier") val identifier: String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("termRange") val termRange: TermRange? = null,
        @SerializedName("balanceRange") val balanceRange: BalanceRange? = null,
        @SerializedName("interestRange") val interestRange: InterestRange? = null,
        @SerializedName("interestBasis") val interestBasis: InterestBasis? = null,
        @SerializedName("patternPackage") val patternPackage: String? = null,
        @SerializedName("description") val description: String? = null,
        @SerializedName("currencyCode") val currencyCode: String? = null,
        @SerializedName("minorCurrencyUnitDigits") val minorCurrencyUnitDigits: Int = 0,
        @SerializedName("accountAssignments") val accountAssignments: List<AccountAssignment> =
                ArrayList(),
        @SerializedName("parameters") val parameters: String? = null,
        @SerializedName("createdOn") val createdOn: String? = null,
        @SerializedName("createdBy") val createdBy: String? = null,
        @SerializedName("lastModifiedOn") val lastModifiedOn: String? = null,
        @SerializedName("lastModifiedBy") val lastModifiedBy: String? = null
) : Parcelable
