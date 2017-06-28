package com.mifos.apache.fineract.ui.customerdetails;

import android.content.Context;

import com.mifos.apache.fineract.data.datamanager.DataManagerCustomer;
import com.mifos.apache.fineract.data.models.customer.Customer;
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
 *         On 26/06/17.
 */
@ConfigPersistent
public class CustomerDetailsPresenter extends BasePresenter<CustomerDetailsContract.View>
        implements CustomerDetailsContract.Presenter {

    private DataManagerCustomer dataManagerCustomer;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public CustomerDetailsPresenter(@ApplicationContext Context context,
            DataManagerCustomer dataManager) {
        super(context);
        dataManagerCustomer = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(CustomerDetailsContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void loanCustomerDetails(String identifier) {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerCustomer.fetchCustomer(identifier)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Customer>() {
                    @Override
                    public void onNext(Customer customer) {
                        getMvpView().hideProgressbar();
                        getMvpView().showCustomerDetails(customer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressbar();
                        getMvpView().showError("Failed to fetch customer details");
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }
}
