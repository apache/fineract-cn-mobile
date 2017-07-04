package com.mifos.apache.fineract.ui.customer;

import android.content.Context;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.datamanager.DataManagerCustomer;
import com.mifos.apache.fineract.data.models.customer.Customer;
import com.mifos.apache.fineract.data.models.customer.CustomerPage;
import com.mifos.apache.fineract.injection.ApplicationContext;
import com.mifos.apache.fineract.injection.ConfigPersistent;
import com.mifos.apache.fineract.ui.base.BasePresenter;

import java.util.List;

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

    private int customerListSize = 50;
    private boolean loadmore = false;

    @Inject
    public CustomersPresenter(@ApplicationContext Context context,
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
    public void fetchCustomers(Integer pageIndex, Boolean loadmore) {
        this.loadmore = loadmore;
        fetchCustomers(pageIndex, customerListSize);
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

                        if (!loadmore && customerPage.getTotalElements() == 0) {
                            getMvpView().showEmptyCustomers(
                                    context.getString(R.string.empty_customer_list));
                        } else if (loadmore && customerPage.getCustomers().size() == 0) {
                            getMvpView().showMessage(
                                    context.getString(R.string.no_more_customer_available));
                        } else {
                            showCustomers(customerPage.getCustomers());
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getMvpView().hideProgressbar();
                        if (loadmore) {
                            getMvpView().showMessage(
                                    context.getString(R.string.error_loading_customers));
                        } else {
                            getMvpView().showError();
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    @Override
    public void showCustomers(List<Customer> customers) {
        if (loadmore) {
            getMvpView().showMoreCustomers(customers);
        } else {
            getMvpView().showCustomers(customers);
        }
    }
}
