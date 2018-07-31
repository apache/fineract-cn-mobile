package org.apache.fineract.data.models.payroll

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class PayrollAllocation(
        @SerializedName("accountNumber") val accountNumber: String? = null,
        @SerializedName("amount") val amount: BigDecimal,
        @SerializedName("proportional") val proportional: Boolean = false
)