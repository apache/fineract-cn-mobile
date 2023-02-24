package org.apache.fineract.online

import android.content.Context
import io.reactivex.Observable
import org.apache.fineract.FakeRemoteDataSource
import org.apache.fineract.data.datamanager.DataManagerAccounting
import org.apache.fineract.data.models.accounts.LedgerPage
import org.apache.fineract.exceptions.NoConnectivityException
import org.apache.fineract.ui.online.accounting.accounts.LedgerListContract
import org.apache.fineract.ui.online.accounting.ledgers.ledgerlist.LedgerListPresenter
import org.apache.fineract.util.RxSchedulersOverrideRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LedgerListPresenterTest {

    @Rule
    @JvmField
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    lateinit var mockedView: LedgerListContract.View

    @Mock
    lateinit var mockedAccountingDataManager: DataManagerAccounting

    @Mock
    lateinit var mockedContext: Context

    lateinit var ledgerListPresenter: LedgerListPresenter

    lateinit var ledgerPage: LedgerPage

    @Before
    fun setup() {
        ledgerListPresenter = LedgerListPresenter(mockedContext, mockedAccountingDataManager)
        ledgerListPresenter.attachView(mockedView)
        ledgerPage = FakeRemoteDataSource.getLedgerPage()
    }

    @Test
    fun testGetLedgerPage() {
        `when`(mockedAccountingDataManager.fetchLedgers()).thenReturn(Observable.just(ledgerPage))
        ledgerListPresenter.getLedgersPage()
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showLedgers(ledgerPage.ledgers!!)
    }

    @Test
    fun testGetLedgerPageEmpty() {
        `when`(mockedAccountingDataManager.fetchLedgers())
                .thenReturn(Observable.just(LedgerPage(ledgers = ArrayList())))
        ledgerListPresenter.getLedgersPage()
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showEmptyLedgers()
    }

    @Test
    fun testGetLedgerPageFail() {
        val exception = NoConnectivityException()
        `when`(mockedAccountingDataManager.fetchLedgers())
                .thenReturn(Observable.error(exception))
        ledgerListPresenter.getLedgersPage()
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showNoInternetConnection()
    }

    @After
    fun tearDown() {
        ledgerListPresenter.detachView()
    }

}