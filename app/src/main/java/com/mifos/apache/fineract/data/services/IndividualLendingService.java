package com.mifos.apache.fineract.data.services;

import com.mifos.apache.fineract.data.models.payment.PlannedPaymentPage;
import com.mifos.apache.fineract.data.remote.EndPoints;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Rajan Maurya
 *         On 13/07/17.
 */
public interface IndividualLendingService {

    @GET(EndPoints.API_PORTFOLIO_PATH
            + "/individuallending/products/{productidentifier}"
            + "/cases/{caseidentifier}/plannedpayments")
    Observable<PlannedPaymentPage> getPaymentScheduleForCase(
            @Path("productidentifier") String productIdentifier,
            @Path("caseidentifier") String caseIdentifier,
            @Query("pageIndex") Integer pageIndex,
            @Query("size") Integer size,
            @Query("initialDisbursalDate") String initialDisbursalDate);
}
