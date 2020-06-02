package org.apache.fineract.data.services

import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.MultipartBody
import org.apache.fineract.data.models.customer.Command
import org.apache.fineract.data.models.customer.Customer
import org.apache.fineract.data.models.customer.CustomerPage
import org.apache.fineract.data.models.customer.identification.Identification
import org.apache.fineract.data.models.customer.identification.ScanCard
import org.apache.fineract.data.remote.EndPoints
import retrofit2.http.*

/**
 * Created by Ahmad Jawid Muhammadi on 2/6/20
 */
interface CustomerService {
    @GET(EndPoints.API_CUSTOMER_PATH + "/customers")
    fun fetchCustomers(
            @Query("pageIndex") integer: Int?,
            @Query("size") size: Int?): Observable<CustomerPage?>?

    @GET(EndPoints.API_CUSTOMER_PATH + "/customers/{identifier}")
    fun fetchCustomer(@Path("identifier") identifier: String?): Observable<Customer?>?

    @PUT(EndPoints.API_CUSTOMER_PATH + "/customers/{identifier}")
    fun updateCustomer(
            @Path("identifier") identifier: String?,
            @Body customer: Customer?): Completable?

    @GET(EndPoints.API_CUSTOMER_PATH + "/customers")
    fun searchCustomer(
            @Query("pageIndex") pageIndex: Int?,
            @Query("size") size: Int?,
            @Query("term") term: String?): Observable<CustomerPage?>?

    @POST(EndPoints.API_CUSTOMER_PATH + "/customers")
    fun createCustomer(@Body customer: Customer?): Completable?

    @POST(EndPoints.API_CUSTOMER_PATH + "/customers/{identifier}/commands")
    fun customerCommand(@Path("identifier") identifier: String?, @Body command: Command?): Completable?

    @GET(EndPoints.API_CUSTOMER_PATH + "/customers/{customerIdentifier}/commands")
    fun fetchCustomerCommands(
            @Path("customerIdentifier") customerIdentifier: String?): Observable<List<Command?>?>?

    @GET(EndPoints.API_CUSTOMER_PATH + "/customers/{identifier}/identifications")
    fun fetchIdentification(
            @Path("identifier") identifier: String?): Observable<List<Identification?>?>?

    @GET(EndPoints.API_CUSTOMER_PATH + "/customers/{identifier}/identifications/{number}")
    fun searchIdentification(
            @Path("identifier") identifier: String?, @Path("number") number: String?): Observable<Identification?>?

    @POST(EndPoints.API_CUSTOMER_PATH + "/customers/{identifier}/identifications")
    fun createIdentificationCard(@Path("identifier") identifier: String?,
                                 @Body identification: Identification?): Completable?

    @PUT(EndPoints.API_CUSTOMER_PATH +
            "/customers/{identifier}/identifications/{identificationNumber}")
    fun updateIdentificationCard(
            @Path("identifier") identifier: String?,
            @Path("identificationNumber") identificationNumber: String?,
            @Body identification: Identification?): Completable?

    @GET(EndPoints.API_CUSTOMER_PATH +
            "/customers/{identifier}/identifications/{identificationnumber}/scans")
    fun fetchIdentificationScanCards(
            @Path("identifier") identifier: String?,
            @Path("identificationnumber") identificationnumber: String?): Observable<List<ScanCard?>?>?

    @Multipart
    @POST(EndPoints.API_CUSTOMER_PATH +
            "/customers/{identifier}/identifications/{identificationnumber}/scans")
    fun uploadIdentificationCardScan(
            @Path("identifier") identifier: String?,
            @Path("identificationnumber") identificationnumber: String?,
            @Query("scanIdentifier") scanIdentifier: String?,
            @Query("description") description: String?,
            @Part file: MultipartBody.Part?): Completable?

    @DELETE(EndPoints.API_CUSTOMER_PATH +
            "/customers/{identifier}/identifications/{identificationnumber}/scans/{scanIdentifier}")
    fun deleteIdentificationCardScan(
            @Path("identifier") identifier: String?,
            @Path("identificationnumber") identificationnumber: String?,
            @Path("scanIdentifier") scanIdentifier: String?): Completable?

    @DELETE(EndPoints.API_CUSTOMER_PATH +
            "/customers/{identifier}/identifications/{identificationnumber}")
    fun deleteIdentificationCard(
            @Path("identifier") identifier: String?,
            @Path("identificationnumber") identificationnumber: String?): Completable?

    @Multipart
    @POST(EndPoints.API_CUSTOMER_PATH + "/customers/{identifier}/portrait")
    fun uploadCustomerPortrait(
            @Path("identifier") customerIdentifier: String?,
            @Part file: MultipartBody.Part?): Completable?

    @DELETE(EndPoints.API_CUSTOMER_PATH + "/customers/{identifier}/portrait")
    fun deleteCustomerPortrait(@Path("identifier") customerIdentifier: String?): Completable?
}