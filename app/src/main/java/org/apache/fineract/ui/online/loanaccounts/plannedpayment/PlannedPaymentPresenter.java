package org.apache.fineract.ui.online.loanaccounts.plannedpayment;

import android.content.Context;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.api.DataManagerIndividualLending;
import org.apache.fineract.data.models.payment.PlannedPayment;
import org.apache.fineract.data.models.payment.PlannedPaymentPage;
import org.apache.fineract.exceptions.NoConnectivityException;
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
 *         On 13/07/17.
 */
@ConfigPersistent
public class PlannedPaymentPresenter extends BasePresenter<PlannedPaymentContract.View>
        implements PlannedPaymentContract.Presenter {

    private DataManagerIndividualLending dataManagerIndividualLending;
    private final CompositeDisposable compositeDisposable;

    private static final Integer PAYMENT_LIST_SIZE = 50;
    private boolean loadmore = false;

    @Inject
    public PlannedPaymentPresenter(@ApplicationContext Context context,
            DataManagerIndividualLending dataManagerIndividualLending) {
        super(context);
        this.dataManagerIndividualLending = dataManagerIndividualLending;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(PlannedPaymentContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void fetchPlannedPayment(String productIdentifier, String caseIdentifier,
            Integer pageIndex, String initialDisbursalDate, Boolean loadmore) {
        this.loadmore = loadmore;
        fetchPlannedPayment(productIdentifier, caseIdentifier, pageIndex, initialDisbursalDate);
    }

    @Override
    public void fetchPlannedPayment(String productIdentifier, String caseIdentifier,
            Integer pageIndex, String initialDisbursalDate) {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerIndividualLending.getPaymentScheduleForCase(
                productIdentifier, caseIdentifier, pageIndex, PAYMENT_LIST_SIZE,
                initialDisbursalDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<PlannedPaymentPage>() {
                    @Override
                    public void onNext(PlannedPaymentPage plannedPaymentPage) {
                        getMvpView().hideProgressbar();
                        getMvpView().showPlannedPayment(plannedPaymentPage.getElements());

                        if (!loadmore && plannedPaymentPage.getTotalElements() == 0) {
                            getMvpView().showEmptyPayments(
                                    context.getString(R.string.empty_planned_payments));
                        } else if (loadmore && plannedPaymentPage.getElements().size() == 0) {
                            getMvpView().showMessage(
                                    context.getString(R.string.no_more_planned_payment_available));
                        } else {
                            showPlannedPayment(plannedPaymentPage.getElements());
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getMvpView().hideProgressbar();
                        if (loadmore && !(throwable instanceof NoConnectivityException)) {
                            getMvpView().showMessage(
                                    context.getString(R.string.error_loading_planned_payment));
                        } else if (loadmore && (throwable instanceof NoConnectivityException)) {
                            getMvpView().showMessage(
                                    context.getString(R.string.no_internet_connection));
                        } else {
                            showExceptionError(throwable,
                                    context.getString(R.string.error_loading_planned_payment));
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    @Override
    public void showPlannedPayment(List<PlannedPayment> plannedPayments) {
        if (loadmore) {
            getMvpView().showMorePlannedPayments(plannedPayments);
        } else {
            getMvpView().showPlannedPayment(plannedPayments);
        }
    }
}
