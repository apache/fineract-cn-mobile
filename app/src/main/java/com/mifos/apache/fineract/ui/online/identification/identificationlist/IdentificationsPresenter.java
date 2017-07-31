package com.mifos.apache.fineract.ui.online.identification.identificationlist;

import android.content.Context;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.datamanager.DataManagerCustomer;
import com.mifos.apache.fineract.data.models.customer.identification.Identification;
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
 *         On 31/07/17.
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
                            getMvpView().showMessage(
                                    context.getString(R.string.empty_identification_list));
                        } else {
                            getMvpView().showIdentification(identifications);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressbar();
                        getMvpView().showMessage(
                                context.getString(R.string.error_fetching_identification_list));
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }
}
