package org.apache.fineract.data.remote;

import android.content.Context;
import androidx.annotation.NonNull;

import org.apache.fineract.exceptions.NoConnectivityException;
import org.apache.fineract.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Rajan Maurya
 *         On 23/09/17.
 */
public class ConnectivityInterceptor implements Interceptor {

    private Context context;

    public ConnectivityInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        if (!NetworkUtil.isConnected(context)) {
            throw new NoConnectivityException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }
}
