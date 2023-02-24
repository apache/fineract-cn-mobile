package org.apache.fineract.data.services

import io.reactivex.Completable
import io.reactivex.Observable
import org.apache.fineract.data.models.accounts.Account
import org.apache.fineract.data.models.accounts.AccountPage
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.data.models.accounts.LedgerPage
import org.apache.fineract.data.remote.EndPoints
import retrofit2.http.*


interface AccountingService {

    @GET(EndPoints.API_ACCOUNTING_PATH + "/ledgers")
    fun fetchLedgers(): Observable<LedgerPage>

    @GET(EndPoints.API_ACCOUNTING_PATH + "/ledgers/{identifier}")
    fun findLedger(@Path("identifier") identifier: String): Observable<Ledger>

    @GET(EndPoints.API_ACCOUNTING_PATH + "/accounts")
    fun fetchAccounts(): Observable<AccountPage>

    @GET(EndPoints.API_ACCOUNTING_PATH + "/accounts/{identifier}")
    fun findAccount(@Path("identifier") identifier: String): Observable<Account>

    @POST(EndPoints.API_ACCOUNTING_PATH + "/ledgers")
    fun createLedger(@Body ledger: Ledger): Completable

    @PUT(EndPoints.API_ACCOUNTING_PATH + "/ledgers/{identifier}")
    fun updateLedger(@Path("identifier") identifier: String,
                     @Body ledger: Ledger): Completable

    @DELETE(EndPoints.API_ACCOUNTING_PATH + "ledgers/{identifier}")
    fun deleteLedger(@Path("identifier") identifier: String): Completable

}