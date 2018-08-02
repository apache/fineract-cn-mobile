package org.apache.fineract.data.models.payroll

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PayrollConfiguration(
        @SerializedName("mainAccountNumber") val mainAccountNumber: String? = null,
        @SerializedName("payrollAllocations") val payrollAllocations:
        List<PayrollAllocation> = ArrayList(),
        @SerializedName("createdOn") val createdOn: String? = null,
        @SerializedName("createdBy") val createdBy: String? = null,
        @SerializedName("lastModifiedOn") val lastModifiedOn: String? = null,
        @SerializedName("lastModifiedBy") val lastModifiedBy: String? = null
) : Parcelable