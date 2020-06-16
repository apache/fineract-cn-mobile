package org.apache.fineract.data.remote

/**
 * Created by Ahmad Jawid Muhammadi on 1/6/20
 */

object BaseUrl {
    private const val PROTOCOL_HTTPS = "https://"
    private const val API_ENDPOINT = "pilot.kuelap.io"
    val PORT = "80"
    // "/" in the last of the base url always

    // "/" in the last of the base url always
    fun getName(): String? {
        return "fineract"
    }

    @JvmStatic
    fun getDefaultBaseUrl(): String? {
        return PROTOCOL_HTTPS + API_ENDPOINT
    }
}