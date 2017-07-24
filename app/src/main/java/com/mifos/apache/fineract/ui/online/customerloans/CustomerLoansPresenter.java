package com.mifos.apache.fineract.ui.online.customerloans;

import android.content.Context;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.datamanager.DataManagerLoans;
import com.mifos.apache.fineract.data.models.loan.LoanAccount;
import com.mifos.apache.fineract.data.models.loan.LoanAccountPage;
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
public class CustomerLoansPresenter extends BasePresenter<CustomerLoansContract.View>
        implements CustomerLoansContract.Presenter {

    private final DataManagerLoans dataManagerLoans;
    private final CompositeDisposable compositeDisposable;

    private static final int CUSTOMER_LOAN_LIST_SIZE = 50;
    private boolean loadmore = false;

    @Inject
    public CustomerLoansPresenter(@ApplicationContext Context context,
            DataManagerLoans dataManagerLoans) {
        super(context);
        this.dataManagerLoans = dataManagerLoans;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(CustomerLoansContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void fetchCustomerLoanAccounts(String customerIdentifier,
            Integer pageIndex, Boolean loadmore) {
        this.loadmore = loadmore;
        fetchCustomerLoanAccounts(customerIdentifier, pageIndex);
    }

    @Override
    public void fetchCustomerLoanAccounts(String customerIdentifier, Integer pageIndex) {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerLoans.fetchCustomerLoanAccounts(customerIdentifier,
                pageIndex, CUSTOMER_LOAN_LIST_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoanAccountPage>() {
                    @Override
                    public void onNext(LoanAccountPage loanAccountPage) {
                        getMvpView().hideProgressbar();

                        if (!loadmore && loanAccountPage.getTotalElements() == 0) {
                            getMvpView().showEmptyLoanAccounts(
                                    context.getString(R.string.empty_customer_loans));
                        } else if (loadmore && loanAccountPage.getLoanAccounts().size() == 0) {
                            getMvpView().showMessage(
                                    context.getString(R.string.no_more_loans_available));
                        } else {
                            showCustomerLoanAccounts(loanAccountPage.getLoanAccounts());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressbar();
                        if (loadmore) {
                            getMvpView().showMessage(
                                    context.getString(R.string.error_loading_customer_loans));
                        } else {
                            getMvpView().showError();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    @Override
    public void showCustomerLoanAccounts(List<LoanAccount> loanAccounts) {
        if (loadmore) {
            getMvpView().showMoreLoanAccounts(loanAccounts);
        } else {
            getMvpView().showLoanAccounts(loanAccounts);
        }
    }
}
