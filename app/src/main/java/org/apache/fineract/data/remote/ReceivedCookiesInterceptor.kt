package org.apache.fineract.data.remote

import android.content.Context
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

class ReceivedCookiesInterceptor(context: Context?) : Interceptor {

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies = HashSet<String>()
            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }
            preferencesHelper!!.putStringSet(PreferenceKey.PREF_KEY_COOKIES, cookies)
        }
        return originalResponse
    }

    init {
        FineractApplication.get(context).component.inject(this)
    }
}