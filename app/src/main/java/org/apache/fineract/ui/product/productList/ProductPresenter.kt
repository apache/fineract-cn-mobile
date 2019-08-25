package org.apache.fineract.ui.product.productList

import android.content.Context
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Predicate
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.apache.fineract.R
import org.apache.fineract.data.datamanager.api.DataManagerProduct
import org.apache.fineract.data.models.product.Product
import org.apache.fineract.data.models.product.ProductPage
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.ui.base.BasePresenter
import javax.inject.Inject

class ProductPresenter @Inject constructor(@ApplicationContext context: Context,
                                           val dataManagerProduct: DataManagerProduct)
    : BasePresenter<ProductContract.View>(context), ProductContract.Presenter {

    val compositeDisposable = CompositeDisposable()

    override fun getProductsPage() {
        checkViewAttached()
        mvpView.showProgressbar()

        compositeDisposable.add(dataManagerProduct.fetchProductPage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<ProductPage>() {
                    override fun onComplete() {

                    }

                    override fun onNext(productPage: ProductPage) {

                        mvpView.hideProgressbar()

                        if (productPage.elements != null) {
                            if (productPage.elements.isEmpty()) {
                                mvpView.showEmptyProduct()
                            } else {
                                mvpView.showProduct(productPage.elements)
                            }
                        }

                    }

                    override fun onError(e: Throwable) {
                        mvpView.hideProgressbar()
                        showExceptionError(e, context.getString(R.string.error_fetching_products))
                    }
                })
        )
    }

    override fun searchProduct(products: List<Product>, query: String) {
        checkViewAttached()
        mvpView.searchedProduct(Observable.fromIterable(products)
                .filter(object : Predicate<Product> {
                    override fun test(product: Product): Boolean {
                        return product.identifier?.toLowerCase()?.contains(query.toLowerCase())
                                .toString().toBoolean()
                    }
                }).toList().blockingGet())
    }
}