package org.apache.fineract.ui.online.customers.customerdetails;

import android.content.Context;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.contracts.ManagerCustomer;
import org.apache.fineract.data.datamanager.database.DbManagerCustomer;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.ConfigPersistent;
import org.apache.fineract.ui.base.BasePresenter;

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

    private ManagerCustomer dataManagerCustomer;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public CustomerDetailsPresenter(@ApplicationContext Context context,
            DbManagerCustomer dataManager) {
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
                    public void onError(Throwable throwable) {
                        getMvpView().hideProgressbar();
                        showExceptionError(throwable,
                                context.getString(R.string.error_fetching_customer_details));
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }
}
