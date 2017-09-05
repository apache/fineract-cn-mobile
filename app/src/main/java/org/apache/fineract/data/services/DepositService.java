package org.apache.fineract.data.services;

import org.apache.fineract.data.models.deposit.DepositAccount;
import org.apache.fineract.data.models.deposit.ProductDefinition;
import org.apache.fineract.data.remote.EndPoints;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Rajan Maurya
 *         On 07/07/17.
 */

public interface DepositService {

    @GET(EndPoints.API_DEPOSIT_PATH + "/instances")
    Observable<List<DepositAccount>> fetchCustomersDeposits(
            @Query("customer") String customerIdentifier);

    @GET(EndPoints.API_DEPOSIT_PATH + "/instances/{accountIdentifier}")
    Observable<DepositAccount> fetchCustomerDepositDetails(
            @Path("accountIdentifier") String accountIdentifier);

    @GET(EndPoints.API_DEPOSIT_PATH + "/definitions")
    Observable<List<ProductDefinition>> fetchProductDefinitions();

    @POST(EndPoints.API_DEPOSIT_PATH + "/instances")
    Completable createDepositAccount(@Body DepositAccount depositAccount);

    @PUT(EndPoints.API_DEPOSIT_PATH + "/instances/{accountIdentifier}")
    Completable updateDepositAccount(
            @Path("accountIdentifier") String accountIdentifier,
            @Body DepositAccount depositAccount);
}
