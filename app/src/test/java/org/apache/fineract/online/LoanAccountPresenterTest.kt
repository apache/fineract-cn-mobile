package org.apache.fineract.online

import android.content.Context
import io.reactivex.Observable
import org.apache.fineract.FakeRemoteDataSource
import org.apache.fineract.R
import org.apache.fineract.data.datamanager.api.DataManagerLoans
import org.apache.fineract.data.models.loan.LoanAccountPage
import org.apache.fineract.exceptions.NoConnectivityException
import org.apache.fineract.ui.online.loanaccounts.loanaccountlist.LoanAccountsContract
import org.apache.fineract.ui.online.loanaccounts.loanaccountlist.LoanAccountsPresenter
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
class LoanAccountPresenterTest {

    @Rule
    @JvmField
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    lateinit var mockedView: LoanAccountsContract.View

    @Mock
    lateinit var mockedLoansDataManager: DataManagerLoans

    @Mock
    lateinit var mockedContext: Context

    lateinit var loanAccountsPresenter: LoanAccountsPresenter

    lateinit var loanAccountPage: LoanAccountPage

    val customerIdentifier: String = "identifier1"
    val pageIndex: Int = 1
    val size = 50

    @Before
    fun setup() {
        loanAccountsPresenter = LoanAccountsPresenter(mockedContext, mockedLoansDataManager)
        loanAccountsPresenter.attachView(mockedView)
        loanAccountPage = FakeRemoteDataSource.getloanAccountPage()
    }

    @Test
    fun testGetLoanAccounts() {
        `when`(mockedLoansDataManager
                .fetchCustomerLoanAccounts(customerIdentifier, pageIndex, size))
                .thenReturn(Observable.just(loanAccountPage))
        loanAccountsPresenter.fetchCustomerLoanAccounts(customerIdentifier, pageIndex)
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showLoanAccounts(loanAccountPage.loanAccounts)
    }

    @Test
    fun testGetEmptyLoanAccounts() {
        `when`(mockedLoansDataManager
                .fetchCustomerLoanAccounts(customerIdentifier, pageIndex, size))
                .thenReturn(Observable.just(LoanAccountPage(totalElements = 0)))
        `when`(mockedContext.getString(R.string.empty_customer_loans))
                .thenReturn("Empty customer loans list")
        loanAccountsPresenter.fetchCustomerLoanAccounts(customerIdentifier, pageIndex)
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showEmptyLoanAccounts(mockedContext
                .getString(R.string.empty_customer_loans))
    }

    @Test
    fun testGetAccountsPageFail() {
        val exception = NoConnectivityException()
        `when`(mockedLoansDataManager
                .fetchCustomerLoanAccounts(customerIdentifier, pageIndex, size))
                .thenReturn(Observable.error(exception))
        loanAccountsPresenter.fetchCustomerLoanAccounts(customerIdentifier, pageIndex)
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showNoInternetConnection()
    }

    @After
    fun tearDown() {
        loanAccountsPresenter.detachView()
    }

}