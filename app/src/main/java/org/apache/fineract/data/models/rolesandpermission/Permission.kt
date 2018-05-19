package org.apache.fineract.data.models.rolesandpermission

import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

/**
 * @author Rajan Maurya
 * On 24/08/17.
 */
@Parcelize
data class Permission(
    @SerializedName("permittableEndpointGroupIdentifier") var
    permittableEndpointGroupIdentifier: String? = null,
    @SerializedName("allowedOperations") var allowedOperations: List<AllowedOperation>? = null
) : Parcelable