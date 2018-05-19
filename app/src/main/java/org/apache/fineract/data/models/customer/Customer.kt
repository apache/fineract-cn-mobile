package org.apache.fineract.data.models.customer

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Customer(
    @SerializedName("identifier") var identifier: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("givenName") var givenName: String? = null,
    @SerializedName("middleName") var middleName: String? = null,
    @SerializedName("surname") var surname: String? = null,
    @SerializedName("dateOfBirth") var dateOfBirth: DateOfBirth? = null,
    @SerializedName("member") var member: Boolean? = null,
    @SerializedName("accountBeneficiary") var accountBeneficiary: String? = null,
    @SerializedName("referenceCustomer") var referenceCustomer: String? = null,
    @SerializedName("assignedOffice") var assignedOffice: String? = null,
    @SerializedName("assignedEmployee") var assignedEmployee: String? = null,
    @SerializedName("address") var address: Address? = null,
    @SerializedName("contactDetails") var contactDetails: List<ContactDetail>? = null,
    @SerializedName("currentState") var currentState: State? = null,
    @SerializedName("createdBy") var createdBy: String? = null,
    @SerializedName("createdOn") var createdOn: String? = null,
    @SerializedName("lastModifiedBy") var lastModifiedBy: String? = null,
    @SerializedName("lastModifiedOn") var lastModifiedOn: String? = null
) : Parcelable {

    enum class Type {

        @SerializedName("PERSON")
        PERSON,

        @SerializedName("BUSINESS")
        BUSINESS
    }

    enum class State {

        @SerializedName("PENDING")
        PENDING,

        @SerializedName("ACTIVE")
        ACTIVE,

        @SerializedName("LOCKED")
        LOCKED,

        @SerializedName("CLOSED")
        CLOSED
    }
}