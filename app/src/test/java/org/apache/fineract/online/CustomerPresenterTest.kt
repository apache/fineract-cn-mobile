package org.apache.fineract.online

import android.content.Context
import io.reactivex.Observable
import org.apache.fineract.FakeRemoteDataSource
import org.apache.fineract.R
import org.apache.fineract.data.datamanager.contracts.ManagerCustomer
import org.apache.fineract.data.datamanager.database.DbManagerCustomer
import org.apache.fineract.data.models.customer.CustomerPage
import org.apache.fineract.exceptions.NoConnectivityException
import org.apache.fineract.ui.online.customers.customerlist.CustomersContract
import org.apache.fineract.ui.online.customers.customerlist.CustomersPresenter
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
class CustomerPresenterTest {

    @Rule
    @JvmField
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    lateinit var mockedView: CustomersContract.View

    @Mock
    lateinit var mockedCustomerDataManager: DbManagerCustomer

    lateinit var mockedManagerCustomer: ManagerCustomer

    @Mock
    lateinit var mockedContext: Context

    lateinit var customerPresenter: CustomersPresenter

    lateinit var customerPage: CustomerPage

    val size = 10

    val pageIndex: Int = 1

    @Before
    fun setup() {
        customerPresenter = CustomersPresenter(mockedContext, mockedCustomerDataManager)
        customerPresenter.attachView(mockedView)
        customerPage = FakeRemoteDataSource.getCustomerPage()
        mockedManagerCustomer = mockedCustomerDataManager
    }

    @Test
    fun testGetCustomerPage() {
        `when`(mockedManagerCustomer.fetchCustomers(pageIndex, size))
                .thenReturn(Observable.just(customerPage))
        customerPresenter.fetchCustomers(pageIndex, size)
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showCustomers(customerPage.customers)
    }

    @Test
    fun testGetAccountsPageEmpty() {
        `when`(mockedManagerCustomer.fetchCustomers(pageIndex, size))
                .thenReturn(Observable.just(CustomerPage(totalPages = 0)))
        `when`(mockedContext.getString(R.string.empty_customer_list))
                .thenReturn("Empty customer list")
        customerPresenter.fetchCustomers(pageIndex, size)
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showEmptyCustomers("Empty customer list")
    }


    @Test
    fun testGetAccountsPageFail() {
        val exception = NoConnectivityException()
        `when`(mockedCustomerDataManager.fetchCustomers(pageIndex, size))
                .thenReturn(Observable.error(exception))
        customerPresenter.fetchCustomers(pageIndex, size)
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showNoInternetConnection()
    }

    @After
    fun tearDown() {
        customerPresenter.detachView()
    }

}