package org.apache.fineract.data.models.payroll

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class PayrollAllocation(
        @SerializedName("accountNumber") val accountNumber: String? = null,
        @SerializedName("amount") val amount: BigDecimal,
        @SerializedName("proportional") val proportional: Boolean = false
) : Parcelable