package org.apache.fineract.ui.online.identification.identificationlist;

import android.content.Context;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.api.DataManagerCustomer;
import org.apache.fineract.data.models.customer.identification.Identification;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.ConfigPersistent;
import org.apache.fineract.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Rajan Maurya
 * On 31/07/17.
 */
@ConfigPersistent
public class IdentificationsPresenter extends BasePresenter<IdentificationsContract.View>
        implements IdentificationsContract.Presenter {

    private DataManagerCustomer dataManagerCustomer;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public IdentificationsPresenter(@ApplicationContext Context context,
            DataManagerCustomer dataManagerCustomer) {
        super(context);
        this.dataManagerCustomer = dataManagerCustomer;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(IdentificationsContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void fetchIdentifications(String customerIdentifier) {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerCustomer.fetchIdentifications(customerIdentifier)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Identification>>() {
                    @Override
                    public void onNext(List<Identification> identifications) {
                        getMvpView().hideProgressbar();

                        if (identifications.size() == 0) {
                            getMvpView().showEmptyIdentifications();
                        } else {
                            getMvpView().showIdentification(identifications);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getMvpView().hideProgressbar();
                        showExceptionError(throwable,
                                context.getString(R.string.error_fetching_identification_list));
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    @Override
    public void searchIdentifications(List<Identification> identificationList, final String query) {
        checkViewAttached();
        getMvpView().searchedIdentifications(Observable.fromIterable(identificationList).
                filter(new Predicate<Identification>() {
                    @Override
                    public boolean test(Identification identification) {
                        return identification.getType().toLowerCase().contains(query.toLowerCase());
                    }
                }).toList().blockingGet());
    }


}
