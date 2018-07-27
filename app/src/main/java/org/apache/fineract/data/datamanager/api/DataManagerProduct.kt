package org.apache.fineract.data.datamanager.api

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.data.remote.BaseApiManager
import javax.inject.Inject
import org.apache.fineract.FakeRemoteDataSource
import org.apache.fineract.data.models.product.Product
import org.apache.fineract.data.models.product.ProductPage
import javax.inject.Singleton

@Singleton
class DataManagerProduct @Inject constructor(val baseManagerApi: BaseApiManager,
                                             dataManagerAuth: DataManagerAuth,
                                             val preferencesHelper: PreferencesHelper)
    : FineractBaseDataManager(dataManagerAuth, preferencesHelper) {

    fun fetchProductPage(): Observable<ProductPage> =
            baseManagerApi.productService.fetchProductPage()
                    .onErrorResumeNext(Function<Throwable, ObservableSource<ProductPage>>
                    { Observable.just(FakeRemoteDataSource.getProductPage()) })

    fun searchProduct(identifier: String): Observable<Product> =
            baseManagerApi.productService.searchProduct(identifier)
                    .onErrorResumeNext(Function<Throwable, ObservableSource<Product>>
                    { Observable.just(FakeRemoteDataSource.getProductPage()!!.elements!![0]) })

}