package org.apache.fineract.data.models.customer

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.ConflictAction
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import kotlinx.android.parcel.Parcelize
import org.apache.fineract.data.local.database.AppDatabase

@Parcelize
@Table(database = AppDatabase::class, allFields = true)
data class DateOfBirth(
    @SerializedName("year") var year: Int? = null,
    @SerializedName("month")  var month: Int? = null,
    @SerializedName("day") var day: Int? = null
) : BaseModel(), Parcelable {
    @PrimaryKey(autoincrement = true)
    var id: Long? = null
}