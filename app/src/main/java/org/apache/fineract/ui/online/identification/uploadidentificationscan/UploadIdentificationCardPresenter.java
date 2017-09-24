package org.apache.fineract.ui.online.identification.uploadidentificationscan;

import android.content.Context;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.DataManagerCustomer;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.ConfigPersistent;
import org.apache.fineract.ui.base.BasePresenter;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
            String scanIdentifier, String description, File file) {

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", "scan.png", requestFile);

        checkViewAttached();
        getMvpView().showProgressDialog();
        compositeDisposable.add(dataManagerCustomer.uploadIdentificationCardScan(
                customerIdentifier, identificationNumber, scanIdentifier, description, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        getMvpView().hideProgressDialog();
                        getMvpView().showScanUploadedSuccessfully();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getMvpView().hideProgressDialog();
                        showExceptionError(throwable, context
                                .getString(R.string.error_uploading_identification_scan_card));
                    }
                })
        );
    }
}
