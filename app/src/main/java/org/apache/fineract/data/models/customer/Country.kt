package org.apache.fineract.data.models.customer

import com.google.gson.annotations.SerializedName

/**
 * @author Rajan Maurya
 * On 26/07/17.
 */

data class Country(
    @SerializedName("translations") var translations: Translations,
    @SerializedName("name") var name: String,
    @SerializedName("alpha2Code") var alphaCode: String
)