package org.apache.fineract.data.models.customer

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.ConflictAction
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import kotlinx.android.parcel.Parcelize
import org.apache.fineract.data.local.database.AppDatabase

@Parcelize
@Table(name = "ContactDetail", database = AppDatabase::class, allFields = true, insertConflict =
ConflictAction.REPLACE, useBooleanGetterSetters = false)
data class ContactDetail(
    @SerializedName("type") var type: Type? = null,
    @SerializedName("value") var value: String? = null,
    @SerializedName("preferenceLevel") var preferenceLevel: Int? = null,
    @SerializedName("validated") var validated: Boolean? = null,
    @SerializedName("group") var group: Group? = null
) : BaseModel(), Parcelable {

    @PrimaryKey
    @ForeignKey(stubbedRelationship = true) var customer: Customer? = null

    enum class Type {
        @SerializedName("EMAIL")
        EMAIL,

        @SerializedName("PHONE")
        PHONE,

        @SerializedName("MOBILE")
        MOBILE
    }

    enum class Group {

        @SerializedName("BUSINESS")
        BUSINESS,

        @SerializedName("PRIVATE")
        PRIVATE
    }

}