package com.mifos.apache.fineract.ui.online.depositaccounts.depositaccountslist;

import android.content.Context;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.datamanager.DataManagerDeposit;
import com.mifos.apache.fineract.data.models.deposit.DepositAccount;
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
public class DepositAccountsPresenter extends BasePresenter<DepositAccountsContract.View> implements
        DepositAccountsContract.Presenter {

    private final DataManagerDeposit dataManagerDeposit;
    private CompositeDisposable compositeDisposable;

    @Inject
    public DepositAccountsPresenter(@ApplicationContext Context context,
            DataManagerDeposit dataManagerDeposit) {
        super(context);
        this.dataManagerDeposit = dataManagerDeposit;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(DepositAccountsContract.View mvpView) {
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
                .subscribeWith(new DisposableObserver<List<DepositAccount>>() {
                    @Override
                    public void onNext(List<DepositAccount> customerDepositAccounts) {
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
