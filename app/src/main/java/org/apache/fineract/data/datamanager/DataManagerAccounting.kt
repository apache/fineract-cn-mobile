package org.apache.fineract.data.datamanager

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.data.remote.BaseApiManager
import javax.inject.Inject
import org.apache.fineract.FakeRemoteDataSource
import org.apache.fineract.data.datamanager.api.DataManagerAuth
import org.apache.fineract.data.datamanager.api.FineractBaseDataManager
import org.apache.fineract.data.models.accounts.Account
import org.apache.fineract.data.models.accounts.AccountPage
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.data.models.accounts.LedgerPage
import javax.inject.Singleton

@Singleton
class DataManagerAccounting @Inject constructor(val baseManagerApi: BaseApiManager,
                                                dataManagerAuth: DataManagerAuth,
                                                val preferencesHelper: PreferencesHelper)
    : FineractBaseDataManager(dataManagerAuth, preferencesHelper) {

    fun fetchLedgers(): Observable<LedgerPage> =
            baseManagerApi.accountingService.fetchLedgers()
                    .onErrorResumeNext(Function<Throwable, ObservableSource<LedgerPage>>
                    { Observable.just(FakeRemoteDataSource.getLedgerPage()) })

    fun findLedger(identifier: String): Observable<Ledger> =
            baseManagerApi.accountingService.findLedger(identifier)
                    .onErrorResumeNext(Function<Throwable, ObservableSource<Ledger>>
                    { Observable.just(FakeRemoteDataSource.getLedgerPage()!!.ledgers!![0]) })

    fun getAccounts(): Observable<AccountPage> =
            baseManagerApi.accountingService.fetchAccounts()
                    .onErrorResumeNext(Function<Throwable, ObservableSource<AccountPage>>
                    { Observable.just(FakeRemoteDataSource.getAccountPage()) })


    fun findAccount(identifier: String): Observable<Account> =
            baseManagerApi.accountingService.findAccount(identifier)
                    .onErrorResumeNext(Function<Throwable, ObservableSource<Account>>
                    { Observable.just(FakeRemoteDataSource.getAccountPage()!!.accounts!![0]) })

    fun createLedger(ledger: Ledger): Completable =
            baseManagerApi.accountingService.createLedger(ledger)

    fun updateLedger(identifier: String, ledger: Ledger) =
            baseManagerApi.accountingService.updateLedger(identifier, ledger)

    fun deleteLedger(identifier: String) =
            baseManagerApi.accountingService.deleteLedger(identifier)

}