package com.mifos.apache.fineract.ui.depositdetails;

import android.content.Context;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.datamanager.DataManagerDeposit;
import com.mifos.apache.fineract.data.models.deposit.CustomerDepositAccounts;
import com.mifos.apache.fineract.injection.ApplicationContext;
import com.mifos.apache.fineract.injection.ConfigPersistent;
import com.mifos.apache.fineract.ui.base.BasePresenter;

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
public class CustomerDepositDetailsPresenter extends
        BasePresenter<CustomerDepositDetailsContract.View> implements
        CustomerDepositDetailsContract.Presenter {

    private final DataManagerDeposit dataManagerDeposit;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public CustomerDepositDetailsPresenter(@ApplicationContext Context context,
            DataManagerDeposit dataManagerDeposit) {
        super(context);
        this.dataManagerDeposit = dataManagerDeposit;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(CustomerDepositDetailsContract.View mvpView) {
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
                .subscribeWith(new DisposableObserver<CustomerDepositAccounts>() {
                    @Override
                    public void onNext(CustomerDepositAccounts customerDepositAccounts) {
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
