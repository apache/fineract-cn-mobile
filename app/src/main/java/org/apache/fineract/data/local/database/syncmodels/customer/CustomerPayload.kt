package org.apache.fineract.data.local.database.syncmodels.customer

import android.os.Parcelable
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
import kotlinx.android.parcel.Parcelize
import org.apache.fineract.data.local.database.AppDatabase

@Parcelize
@Table(database = AppDatabase::class, useBooleanGetterSetters = false)
data class CustomerPayload(
    @PrimaryKey
    @Column var customerPayload: String? = null
) : BaseModel(), Parcelable
