package org.apache.fineract.online

import android.content.Context
import io.reactivex.Observable
import org.apache.fineract.FakeRemoteDataSource
import org.apache.fineract.data.datamanager.DataManagerAccounting
import org.apache.fineract.data.models.accounts.LedgerPage
import org.apache.fineract.exceptions.NoConnectivityException
import org.apache.fineract.ui.online.accounting.accounts.LedgerContract
import org.apache.fineract.ui.online.accounting.ledgers.LedgerPresenter
import org.apache.fineract.util.RxSchedulersOverrideRule
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LedgerPresenterTest {

    @Rule
    @JvmField
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    lateinit var mockedView: LedgerContract.View

    @Mock
    lateinit var mockedAccountingDataManager: DataManagerAccounting

    @Mock
    lateinit var mockedContext: Context

    lateinit var ledgerPresenter: LedgerPresenter

    lateinit var ledgerPage: LedgerPage

    val ledgerIdentifier: String = "identifier1"

    @Before
    fun setup() {
        ledgerPresenter = LedgerPresenter(mockedContext, mockedAccountingDataManager)
        ledgerPresenter.attachView(mockedView)
        ledgerPage = FakeRemoteDataSource.getLedgerPage()
    }

    @Test
    fun testGetLedgerPage() {
        `when`(mockedAccountingDataManager.fetchLedgers()).thenReturn(Observable.just(ledgerPage))
        ledgerPresenter.getLedgersPage()
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showLedgers(ledgerPage.ledgers!!)
    }

    @Test
    fun testGetLedgerPageEmpty() {
        `when`(mockedAccountingDataManager.fetchLedgers())
                .thenReturn(Observable.just(LedgerPage(ledgers = ArrayList())))
        ledgerPresenter.getLedgersPage()
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showEmptyLedgers()
    }

    @Test
    fun testGetLedgerPageFail() {
        val exception = NoConnectivityException()
        `when`(mockedAccountingDataManager.fetchLedgers())
                .thenReturn(Observable.error(exception))
        ledgerPresenter.getLedgersPage()
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showNoInternetConnection()
    }

    @Test
    fun testSearchLedger() {
        `when`(mockedAccountingDataManager.findLedger(ledgerIdentifier))
                .thenReturn(Observable.just(ledgerPage.ledgers!![0]))
        ledgerPresenter.searchLedger(ledgerIdentifier)
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).searchedLedger(ledgerPage.ledgers!![0])
        assertEquals(ledgerIdentifier, ledgerPage.ledgers!![0].identifier)
    }

    @After
    fun tearDown() {
        ledgerPresenter.detachView()
    }

}