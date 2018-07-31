package org.apache.fineract.data.services

import io.reactivex.Observable
import org.apache.fineract.data.models.payroll.PayrollConfiguration
import org.apache.fineract.data.remote.EndPoints
import retrofit2.http.GET
import retrofit2.http.Path


interface PayrollService {

    @GET(EndPoints.API_PAYROLL_PATH + "/customers/{identifier}/payroll")
    fun getPayrollConfig(@Path("identifier") identifier: String): Observable<PayrollConfiguration>
}
