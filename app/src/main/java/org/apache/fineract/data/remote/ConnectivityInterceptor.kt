package org.apache.fineract.data.remote

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import org.apache.fineract.exceptions.NoConnectivityException
import org.apache.fineract.utils.NetworkUtil
import java.io.IOException

/**
 * Created by Ahmad Jawid Muhammadi on 1/6/20
 */

class ConnectivityInterceptor(private var context: Context?) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        if (!NetworkUtil.isConnected(context)) {
            throw NoConnectivityException()
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}