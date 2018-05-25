package org.apache.fineract.ui.online.depositaccounts.createdepositaccount.formdepositassignproduct;

import android.content.Context;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.api.DataManagerDeposit;
import org.apache.fineract.data.models.deposit.ProductDefinition;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.ConfigPersistent;
import org.apache.fineract.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Rajan Maurya
 *         On 13/08/17.
 */
@ConfigPersistent
public class FormDepositAssignProductPresenter extends
        BasePresenter<FormDepositAssignProductContract.View>
        implements FormDepositAssignProductContract.Presenter {

    private DataManagerDeposit dataManagerDeposit;
    private final CompositeDisposable  compositeDisposable;
    private List<ProductDefinition> productDefinition;

    @Inject
    public FormDepositAssignProductPresenter(@ApplicationContext Context context,
            DataManagerDeposit dataManagerDeposit) {
        super(context);
        this.dataManagerDeposit = dataManagerDeposit;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(FormDepositAssignProductContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void fetchProductDefinitions() {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerDeposit.fetchProductDefinitions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<ProductDefinition>>() {
                    @Override
                    public void onNext(List<ProductDefinition> definitions) {
                        getMvpView().hideProgressbar();
                        getMvpView().showProductDefinitions(filterProductsName(definitions));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getMvpView().hideProgressbar();
                        showExceptionError(throwable,
                                context.getString(R.string.error_fetching_deposit_product));
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    @Override
    public List<String> filterProductsName(List<ProductDefinition> productDefinitions) {
        this.productDefinition = productDefinitions;
        return Observable.fromIterable(productDefinitions)
                .map(new Function<ProductDefinition, String>() {
                    @Override
                    public String apply(ProductDefinition productDefinition) throws Exception {
                        return productDefinition.getName();
                    }
                }).toList().blockingGet();
    }

    @Override
    public String getProductIdentifier(Integer position) {
        return productDefinition.get(position).getIdentifier();
    }
}
