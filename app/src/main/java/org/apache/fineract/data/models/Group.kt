package org.apache.fineract.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.apache.fineract.data.models.customer.Address

/*
 * Created by saksham on 16/June/2019
*/

@Parcelize
data class Group(
        val identifier: String,
        val groupDefinitionIdentifier: String,
        val name: String,
        val leaders: List<String>,
        val members: List<String>,
        val office: String,
        val assignedEmployee: String,
        val weekday: Int,
        val status: Status,
        val address: Address,
        val createdOn: String,
        val createdBy: String,
        val lastModifiedBy: String,
        val lastModifiedOn: String) : Parcelable {

    enum class Status {
        PENDING,
        ACTIVE,
        CLOSED
    }
}