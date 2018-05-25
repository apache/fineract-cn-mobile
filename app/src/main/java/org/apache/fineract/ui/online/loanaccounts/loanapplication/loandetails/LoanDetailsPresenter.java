package org.apache.fineract.ui.online.loanaccounts.loanapplication.loandetails;

import android.content.Context;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.api.DataManagerLoans;
import org.apache.fineract.data.models.product.Product;
import org.apache.fineract.data.models.product.ProductPage;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.ConfigPersistent;
import org.apache.fineract.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Rajan Maurya
 *         On 20/07/17.
 */
@ConfigPersistent
public class LoanDetailsPresenter extends BasePresenter<LoanDetailsContract.View>
        implements LoanDetailsContract.Presenter {

    private static final Integer PAGE_INDEX = 0;
    private static final Integer PAGE_SIZE = 100;

    private final DataManagerLoans dataManagerLoans;
    private CompositeDisposable compositeDisposable;

    private List<Product> products;

    @Inject
    public LoanDetailsPresenter(@ApplicationContext Context context,
            DataManagerLoans dataManagerLoans) {
        super(context);
        this.dataManagerLoans = dataManagerLoans;
        compositeDisposable = new CompositeDisposable();
        products = new ArrayList<>();
    }

    @Override
    public void attachView(LoanDetailsContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void fetchProducts() {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerLoans.getProducts(PAGE_INDEX, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ProductPage>() {
                    @Override
                    public void onNext(ProductPage productPage) {
                        getMvpView().hideProgressbar();
                        if (productPage.getTotalElements() == 0) {
                            getMvpView().showEmptyProducts();
                        } else {
                            products = productPage.getElements();
                            getMvpView().showProducts(filterProducts(productPage.getElements()));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getMvpView().hideProgressbar();
                        showExceptionError(throwable,
                                context.getString(R.string.error_loading_products));
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    @Override
    public List<String> filterProducts(List<Product> products) {
        return Observable.fromIterable(products).map(new Function<Product, String>() {
            @Override
            public String apply(Product product) throws Exception {
                return product.getName();
            }
        }).toList().blockingGet();
    }

    @Override
    public void setProductPositionAndValidateViews(Integer position) {
        getMvpView().setComponentsValidations(products.get(position));
    }

    @Override
    public List<String> getCurrentTermUnitType(List<String> unitTypes, final String unitType) {
        return Observable.fromIterable(unitTypes).filter(new Predicate<String>() {
            @Override
            public boolean test(String s) throws Exception {
                return s.equals(unitType.toLowerCase());
            }
        }).toList().blockingGet();
    }
}
