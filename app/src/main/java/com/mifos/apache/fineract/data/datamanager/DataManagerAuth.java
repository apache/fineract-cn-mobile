package com.mifos.apache.fineract.data.datamanager;

import android.util.Base64;

import com.mifos.apache.fineract.data.local.PreferenceKey;
import com.mifos.apache.fineract.data.local.PreferencesHelper;
import com.mifos.apache.fineract.data.models.Authentication;
import com.mifos.apache.fineract.data.remote.BaseApiManager;
import com.mifos.apache.fineract.exceptions.ExceptionStatusCode;

import java.nio.charset.Charset;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author Rajan Maurya On 16/03/17.
 */
@Singleton
public class DataManagerAuth {

    private final BaseApiManager baseApiManager;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManagerAuth(BaseApiManager baseApiManager, PreferencesHelper preferencesHelper) {
        this.baseApiManager = baseApiManager;
        this.preferencesHelper = preferencesHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return preferencesHelper;
    }

    public Observable<Authentication> login(String username, String password) {
        return baseApiManager.getAuthApi().login(username,
                Base64.encodeToString(password.getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP));
    }

    public Observable<Authentication> refreshToken() {
        return baseApiManager.getAuthApi().refreshToken();
    }

    public <T> Function<Throwable, ? extends Observable<? extends T>> refreshTokenAndRetry(
            final Observable<T> toBeResumed) {
        return new Function<Throwable, Observable<? extends T>>() {
            @Override
            public Observable<? extends T> apply(Throwable throwable) throws Exception {
                // Here check if the error thrown really is a 401
                if (ExceptionStatusCode.isHttp401Error(throwable)) {
                    preferencesHelper.putBoolean(PreferenceKey.PREF_KEY_REFRESH_ACCESS_TOKEN, true);
                    return refreshToken().concatMap(new Function<Authentication,
                            ObservableSource<? extends T>>() {
                        @Override
                        public ObservableSource<? extends T> apply(
                                Authentication authentication) throws Exception {
                            preferencesHelper.putBoolean(
                                    PreferenceKey.PREF_KEY_REFRESH_ACCESS_TOKEN, false);
                            preferencesHelper.putAccessToken(
                                    authentication.getAccessToken());
                            preferencesHelper.putSignInUser(authentication);
                            return toBeResumed;
                        }
                    });
                }
                // re-throw this error because it's not recoverable from here
                return Observable.error(throwable);
            }
        };
    }
}