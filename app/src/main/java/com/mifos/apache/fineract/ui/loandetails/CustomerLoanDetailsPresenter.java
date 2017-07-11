package com.mifos.apache.fineract.ui.loandetails;

import android.content.Context;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.datamanager.DataManagerLoans;
import com.mifos.apache.fineract.data.models.loan.LoanAccount;
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
 *         On 11/07/17.
 */
@ConfigPersistent
public class CustomerLoanDetailsPresenter extends BasePresenter<CustomerLoanDetailsContract.View>
        implements CustomerLoanDetailsContract.Presenter {

    private final DataManagerLoans dataManagerLoans;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public CustomerLoanDetailsPresenter(@ApplicationContext Context context,
            DataManagerLoans dataManagerLoans) {
        super(context);
        this.dataManagerLoans = dataManagerLoans;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(CustomerLoanDetailsContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void fetchCustomerLoanDetails(String productIdentifier, String caseIdentifier) {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerLoans.fetchCustomerLoanDetails(
                productIdentifier, caseIdentifier)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoanAccount>() {
                    @Override
                    public void onNext(LoanAccount loanAccount) {
                        getMvpView().hideProgressbar();
                        getMvpView().showLoanAccountDetails(loanAccount);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressbar();
                        getMvpView().showError(
                                context.getString(R.string.error_loading_customer_loan_details));
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }
}
