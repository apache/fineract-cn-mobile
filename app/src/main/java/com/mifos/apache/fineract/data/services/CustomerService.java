package com.mifos.apache.fineract.data.services;

import com.mifos.apache.fineract.data.models.customer.CustomerPage;
import com.mifos.apache.fineract.data.remote.EndPoints;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Rajan Maurya
 *         On 20/06/17.
 */

public interface CustomerService {

    @GET(EndPoints.API_CUSTOMER_PATH + "/customers" )
    Observable<CustomerPage> fetchCustomers(
            @Query("pageIndex") Integer integer,
            @Query("size") Integer size);
}
