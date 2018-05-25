package org.apache.fineract.ui.online.loanaccounts.loanapplication.loanactivity;

import android.content.Context;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.api.DataManagerLoans;
import org.apache.fineract.data.models.loan.LoanAccount;
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
 *         On 24/07/17.
 */
@ConfigPersistent
public class LoanApplicationPresenter extends BasePresenter<LoanApplicationContract.View>
        implements LoanApplicationContract.Presenter {

    private DataManagerLoans dataManagerLoans;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public LoanApplicationPresenter(@ApplicationContext Context context,
            DataManagerLoans dataManagerLoans) {
        super(context);
        this.dataManagerLoans = dataManagerLoans;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(LoanApplicationContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void createLoan(String productIdentifier, LoanAccount loanAccount) {
        checkViewAttached();
        getMvpView().showProgressbar(context.getString(R.string.creating_loan_please_wait));
        compositeDisposable.add(dataManagerLoans.createLoan(productIdentifier, loanAccount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        getMvpView().hideProgressbar();
                        getMvpView().applicationCreatedSuccessfully();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getMvpView().hideProgressbar();
                        showExceptionError(throwable,
                                context.getString(R.string.error_while_creating_loan));
                    }
                })
        );
    }
}
