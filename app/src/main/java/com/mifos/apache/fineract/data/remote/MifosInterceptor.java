/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */

package com.mifos.apache.fineract.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.mifos.apache.fineract.MifosApplication;
import com.mifos.apache.fineract.data.local.PreferenceKey;
import com.mifos.apache.fineract.data.local.PreferencesHelper;

import java.io.IOException;
import java.util.HashSet;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

/**
 * @author Rajan Maurya
 * @since 17/03/2017
 */
public class MifosInterceptor implements Interceptor {

    public static final String HEADER_TENANT = "X-Tenant-Identifier";
    public static final String HEADER_AUTH = "Authorization";
    private static final String HEADER_ACCEPT_JSON = "Accept";
    private static final String HEADER_CONTENT_TYPE = "Content-type";
    public static final String HEADER_USER = "User";

    @Inject
    PreferencesHelper preferencesHelper;

    public MifosInterceptor(Context context) {
        MifosApplication.get(context).getComponent().inject(this);
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request chainRequest = chain.request();
        Builder builder = chainRequest.newBuilder();

        //TODO fix call single time instead of calling every request
        String authToken = preferencesHelper.getAccessToken();
        String tenantIdentifier = preferencesHelper.getTenantIdentifier();
        String user = preferencesHelper.getUserName();
        Boolean refreshTokenStatus = preferencesHelper.getBoolean(
                PreferenceKey.PREF_KEY_REFRESH_ACCESS_TOKEN, false);

        builder.header(HEADER_ACCEPT_JSON, "application/json");
        builder.header(HEADER_CONTENT_TYPE, "application/json");

        if (refreshTokenStatus) {
            //Add Cookies
            HashSet<String> cookies = (HashSet<String>) preferencesHelper.getStringSet(
                            PreferenceKey.PREF_KEY_COOKIES);
            if (cookies != null) {
                for (String cookie : cookies) {
                    builder.addHeader("Cookie", cookie);
                }
            }
        } else {
            if (!TextUtils.isEmpty(authToken)) {
                builder.header(HEADER_AUTH, authToken);
            }

            if (!TextUtils.isEmpty(user)) {
                builder.header(HEADER_USER, user);
            }
        }

        if (!TextUtils.isEmpty(tenantIdentifier)) {
            builder.header(HEADER_TENANT, tenantIdentifier);
        }

        Request request = builder.build();
        return chain.proceed(request);
    }
}
