package com.mifos.apache.fineract.ui.login;

import android.content.Context;

import com.mifos.apache.fineract.data.datamanager.DataManagerAuth;
import com.mifos.apache.fineract.data.models.User;
import com.mifos.apache.fineract.injection.ApplicationContext;
import com.mifos.apache.fineract.injection.ConfigPersistent;
import com.mifos.apache.fineract.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Rajan Maurya
 *         On 17/06/17.
 */
@ConfigPersistent
public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private DataManagerAuth dataManagerAuth;
    private CompositeDisposable compositeDisposable;

    @Inject
    public LoginPresenter(DataManagerAuth dataManagerAuth, @ApplicationContext Context context) {
        super(context);
        this.dataManagerAuth = dataManagerAuth;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(LoginContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void login(String username, String password) {
        checkViewAttached();
        getMvpView().showProgressDialog();
        compositeDisposable.add(dataManagerAuth.login(username, password)
                .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        getMvpView().hideProgressDialog();
                        getMvpView().showUserLoginSuccessfully(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressDialog();
                        //TODO extract server generic type of error
                        getMvpView().showError("failed to login");
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }
}
