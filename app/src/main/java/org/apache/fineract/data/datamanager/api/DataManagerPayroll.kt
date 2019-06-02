package org.apache.fineract.data.datamanager.api

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.data.remote.BaseApiManager
import javax.inject.Inject
import org.apache.fineract.FakeRemoteDataSource
import org.apache.fineract.data.models.payroll.PayrollConfiguration
import javax.inject.Singleton

@Singleton
class DataManagerPayroll @Inject constructor(val baseManagerApi: BaseApiManager,
                                             dataManagerAuth: DataManagerAuth,
                                             val preferencesHelper: PreferencesHelper)
    : FineractBaseDataManager(dataManagerAuth, preferencesHelper) {

    fun fetchPayrollConfig(identifier: String): Observable<PayrollConfiguration> =
            baseManagerApi.payrollService.getPayrollConfig(identifier)
                    .onErrorResumeNext(Function<Throwable, ObservableSource<PayrollConfiguration>>
                    { Observable.just(FakeRemoteDataSource.getPayrollConfig()) })

    fun editPayrollConfig(identifier: String, payrollConfiguration: PayrollConfiguration)
            : Completable = baseManagerApi
            .payrollService.updatePayrollConfig(identifier, payrollConfiguration)


}