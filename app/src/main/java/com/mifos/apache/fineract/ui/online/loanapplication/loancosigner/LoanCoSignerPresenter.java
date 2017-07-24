package com.mifos.apache.fineract.ui.online.loanapplication.loancosigner;

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

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Rajan Maurya
 *         On 24/07/17.
 */
@ConfigPersistent
public class LoanCoSignerPresenter extends BasePresenter<LoanCoSignerContract.View>
        implements LoanCoSignerContract.Presenter {

    private DataManagerCustomer dataManagerCustomer;
    private final CompositeDisposable compositeDisposable;

    private static final Integer PAGE_INDEX = 0;
    private static final Integer PAGE_SIZE = 10;

    @Inject
    public LoanCoSignerPresenter(@ApplicationContext Context context,
            DataManagerCustomer dataManagerCustomer) {
        super(context);
        this.dataManagerCustomer = dataManagerCustomer;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(LoanCoSignerContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void searchCustomer(String term) {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerCustomer.searchCustomer(PAGE_INDEX, PAGE_SIZE, term)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CustomerPage>() {
                    @Override
                    public void onNext(CustomerPage customers) {
                        getMvpView().hideProgressbar();
                        getMvpView().showCustomers(filterCustomerNames(customers.getCustomers()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressbar();
                        getMvpView().showError(context.getString(R.string.error_loading_customers));
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    @Override
    public List<String> filterCustomerNames(List<Customer> customers) {
        return Observable.fromIterable(customers).map(new Function<Customer, String>() {
            @Override
            public String apply(Customer customer) throws Exception {
                return customer.getIdentifier();
            }
        }).toList().blockingGet();
    }

    @Override
    public Boolean findCustomer(final String customer, String [] customers) {
        boolean isCustomerPresent = false;
        for (String s :  customers) {
            if (s.equals(customer)) {
                isCustomerPresent = true;
            }
        }
        return isCustomerPresent;
    }
}
