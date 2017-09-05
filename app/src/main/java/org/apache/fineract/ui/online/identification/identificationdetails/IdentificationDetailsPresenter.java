package org.apache.fineract.ui.online.identification.identificationdetails;

import android.content.Context;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.DataManagerCustomer;
import org.apache.fineract.data.models.customer.identification.ScanCard;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.ConfigPersistent;
import org.apache.fineract.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Rajan Maurya
 *         On 01/08/17.
 */
@ConfigPersistent
public class IdentificationDetailsPresenter extends
        BasePresenter<IdentificationDetailsContract.View>
        implements IdentificationDetailsContract.Presenter {

    private DataManagerCustomer dataManagerCustomer;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public IdentificationDetailsPresenter(@ApplicationContext Context context,
            DataManagerCustomer dataManagerCustomer) {
        super(context);
        this.dataManagerCustomer = dataManagerCustomer;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(IdentificationDetailsContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void fetchIdentificationScanCards(String customerIdentifier,
            String identificationNumber) {
        checkViewAttached();
        compositeDisposable.add(dataManagerCustomer
                .fetchIdentificationScanCards(customerIdentifier, identificationNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<ScanCard>>() {
                    @Override
                    public void onNext(List<ScanCard> scanCards) {
                        if (scanCards.size() == 0) {
                            getMvpView().showScansStatus(
                                    context.getString(R.string.empty_scans_to_show));
                        } else {
                            getMvpView().showScanCards(scanCards);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(
                                context.getString(R.string.error_fetching_scans));
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    @Override
    public void deleteIdentificationCard(String customerIdentifier, String identificationNumber) {
        checkViewAttached();
        getMvpView().showProgressDialog();
        compositeDisposable.add(dataManagerCustomer
                .deleteIdentificationCard(customerIdentifier, identificationNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        getMvpView().hideProgressDialog();
                        getMvpView().showIdentifierDeletedSuccessfully();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressDialog();
                        getMvpView().showError(
                                context.getString(R.string.error_deleting_identification_card));
                    }
                })
        );
    }

    @Override
    public void deleteIdentificationCardScan(String customerIdentifier, String identificationNumber,
            final ScanCard scanCard) {
        checkViewAttached();
        getMvpView().showProgressDialog();
        compositeDisposable.add(dataManagerCustomer.deleteIdentificationCardScan(
                customerIdentifier, identificationNumber, scanCard.getIdentifier())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        getMvpView().hideProgressDialog();
                        getMvpView().showIdentificationCardScanDeletedSuccessfully(scanCard);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressDialog();
                        getMvpView().showError(context.getString(
                                R.string.error_deleting_identification_scan_card));
                    }
                })
        );
    }
}
