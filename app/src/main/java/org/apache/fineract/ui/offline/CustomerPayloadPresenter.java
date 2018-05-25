package org.apache.fineract.ui.offline;

import android.content.Context;

import org.apache.fineract.data.local.database.helpers.DatabaseHelperCustomer;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CustomerPayloadPresenter extends BasePresenter<CustomerPayloadContract.View>
        implements CustomerPayloadContract.Presenter {

    private DatabaseHelperCustomer databaseHelperCustomer;
    private CompositeDisposable compositeDisposable;

    @Inject
    protected CustomerPayloadPresenter(@ApplicationContext Context context,
                                       DatabaseHelperCustomer databaseHelperCustomer) {
        super(context);
        this.databaseHelperCustomer = databaseHelperCustomer;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void fetchCustomers() {
        compositeDisposable.add(databaseHelperCustomer.fetchCustomerPayload()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Customer>>() {
                    @Override
                    public void onNext(List<Customer> customers) {
                        if (customers.size() > 0) {
                            getMvpView().showCustomers(customers);
                        } else {
                            getMvpView().showEmptyCustomers();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void attachView(CustomerPayloadContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.dispose();
    }
}
