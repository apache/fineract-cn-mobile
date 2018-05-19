package org.apache.fineract.data.models.deposit

import com.google.gson.annotations.SerializedName

data class Currency(
    @SerializedName("code") var code: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("sign") var sign: String? = null,
    @SerializedName("scale") var scale: Int? = null
)