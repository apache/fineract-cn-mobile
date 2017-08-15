package com.mifos.apache.fineract.ui.online.customer.customeractivities;

import android.content.Context;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.datamanager.DataManagerCustomer;
import com.mifos.apache.fineract.data.models.customer.Command;
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
                    public void onError(Throwable e) {
                        getMvpView().hideProgressbar();
                        getMvpView().showError(
                                context.getString(R.string.error_fetching_customer_activities));
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }
}