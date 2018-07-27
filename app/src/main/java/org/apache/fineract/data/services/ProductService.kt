package org.apache.fineract.data.services

import io.reactivex.Observable
import org.apache.fineract.data.models.product.Product
import org.apache.fineract.data.models.product.ProductPage
import org.apache.fineract.data.remote.EndPoints
import retrofit2.http.GET
import retrofit2.http.Path


interface ProductService {

    @GET(EndPoints.API_PORTFOLIO_PATH + "/products")
    fun fetchProductPage(): Observable<ProductPage>

    @GET(EndPoints.API_PORTFOLIO_PATH + "/products/{productidentifier}")
    fun searchProduct(@Path("productidentifier") identifier: String): Observable<Product>
}