package org.apache.fineract.ui.online.customers.customerlist;

import android.content.Context;
import android.util.Log;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.contracts.ManagerCustomer;
import org.apache.fineract.data.datamanager.database.DbManagerCustomer;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.data.models.customer.CustomerPage;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.ConfigPersistent;
import org.apache.fineract.ui.base.BasePresenter;

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

    private final ManagerCustomer dataManagerCustomer;
    private CompositeDisposable compositeDisposable;

    private int customerListSize = 50;
    private boolean loadmore = false;

    @Inject
    public CustomersPresenter(@ApplicationContext Context context,
            DbManagerCustomer dataManager) {
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

                        if (!loadmore && customerPage.getTotalPages() == 0) {
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
                        Log.d("mytag", throwable.toString());
                        getMvpView().hideProgressbar();
                        if (loadmore) {
                            getMvpView().showMessage(
                                    context.getString(R.string.error_loading_customers));
                        } else {
                            showExceptionError(throwable,
                                    context.getString(R.string.error_loading_customers));
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
