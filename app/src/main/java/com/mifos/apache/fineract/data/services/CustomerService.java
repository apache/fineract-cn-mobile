package com.mifos.apache.fineract.data.services;

import com.mifos.apache.fineract.data.models.customer.Command;
import com.mifos.apache.fineract.data.models.customer.Customer;
import com.mifos.apache.fineract.data.models.customer.CustomerPage;
import com.mifos.apache.fineract.data.models.customer.identification.Identification;
import com.mifos.apache.fineract.data.models.customer.identification.ScanCard;
import com.mifos.apache.fineract.data.remote.EndPoints;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Rajan Maurya
 *         On 20/06/17.
 */

public interface CustomerService {

    @GET(EndPoints.API_CUSTOMER_PATH + "/customers")
    Observable<CustomerPage> fetchCustomers(
            @Query("pageIndex") Integer integer,
            @Query("size") Integer size);

    @GET(EndPoints.API_CUSTOMER_PATH + "/customers/{identifier}")
    Observable<Customer> fetchCustomer(@Path("identifier") String identifier);

    @PUT(EndPoints.API_CUSTOMER_PATH + "/customers/{identifier}")
    Completable updateCustomer(
            @Path("identifier") String identifier,
            @Body Customer customer);

    @GET(EndPoints.API_CUSTOMER_PATH + "/customers")
    Observable<CustomerPage> searchCustomer(
            @Query("pageIndex") Integer pageIndex,
            @Query("size") Integer size,
            @Query("term") String term);

    @POST(EndPoints.API_CUSTOMER_PATH + "/customers")
    Completable createCustomer(@Body Customer customer);

    @POST(EndPoints.API_CUSTOMER_PATH + "/customers/{identifier}/commands")
    Completable customerCommand(@Path("identifier") String identifier, @Body Command command);

    @GET(EndPoints.API_CUSTOMER_PATH + "/customers/{customerIdentifier}/commands")
    Observable<List<Command>> fetchCustomerCommands(
            @Path("customerIdentifier") String customerIdentifier);

    @GET(EndPoints.API_CUSTOMER_PATH + "/customers/{identifier}/identifications")
    Observable<List<Identification>> fetchIdentification(
            @Path("identifier") String identifier);

    @POST(EndPoints.API_CUSTOMER_PATH + "/customers/{identifier}/identifications")
    Completable createIdentificationCard(@Path("identifier") String identifier,
            @Body Identification identification);

    @PUT(EndPoints.API_CUSTOMER_PATH +
            "/customers/{identifier}/identifications/{identificationNumber}")
    Completable updateIdentificationCard(
            @Path("identifier") String identifier,
            @Path("identificationNumber") String identificationNumber,
            @Body Identification identification);

    @GET(EndPoints.API_CUSTOMER_PATH +
            "/customers/{identifier}/identifications/{identificationnumber}/scans")
    Observable<List<ScanCard>> fetchIdentificationScanCards(
            @Path("identifier") String identifier,
            @Path("identificationnumber") String identificationnumber);

    @Multipart
    @POST(EndPoints.API_CUSTOMER_PATH +
            "/customers/{identifier}/identifications/{identificationnumber}/scans")
    Completable uploadIdentificationCardScan(
            @Path("identifier") String identifier,
            @Path("identificationnumber") String identificationnumber,
            @Query("scanIdentifier") String scanIdentifier,
            @Query("description") String description,
            @Part MultipartBody.Part file);

    @DELETE(EndPoints.API_CUSTOMER_PATH +
            "/customers/{identifier}/identifications/{identificationnumber}/scans/{scanIdentifier}")
    Completable deleteIdentificationCardScan(
            @Path("identifier") String identifier,
            @Path("identificationnumber") String identificationnumber,
            @Path("scanIdentifier") String scanIdentifier);

    @DELETE(EndPoints.API_CUSTOMER_PATH +
            "/customers/{identifier}/identifications/{identificationnumber}")
    Completable deleteIdentificationCard(
            @Path("identifier") String identifier,
            @Path("identificationnumber") String identificationnumber);

    @Multipart
    @POST(EndPoints.API_CUSTOMER_PATH + "/customers/{identifier}/portrait")
    Completable uploadCustomerPortrait(
            @Path("identifier") String customerIdentifier,
            @Part MultipartBody.Part file);

    @DELETE(EndPoints.API_CUSTOMER_PATH + "/customers/{identifier}/portrait")
    Completable deleteCustomerPortrait(@Path("identifier") String customerIdentifier);
}
