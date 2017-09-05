package org.apache.fineract.ui.online.launcher;

import android.content.Context;

import org.apache.fineract.data.datamanager.DataManagerAuth;
import org.apache.fineract.data.models.Authentication;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.ConfigPersistent;
import org.apache.fineract.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Rajan Maurya
 *         On 06/07/17.
 */
@ConfigPersistent
public class LauncherPresenter extends BasePresenter<LauncherContract.View>
        implements LauncherContract.Presenter {

    private final DataManagerAuth dataManagerAuth;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public LauncherPresenter(@ApplicationContext Context context, DataManagerAuth dataManager) {
        super(context);
        dataManagerAuth = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(LauncherContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void refreshAccessToken() {
        checkViewAttached();
        compositeDisposable.add(dataManagerAuth.refreshToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Authentication>() {
                    @Override
                    public void onNext(Authentication authentication) {
                        getMvpView().refreshAccessTokenSuccessfully(authentication);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().refreshAccessTokenFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }
}
