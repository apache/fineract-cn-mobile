package org.apache.fineract.ui.online.login;

import static org.apache.fineract.data.remote.BaseApiManager.retrofit;

import android.content.Context;
import android.util.Log;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.api.DataManagerAuth;
import org.apache.fineract.data.models.Authentication;
import org.apache.fineract.data.models.error.MifosError;
import org.apache.fineract.exceptions.NoConnectivityException;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.ConfigPersistent;
import org.apache.fineract.ui.base.BasePresenter;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * @author Rajan Maurya
 *         On 17/06/17.
 */
@ConfigPersistent
public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private static final String LOG_TAG = LoginPresenter.class.getSimpleName();

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
                .subscribeWith(new DisposableObserver<Authentication>() {
                    @Override
                    public void onNext(Authentication user) {
                        getMvpView().hideProgressDialog();
                        getMvpView().showUserLoginSuccessfully(user);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getMvpView().hideProgressDialog();
                        if (throwable instanceof NoConnectivityException) {
                            getMvpView().showNoInternetConnection();
                        }

                        MifosError mifosError = new MifosError();
                        Converter<ResponseBody, MifosError> errorConverter =
                                retrofit.responseBodyConverter(MifosError.class, new Annotation[0]);
                        if (throwable instanceof HttpException) {
                            HttpException httpException = (HttpException) throwable;
                            Response response = httpException.response();

                            if (response.errorBody() != null) {
                                try {
                                    mifosError = errorConverter.convert(response.errorBody());
                                } catch (IOException e) {
                                    Log.d(LOG_TAG, e.getLocalizedMessage());
                                }
                            }

                            if (mifosError.getMessage() == null) {
                                getMvpView().showError(
                                        context.getString(R.string.wrong_username_or_password));
                            } else {
                                getMvpView().showError(mifosError.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }
}
