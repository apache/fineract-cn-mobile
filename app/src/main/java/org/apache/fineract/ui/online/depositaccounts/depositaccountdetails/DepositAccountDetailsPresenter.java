package org.apache.fineract.ui.online.depositaccounts.depositaccountdetails;

import android.content.Context;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.DataManagerDeposit;
import org.apache.fineract.data.models.deposit.DepositAccount;
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
 *         On 12/07/17.
 */
@ConfigPersistent
public class DepositAccountDetailsPresenter extends
        BasePresenter<DepositAccountDetailsContract.View> implements
        DepositAccountDetailsContract.Presenter {

    private final DataManagerDeposit dataManagerDeposit;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public DepositAccountDetailsPresenter(@ApplicationContext Context context,
            DataManagerDeposit dataManagerDeposit) {
        super(context);
        this.dataManagerDeposit = dataManagerDeposit;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(DepositAccountDetailsContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void fetchDepositAccountDetails(String accountIdentifier) {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerDeposit.getCustomerDepositAccountDetails(
                accountIdentifier)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<DepositAccount>() {
                    @Override
                    public void onNext(DepositAccount customerDepositAccounts) {
                        getMvpView().hideProgressbar();
                        getMvpView().showDepositDetails(customerDepositAccounts);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressbar();
                        getMvpView().showError(
                                context.getString(R.string.error_loading_deposit_details));
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }
}
