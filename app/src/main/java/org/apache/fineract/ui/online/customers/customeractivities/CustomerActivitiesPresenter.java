package org.apache.fineract.ui.online.customers.customeractivities;

import android.content.Context;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.api.DataManagerCustomer;
import org.apache.fineract.data.models.customer.Command;
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
 *         On 15/08/17.
 */
@ConfigPersistent
public class CustomerActivitiesPresenter extends BasePresenter<CustomerActivitiesContract.View>
        implements CustomerActivitiesContract.Presenter {

    private DataManagerCustomer dataManagerCustomer;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public CustomerActivitiesPresenter(@ApplicationContext Context context,
            DataManagerCustomer dataManagerCustomer) {
        super(context);
        this.dataManagerCustomer = dataManagerCustomer;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(CustomerActivitiesContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void fetchCustomerCommands(String customerIdentifier) {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerCustomer.fetchCustomerCommands(customerIdentifier)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Command>>() {
                    @Override
                    public void onNext(List<Command> commands) {
                        getMvpView().hideProgressbar();
                        if (!commands.isEmpty()) {
                            getMvpView().showCustomerCommands(commands);
                        } else {
                            getMvpView().showEmptyCommands(
                                    context.getString(R.string.empty_customer_activities));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getMvpView().hideProgressbar();
                        showExceptionError(throwable,
                                context.getString(R.string.error_fetching_customer_activities));
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

}