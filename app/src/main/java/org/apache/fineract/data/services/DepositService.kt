package org.apache.fineract.data.services

import io.reactivex.Completable
import io.reactivex.Observable
import org.apache.fineract.data.models.deposit.DepositAccount
import org.apache.fineract.data.models.deposit.ProductDefinition
import org.apache.fineract.data.remote.EndPoints
import retrofit2.http.*

/**
 * Created by Ahmad Jawid Muhammadi on 2/6/20
 */
interface DepositService {
    @GET(EndPoints.API_DEPOSIT_PATH + "/instances")
    fun fetchCustomersDeposits(
            @Query("customer") customerIdentifier: String?): Observable<List<DepositAccount?>?>?

    @GET(EndPoints.API_DEPOSIT_PATH + "/instances/{accountIdentifier}")
    fun fetchCustomerDepositDetails(
            @Path("accountIdentifier") accountIdentifier: String?): Observable<DepositAccount?>?

    @GET(EndPoints.API_DEPOSIT_PATH + "/definitions")
    fun fetchProductDefinitions(): Observable<List<ProductDefinition?>?>?

    @POST(EndPoints.API_DEPOSIT_PATH + "/instances")
    fun createDepositAccount(@Body depositAccount: DepositAccount?): Completable?

    @PUT(EndPoints.API_DEPOSIT_PATH + "/instances/{accountIdentifier}")
    fun updateDepositAccount(
            @Path("accountIdentifier") accountIdentifier: String?,
            @Body depositAccount: DepositAccount?): Completable?
}