package org.apache.fineract.data.datamanager.api;

import android.util.Base64;

import org.apache.fineract.FakeRemoteDataSource;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.data.models.Authentication;
import org.apache.fineract.data.remote.BaseApiManager;

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
                Base64.encodeToString(password.getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP))
                .onErrorResumeNext(
                        new Function<Throwable, ObservableSource<Authentication>>() {
                            @Override
                            public ObservableSource<Authentication> apply(
                                    Throwable throwable) throws Exception {
                                return Observable.just(FakeRemoteDataSource.getAuth());
                            }
                        });
    }

    public Observable<Authentication> refreshToken() {
        return baseApiManager.getAuthApi().refreshToken();
    }
}