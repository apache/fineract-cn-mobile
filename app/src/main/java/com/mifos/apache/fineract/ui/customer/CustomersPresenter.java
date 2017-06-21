package com.mifos.apache.fineract.ui.customer;

import android.content.Context;

import com.mifos.apache.fineract.data.datamanager.DataManagerCustomer;
import com.mifos.apache.fineract.data.models.customer.CustomerPage;
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
 *         On 20/06/17.
 */
@ConfigPersistent
public class CustomersPresenter extends BasePresenter<CustomersContract.View>
        implements CustomersContract.Presenter {

    private final DataManagerCustomer dataManagerCustomer;
    private CompositeDisposable compositeDisposable;

    @Inject
    protected CustomersPresenter(@ApplicationContext Context context,
            DataManagerCustomer dataManager) {
        super(context);
        dataManagerCustomer = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(CustomersContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void fetchCustomers(Integer pageIndex, Integer size) {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerCustomer.fetchCustomers(pageIndex, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CustomerPage>() {
                    @Override
                    public void onNext(CustomerPage customerPage) {
                        getMvpView().hideProgressbar();
                        getMvpView().showCustomers(customerPage);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getMvpView().hideProgressbar();
                        getMvpView().showError("failed to fetch customers");
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }
}
