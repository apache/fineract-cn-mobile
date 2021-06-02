package org.apache.fineract.data.models.customer

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.sql.language.SQLite.select
import com.raizlabs.android.dbflow.structure.BaseModel
import kotlinx.android.parcel.Parcelize
import org.apache.fineract.couchbase.DocumentType
import org.apache.fineract.data.local.database.AppDatabase

@Parcelize
@Table(database = AppDatabase::class, useBooleanGetterSetters = false)
data class Customer(
        @PrimaryKey
        @Column var identifier: String? = null,
        @Column var type: String? = null,
        @Column var givenName: String? = null,
        @Column var middleName: String? = null,
        @Column var surname: String? = null,
        @ForeignKey(saveForeignKeyModel = true)
        @Column var dateOfBirth: DateOfBirth? = null,
        @Column var member: Boolean? = null,
        @Column var accountBeneficiary: String? = null,
        @Column var referenceCustomer: String? = null,
        @Column var assignedOffice: String? = null,
        @Column var assignedEmployee: String? = null,
        @ForeignKey(saveForeignKeyModel = true)
        @Column var address: Address? = null,
        var contactDetails: List<ContactDetail>? = null,
        @Column var currentState: State? = State.PENDING,
        @Column var createdBy: String? = null,
        @Column var createdOn: String? = null,
        @Column var lastModifiedBy: String? = null,
        @Column var lastModifiedOn: String? = null,
        @Column var documentType: String = DocumentType.CUSTOMER.value
) : BaseModel(), Parcelable {

    var isUpdate: Boolean? = null

    @OneToMany(methods = arrayOf(OneToMany.Method.ALL), variableName = "contactDetails")
    fun getContactDetail(): List<ContactDetail>? {
        contactDetails = select()
                .from(ContactDetail::class.java)
                .where(ContactDetail_Table.customer_identifier.eq(identifier))
                .queryList()
        return contactDetails
    }

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

    override fun save(): Boolean {
        val res = super.save()
        if (contactDetails != null) {
            contactDetails!!.forEach {
                it.customer = this
                it.save()
            }
        }
        return res
    }
}