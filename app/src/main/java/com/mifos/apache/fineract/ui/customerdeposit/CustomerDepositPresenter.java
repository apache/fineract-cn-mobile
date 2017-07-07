package com.mifos.apache.fineract.ui.customerdeposit;

import android.content.Context;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.datamanager.DataManagerDeposit;
import com.mifos.apache.fineract.data.models.deposit.CustomerDepositAccounts;
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
 *         On 07/07/17.
 */
@ConfigPersistent
public class CustomerDepositPresenter extends BasePresenter<CustomerDepositContract.View> implements
        CustomerDepositContract.Presenter {

    private final DataManagerDeposit dataManagerDeposit;
    private CompositeDisposable compositeDisposable;

    @Inject
    public CustomerDepositPresenter(@ApplicationContext Context context,
            DataManagerDeposit dataManagerDeposit) {
        super(context);
        this.dataManagerDeposit = dataManagerDeposit;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(CustomerDepositContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void fetchCustomerDepositAccounts(String customerIdentifier) {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerDeposit.getCustomerDepositAccounts(customerIdentifier)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<CustomerDepositAccounts>>() {
                    @Override
                    public void onNext(List<CustomerDepositAccounts> customerDepositAccounts) {
                        getMvpView().hideProgressbar();
                        getMvpView().showCustomerDeposits(customerDepositAccounts);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressbar();
                        getMvpView().showError(
                                context.getString(R.string.error_fetching_deposit_accounts));
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }
}
