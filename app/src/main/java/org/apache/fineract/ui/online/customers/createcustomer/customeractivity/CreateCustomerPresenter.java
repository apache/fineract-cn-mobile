package org.apache.fineract.ui.online.customers.createcustomer.customeractivity;

import android.content.Context;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.DataManagerCustomer;
import org.apache.fineract.data.models.customer.Customer;
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
 *         On 27/07/17.
 */
@ConfigPersistent
public class CreateCustomerPresenter extends BasePresenter<CreateCustomerContract.View>
        implements CreateCustomerContract.Presenter {

    private DataManagerCustomer dataManagerCustomer;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public CreateCustomerPresenter(@ApplicationContext Context context,
            DataManagerCustomer dataManagerCustomer) {
        super(context);
        this.dataManagerCustomer = dataManagerCustomer;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void createCustomer(final Customer customer) {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerCustomer.createCustomer(customer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        getMvpView().hideProgressbar();
                        getMvpView().customerCreatedSuccessfully();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressbar();
                        getMvpView().showError(context.getString(R.string.error_creating_customer));
                    }
                })

        );
    }

    @Override
    public void updateCustomer(String customerIdentifier, Customer customer) {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerCustomer.updateCustomer(customerIdentifier, customer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        getMvpView().hideProgressbar();
                        getMvpView().customerUpdatedSuccessfully();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressbar();
                        getMvpView().showError(context.getString(R.string.error_updating_customer));
                    }
                })
        );
    }
}
