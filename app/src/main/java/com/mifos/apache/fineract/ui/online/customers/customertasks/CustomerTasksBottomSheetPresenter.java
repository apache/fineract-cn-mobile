package com.mifos.apache.fineract.ui.online.customers.customertasks;

import android.content.Context;

import com.mifos.apache.fineract.data.datamanager.DataManagerCustomer;
import com.mifos.apache.fineract.data.models.customer.Command;
import com.mifos.apache.fineract.injection.ApplicationContext;
import com.mifos.apache.fineract.injection.ConfigPersistent;
import com.mifos.apache.fineract.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Rajan Maurya
 *         On 28/07/17.
 */
@ConfigPersistent
public class CustomerTasksBottomSheetPresenter extends BasePresenter<CustomerTasksBottomSheetContract.View>
        implements CustomerTasksBottomSheetContract.Presenter {

    private DataManagerCustomer dataManagerCustomer;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public CustomerTasksBottomSheetPresenter(@ApplicationContext Context context,
            DataManagerCustomer dataManagerCustomer) {
        super(context);
        this.dataManagerCustomer = dataManagerCustomer;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(CustomerTasksBottomSheetContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void changeCustomerStatus(String identifier, Command command) {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerCustomer.customerCommand(identifier, command)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        getMvpView().hideProgressbar();
                        getMvpView().statusChangedSuccessfully();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressbar();
                        getMvpView().showError();
                    }
                })
        );
    }
}
