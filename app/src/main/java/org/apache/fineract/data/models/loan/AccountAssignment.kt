package org.apache.fineract.data.models.loan

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author Rajan Maurya
 * On 09/07/17.
 */
@Parcelize
data class AccountAssignment(
        @SerializedName("designator") var designator: String? = null,
        @SerializedName("accountIdentifier") var accountIdentifier: String? = null,
        @SerializedName("ledgerIdentifier") var ledgerIdentifier: String? = null
) : Parcelable