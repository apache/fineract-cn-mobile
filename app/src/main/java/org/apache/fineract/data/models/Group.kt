package org.apache.fineract.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.apache.fineract.data.models.customer.Address

/*
 * Created by saksham on 16/June/2019
*/

@Parcelize
data class Group(
        var identifier: String? = null,
        var groupDefinitionIdentifier: String? = null,
        var name: String? = null,
        var leaders: List<String>? = null,
        var members: List<String>? = null,
        var office: String? = null,
        var assignedEmployee: String? = null,
        var weekday: Int? = null,
        var status: Status? = null,
        var address: Address? = null,
        var createdOn: String? = null,
        var createdBy: String? = null,
        var lastModifiedBy: String? = null,
        var lastModifiedOn: String? = null) : Parcelable {

    enum class Status {
        PENDING,
        ACTIVE,
        CLOSED
    }
}