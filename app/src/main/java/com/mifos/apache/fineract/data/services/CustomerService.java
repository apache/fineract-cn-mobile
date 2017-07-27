package com.mifos.apache.fineract.data.services;

import com.mifos.apache.fineract.data.models.customer.Command;
import com.mifos.apache.fineract.data.models.customer.Customer;
import com.mifos.apache.fineract.data.models.customer.CustomerPage;
import com.mifos.apache.fineract.data.remote.EndPoints;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @GET(EndPoints.API_CUSTOMER_PATH + "/customers")
    Observable<CustomerPage> searchCustomer(
            @Query("pageIndex") Integer pageIndex,
            @Query("size") Integer size,
            @Query("term") String term);

    @POST(EndPoints.API_CUSTOMER_PATH + "/customers")
    Completable createCustomer(@Body Customer customer);

    @POST(EndPoints.API_CUSTOMER_PATH + "/customers/{identifier}/commands")
    Completable customerCommand(@Path("identifier") String identifier, @Body Command command);
}
