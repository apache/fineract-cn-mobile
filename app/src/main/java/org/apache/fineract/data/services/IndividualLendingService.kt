package org.apache.fineract.data.services

import io.reactivex.Observable
import org.apache.fineract.data.models.payment.PlannedPaymentPage
import org.apache.fineract.data.remote.EndPoints
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Ahmad Jawid Muhammadi on 2/6/20
 */
interface IndividualLendingService {

    @GET(EndPoints.API_PORTFOLIO_PATH
            + "/individuallending/products/{productidentifier}"
            + "/cases/{caseidentifier}/plannedpayments")
    fun getPaymentScheduleForCase(
            @Path("productidentifier") productIdentifier: String?,
            @Path("caseidentifier") caseIdentifier: String?,
            @Query("pageIndex") pageIndex: Int?,
            @Query("size") size: Int?,
            @Query("initialDisbursalDate") initialDisbursalDate: String?): Observable<PlannedPaymentPage?>?
}