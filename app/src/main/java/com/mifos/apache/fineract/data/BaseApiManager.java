package com.mifos.apache.fineract.data;

import com.mifos.apache.fineract.data.services.AuthService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author Rajan Maurya
 * @since 16/3/2017
 */
public class BaseApiManager {

    private static Retrofit retrofit;
    private static AuthService authApi;

    public BaseApiManager() {
        createService();
    }

    private static void init() {
        authApi = createApi(AuthService.class);
    }

    private static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    public static void createService() {

        retrofit = new Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        init();
    }

    public AuthService getAuthApi() {
        return authApi;
    }
}
