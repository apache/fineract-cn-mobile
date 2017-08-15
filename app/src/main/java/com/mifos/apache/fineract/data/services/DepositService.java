package com.mifos.apache.fineract.data.services;

import com.mifos.apache.fineract.data.models.deposit.CustomerDepositAccounts;
import com.mifos.apache.fineract.data.models.deposit.ProductDefinition;
import com.mifos.apache.fineract.data.models.deposit.ProductInstance;
import com.mifos.apache.fineract.data.remote.EndPoints;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Rajan Maurya
 *         On 07/07/17.
 */

public interface DepositService {

    @GET(EndPoints.API_DEPOSIT_PATH + "/instances")
    Observable<List<CustomerDepositAccounts>> fetchCustomersDeposits(
            @Query("customer") String customerIdentifier);

    @GET(EndPoints.API_DEPOSIT_PATH + "/instances/{accountIdentifier}")
    Observable<CustomerDepositAccounts> fetchCustomerDepositDetails(
            @Path("accountIdentifier") String accountIdentifier);

    @GET(EndPoints.API_DEPOSIT_PATH + "/definitions")
    Observable<List<ProductDefinition>> fetchProductDefinitions();

    @POST(EndPoints.API_DEPOSIT_PATH + "/instances")
    Completable createDepositAccount(@Body ProductInstance productInstance);

}
