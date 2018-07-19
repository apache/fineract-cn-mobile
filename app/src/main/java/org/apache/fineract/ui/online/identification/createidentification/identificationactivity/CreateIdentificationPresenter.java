package org.apache.fineract.ui.online.identification.createidentification.identificationactivity;


import android.content.Context;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.api.DataManagerCustomer;
import org.apache.fineract.data.models.customer.identification.Identification;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.ConfigPersistent;
import org.apache.fineract.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Rajan Maurya
 *         On 31/07/17.
 */
@ConfigPersistent
public class CreateIdentificationPresenter extends BasePresenter<CreateIdentificationContract.View>
        implements CreateIdentificationContract.Presenter {

    private DataManagerCustomer dataManagerCustomer;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public CreateIdentificationPresenter(@ApplicationContext Context context,
            DataManagerCustomer dataManagerCustomer) {
        super(context);
        this.dataManagerCustomer = dataManagerCustomer;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(CreateIdentificationContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void createIdentification(String identifier, Identification identification) {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(
                dataManagerCustomer.createIdentificationCard(identifier, identification)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                getMvpView().hideProgressbar();
                                getMvpView().identificationCreatedSuccessfully();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                getMvpView().hideProgressbar();
                                showExceptionError(throwable, context.getString(
                                        R.string.error_creating_identification_card));
                            }
                        })
        );
    }

    @Override
    public void updateIdentificationCard(String identifier, String identificationNumber,
            Identification identification) {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerCustomer
                .updateIdentificationCard(identifier, identificationNumber, identification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        getMvpView().hideProgressbar();
                        getMvpView().identificationCardEditedSuccessfully();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getMvpView().hideProgressbar();
                        showExceptionError(throwable, context.getString(
                                R.string.error_updating_identification_card));
                    }
                })
        );
    }
}
