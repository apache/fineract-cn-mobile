package org.apache.fineract.ui.online.depositaccounts.depositaccountslist;

import android.content.Context;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.api.DataManagerDeposit;
import org.apache.fineract.data.models.deposit.DepositAccount;
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
                        if (!customerDepositAccounts.isEmpty()) {
                            getMvpView().showCustomerDeposits(customerDepositAccounts);
                        } else {
                            getMvpView().showEmptyDepositAccounts();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getMvpView().hideProgressbar();
                        showExceptionError(throwable,
                                context.getString(R.string.error_fetching_deposit_accounts));
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }
}
