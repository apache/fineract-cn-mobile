package com.mifos.apache.fineract.data;

import android.content.Context;

import com.mifos.apache.fineract.data.services.AuthService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author Rajan Maurya
 * @since 16/3/2017
 */
public class BaseApiManager {

    private static Retrofit retrofit;
    private static AuthService authApi;

    public BaseApiManager(Context context) {
        createService(context);
    }

    private static void init() {
        authApi = createApi(AuthService.class);
    }

    private static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    private static void createService(Context context) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new MifosInterceptor(context))
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.getDefaultBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        init();
    }

    public AuthService getAuthApi() {
        return authApi;
    }
}
