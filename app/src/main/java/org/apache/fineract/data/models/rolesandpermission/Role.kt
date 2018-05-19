package org.apache.fineract.data.models.rolesandpermission

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author Rajan Maurya
 * On 24/08/17.
 */
@Parcelize
data class Role(
    @SerializedName("identifier") var identifier: String? = null,
    @SerializedName("permissions") var permissions: List<Permission>? = null
) : Parcelable