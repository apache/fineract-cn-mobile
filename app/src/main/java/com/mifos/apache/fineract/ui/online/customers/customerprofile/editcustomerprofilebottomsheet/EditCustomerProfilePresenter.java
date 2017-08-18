package com.mifos.apache.fineract.ui.online.customers.customerprofile.editcustomerprofilebottomsheet;

import android.content.Context;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.datamanager.DataManagerCustomer;
import com.mifos.apache.fineract.injection.ApplicationContext;
import com.mifos.apache.fineract.injection.ConfigPersistent;
import com.mifos.apache.fineract.ui.base.BasePresenter;
import com.mifos.apache.fineract.utils.FileUtils;

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
 *         On 06/08/17.
 */
@ConfigPersistent
public class EditCustomerProfilePresenter extends BasePresenter<EditCustomerProfileContract.View>
        implements EditCustomerProfileContract.Presenter {

    private DataManagerCustomer dataManagerCustomer;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public EditCustomerProfilePresenter(@ApplicationContext Context context,
            DataManagerCustomer dataManagerCustomer) {
        super(context);
        this.dataManagerCustomer = dataManagerCustomer;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(EditCustomerProfileContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void uploadCustomerPortrait(String customerIdentifier, File file) {

        RequestBody requestFile =
                RequestBody.create(MediaType.parse(FileUtils.getMimeType(file)), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("portrait", file.getName(), requestFile);

        checkViewAttached();
        getMvpView().showProgressDialog(context.getString(R.string.uploading_portrait));
        compositeDisposable.add(dataManagerCustomer.uploadCustomerPortrait(customerIdentifier, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        getMvpView().hideProgressDialog();
                        getMvpView().showPortraitUploadedSuccessfully();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressDialog();
                        getMvpView().showMessage(context.getString(
                                R.string.error_uploading_customer_portrait));
                    }
                })
        );
    }

    @Override
    public void deleteCustomerPortrait(String customerIdentifier) {
        checkViewAttached();
        getMvpView().showProgressDialog(context.getString(R.string.deleting_portrait));
        compositeDisposable.add(dataManagerCustomer.deleteCustomerPortrait(customerIdentifier)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        getMvpView().hideProgressDialog();
                        getMvpView().showPortraitDeletedSuccessfully();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressDialog();
                        getMvpView().showMessage(context.getString(
                                R.string.error_deleting_customer_portrait));
                    }
                })
        );
    }
}
