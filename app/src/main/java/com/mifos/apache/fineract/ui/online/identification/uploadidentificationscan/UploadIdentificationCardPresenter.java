package com.mifos.apache.fineract.ui.online.identification.uploadidentificationscan;

import android.content.Context;
import android.graphics.Bitmap;

import com.mifos.apache.fineract.data.datamanager.DataManagerCustomer;
import com.mifos.apache.fineract.injection.ApplicationContext;
import com.mifos.apache.fineract.injection.ConfigPersistent;
import com.mifos.apache.fineract.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

/**
 * @author Rajan Maurya
 *         On 01/08/17.
 */
@ConfigPersistent
public class UploadIdentificationCardPresenter extends
        BasePresenter<UploadIdentificationCardContract.View>
        implements UploadIdentificationCardContract.Presenter {

    private DataManagerCustomer dataManagerCustomer;
    private final CompositeDisposable compositeDisposable;

    @Inject
    protected UploadIdentificationCardPresenter(@ApplicationContext Context context,
            DataManagerCustomer dataManagerCustomer) {
        super(context);
        this.dataManagerCustomer = dataManagerCustomer;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(UploadIdentificationCardContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void uploadIdentificationCardScan(String customerIdentifier, String identificationNumber,
            String scanIdentifier, String description, Bitmap bitmap) {
        MultipartBody.Part file = null;
        checkViewAttached();
        getMvpView().showProgressDialog();
        compositeDisposable.add(dataManagerCustomer.uploadIdentificationCardScan(
                customerIdentifier, identificationNumber, scanIdentifier, description, file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );
    }
}
