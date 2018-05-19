package org.apache.fineract.data.models.rolesandpermission

import com.google.gson.annotations.SerializedName

/**
 * @author Rajan Maurya
 * On 24/08/17.
 */
enum class AllowedOperation {

    @SerializedName("READ") READ, //GET, HEAD

    @SerializedName("CHANGE") CHANGE, //POST, PUT

    @SerializedName("DELETE") DELETE //DELETE
}
