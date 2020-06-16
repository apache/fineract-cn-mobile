package org.apache.fineract.data.remote

import android.content.Context
import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response
import org.apache.fineract.FineractApplication
import org.apache.fineract.data.local.PreferenceKey
import org.apache.fineract.data.local.PreferencesHelper
import java.io.IOException
import java.util.*
import javax.inject.Inject

/**
 * Created by Ahmad Jawid Muhammadi on 1/6/20
 */

class FineractInterceptor(context: Context?) : Interceptor {

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val chainRequest = chain.request()
        val builder = chainRequest.newBuilder()

        //TODO fix call single time instead of calling every request
        val authToken = preferencesHelper!!.accessToken
        val tenantIdentifier = preferencesHelper!!.tenantIdentifier
        val user = preferencesHelper!!.userName
        val refreshTokenStatus = preferencesHelper!!.getBoolean(
                PreferenceKey.PREF_KEY_REFRESH_ACCESS_TOKEN, false)
        builder.header(HEADER_ACCEPT_JSON, "application/json")
        builder.header(HEADER_CONTENT_TYPE, "application/json")
        if (refreshTokenStatus) {
            //Add Cookies
            val cookies = preferencesHelper!!.getStringSet(
                    PreferenceKey.PREF_KEY_COOKIES) as HashSet<String>
            if (cookies != null) {
                for (cookie in cookies) {
                    builder.addHeader("Cookie", cookie)
                }
            }
        } else {
            if (!TextUtils.isEmpty(authToken)) {
                builder.header(HEADER_AUTH, authToken)
            }
            if (!TextUtils.isEmpty(user)) {
                builder.header(HEADER_USER, user)
            }
        }
        if (!TextUtils.isEmpty(tenantIdentifier)) {
            builder.header(HEADER_TENANT, tenantIdentifier)
        }
        val request = builder.build()
        return chain.proceed(request)
    }

    companion object {
        const val HEADER_TENANT = "X-Tenant-Identifier"
        const val HEADER_AUTH = "Authorization"
        private const val HEADER_ACCEPT_JSON = "Accept"
        private const val HEADER_CONTENT_TYPE = "Content-type"
        const val HEADER_USER = "User"
    }

    init {
        FineractApplication.get(context).component.inject(this)
    }
}