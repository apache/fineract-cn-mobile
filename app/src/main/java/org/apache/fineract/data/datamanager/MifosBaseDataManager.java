package org.apache.fineract.data.datamanager;

import org.apache.fineract.data.local.PreferenceKey;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.data.models.Authentication;
import org.apache.fineract.exceptions.ExceptionStatusCode;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author Rajan Maurya
 *         On 20/08/17.
 */
public class MifosBaseDataManager {

    private DataManagerAuth dataManagerAuth;
    private PreferencesHelper preferencesHelper;

    public MifosBaseDataManager(DataManagerAuth dataManagerAuth,
            PreferencesHelper preferencesHelper) {
        this.dataManagerAuth = dataManagerAuth;
        this.preferencesHelper = preferencesHelper;
    }

    public <T> Observable<T> authenticatedObservableApi(Observable<T> observable) {
        return observable.onErrorResumeNext(refreshTokenAndRetryObser(observable));
    }

    public Completable authenticatedCompletableApi(Completable completable) {
        return completable.onErrorResumeNext(refreshTokenAndRetryCompletable(completable));
    }

    public <T> Function<Throwable, ? extends Observable<? extends T>> refreshTokenAndRetryObser(
            final Observable<T> toBeResumed) {
        return new Function<Throwable, Observable<? extends T>>() {
            @Override
            public Observable<? extends T> apply(Throwable throwable) throws Exception {
                // Here check if the error thrown really is a 403
                if (ExceptionStatusCode.isHttp403Error(throwable)) {
                    preferencesHelper.putBoolean(PreferenceKey.PREF_KEY_REFRESH_ACCESS_TOKEN, true);
                    return dataManagerAuth.refreshToken().concatMap(new Function<Authentication,
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

    public Function<Throwable, ? extends CompletableSource> refreshTokenAndRetryCompletable(
            final Completable toBeResumed) {
        return new Function<Throwable, CompletableSource>() {
            @Override
            public CompletableSource apply(Throwable throwable) throws Exception {
                // Here check if the error thrown really is a 403
                if (ExceptionStatusCode.isHttp403Error(throwable)) {
                    preferencesHelper.putBoolean(PreferenceKey.PREF_KEY_REFRESH_ACCESS_TOKEN, true);
                    return dataManagerAuth.refreshToken().flatMapCompletable(
                            new Function<Authentication, CompletableSource>() {
                                @Override
                                public CompletableSource apply(Authentication authentication)
                                        throws Exception {
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
                return Completable.error(throwable);
            }
        };
    }
}
