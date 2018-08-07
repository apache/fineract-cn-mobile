package org.apache.fineract.online

import android.content.Context
import io.reactivex.Observable
import org.apache.fineract.FakeRemoteDataSource
import org.apache.fineract.data.datamanager.DataManagerAccounting
import org.apache.fineract.data.models.accounts.AccountPage
import org.apache.fineract.exceptions.NoConnectivityException
import org.apache.fineract.ui.online.accounting.accounts.AccountContract
import org.apache.fineract.ui.online.accounting.accounts.AccountsPresenter
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
class AccountPresenterTest {

    @Rule
    @JvmField
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    lateinit var mockedView: AccountContract.View

    @Mock
    lateinit var mockedAccountingDataManager: DataManagerAccounting

    @Mock
    lateinit var mockedContext: Context

    lateinit var accountPresenter: AccountsPresenter

    lateinit var accountPage: AccountPage

    val accountIdentifier: String = "identifier1"

    @Before
    fun setup() {
        accountPresenter = AccountsPresenter(mockedContext, mockedAccountingDataManager)
        accountPresenter.attachView(mockedView)
        accountPage = FakeRemoteDataSource.getAccountPage()
    }

    @Test
    fun testGetAccountsPage() {
        `when`(mockedAccountingDataManager.getAccounts()).thenReturn(Observable.just(accountPage))
        accountPresenter.getAccountsPage()
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showAccounts(accountPage.accounts!!)
    }

    @Test
    fun testGetAccountsPageEmpty() {
        `when`(mockedAccountingDataManager.getAccounts())
                .thenReturn(Observable.just(AccountPage(accounts = ArrayList())))
        accountPresenter.getAccountsPage()
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showEmptyAccounts()
    }

    @Test
    fun testGetAccountsPageFail() {
        val exception = NoConnectivityException()
        `when`(mockedAccountingDataManager.getAccounts())
                .thenReturn(Observable.error(exception))
        accountPresenter.getAccountsPage()
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showNoInternetConnection()
    }

    @Test
    fun testSearchAccount() {
        `when`(mockedAccountingDataManager.findAccount(accountIdentifier))
                .thenReturn(Observable.just(accountPage.accounts!![0]))
        accountPresenter.searchAccount(accountIdentifier)
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).searchedAccount(accountPage.accounts!![0])
        assertEquals(accountIdentifier, accountPage.accounts!![0].identifier)
    }

    @After
    fun tearDown() {
        accountPresenter.detachView()
    }

}