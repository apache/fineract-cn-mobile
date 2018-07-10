package org.apache.fineract.data.datamanager

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.data.models.teller.Teller
import org.apache.fineract.data.remote.BaseApiManager
import javax.inject.Inject
import org.apache.fineract.FakeRemoteDataSource
import org.apache.fineract.data.datamanager.api.DataManagerAuth
import org.apache.fineract.data.datamanager.api.FineractBaseDataManager
import javax.inject.Singleton

@Singleton
class DataManagerTeller @Inject constructor(val baseManagerApi: BaseApiManager,
                                            dataManagerAuth: DataManagerAuth,
                                            val preferencesHelper: PreferencesHelper)
    : FineractBaseDataManager(dataManagerAuth, preferencesHelper) {

    fun getTellers(): Observable<List<Teller>> =
            baseManagerApi.tellerService.getTellerList(preferencesHelper.tenantIdentifier)
                    .onErrorResumeNext(Function<Throwable, ObservableSource<List<Teller>>>
                    { Observable.just(FakeRemoteDataSource.getTeller()) })

    fun findTeller(tellerCode: String): Observable<Teller> = baseManagerApi.tellerService
            .searchTeller(preferencesHelper.tenantIdentifier, tellerCode)
            .onErrorResumeNext(Function<Throwable, ObservableSource<Teller>>
            { Observable.just(FakeRemoteDataSource.getTeller()[0]) })
}