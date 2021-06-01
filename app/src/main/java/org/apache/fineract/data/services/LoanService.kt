package org.apache.fineract.data.services

import io.reactivex.Completable
import io.reactivex.Observable
import org.apache.fineract.data.models.loan.LoanAccount
import org.apache.fineract.data.models.loan.LoanAccountPage
import org.apache.fineract.data.models.product.ProductPage
import org.apache.fineract.data.remote.EndPoints
import retrofit2.http.*

/**
 * Created by Ahmad Jawid Muhammadi on 2/6/20
 */
interface LoanService {

    @GET(EndPoints.API_PORTFOLIO_PATH + "/individuallending/customers/{customeridentifier}/cases")
    fun fetchCustomerLoanAccounts(
            @Path("customeridentifier") customerIdentifier: String?,
            @Query("pageIndex") pageIndex: Int?,
            @Query("size") size: Int?): Observable<LoanAccountPage?>?

    @GET(EndPoints.API_PORTFOLIO_PATH + "/products/{productidentifier}/cases/{caseidentifier}")
    fun fetchCustomerLoanDetails(
            @Path("productidentifier") productIdentifier: String?,
            @Path("caseidentifier") caseIdentifier: String?): Observable<LoanAccount?>?

    @GET(EndPoints.API_PORTFOLIO_PATH + "/products/")
    fun getProducts(
            @Query("pageIndex") pageIndex: Int?,
            @Query("size") size: Int?,
            @Query("includeDisabled") includeDisabled: Boolean?): Observable<ProductPage?>?

    @POST(EndPoints.API_PORTFOLIO_PATH + "/products/{productidentifier}/cases/")
    fun createLoan(
            @Path("productidentifier") productidentifier: String?,
            @Body loanAccount: LoanAccount?): Completable?
}