package org.apache.fineract.online

import android.content.Context
import io.reactivex.Observable
import org.apache.fineract.FakeRemoteDataSource
import org.apache.fineract.R
import org.apache.fineract.data.datamanager.api.DataManagerCustomer
import org.apache.fineract.data.models.customer.identification.Identification
import org.apache.fineract.exceptions.NoConnectivityException
import org.apache.fineract.ui.online.identification.identificationlist.IdentificationsContract
import org.apache.fineract.ui.online.identification.identificationlist.IdentificationsPresenter
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
class IdentificationPresenterTest {

    @Rule
    @JvmField
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    lateinit var mockedView: IdentificationsContract.View

    @Mock
    lateinit var mockedDataManagerCustomer: DataManagerCustomer

    @Mock
    lateinit var mockedContext: Context

    lateinit var identificationPresenter: IdentificationsPresenter

    lateinit var identifications: List<Identification>

    val customerIdentifier: String = "identifier1"

    val searchIdentifierNumber: String = "number1"

    @Before
    fun setup() {
        identificationPresenter = IdentificationsPresenter(mockedContext,
                mockedDataManagerCustomer)
        identificationPresenter.attachView(mockedView)
        identifications = FakeRemoteDataSource.getIdentifications()
    }

    @Test
    fun testGetIdentifications() {
        `when`(mockedDataManagerCustomer.fetchIdentifications(customerIdentifier))
                .thenReturn(Observable.just(identifications))
        identificationPresenter.fetchIdentifications(customerIdentifier)
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showIdentification(identifications)
    }

    @Test
    fun testGetIdentificationsFail() {
        `when`(mockedDataManagerCustomer.fetchIdentifications(customerIdentifier))
                .thenReturn(Observable.error(RuntimeException()))
        `when`(mockedContext.getString(R.string.error_fetching_identification_list))
                .thenReturn("Error while fetching identification cards")
        identificationPresenter.fetchIdentifications(customerIdentifier)
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showError("Error while fetching identification cards")
    }

    @Test
    fun testGetIdentificationsFailDueToInternet() {
        `when`(mockedDataManagerCustomer.fetchIdentifications(customerIdentifier))
                .thenReturn(Observable.error(NoConnectivityException()))
        identificationPresenter.fetchIdentifications(customerIdentifier)
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showNoInternetConnection()
    }

    @Test
    fun testSearchIdentifications() {
        `when`(mockedDataManagerCustomer
                .searchIdentifications(customerIdentifier, searchIdentifierNumber))
                .thenReturn(Observable.just(identifications[0]))
        identificationPresenter.searchIdentifications(customerIdentifier, searchIdentifierNumber)
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).searchIdentificationList(identifications[0])
    }

    @Test
    fun testSearchIdentificationsFail() {
        `when`(mockedDataManagerCustomer
                .searchIdentifications(customerIdentifier, searchIdentifierNumber))
                .thenReturn(Observable.error(RuntimeException()))
        `when`(mockedContext.getString(R.string.error_finding_identification))
                .thenReturn("Error find identification")
        identificationPresenter.searchIdentifications(customerIdentifier, searchIdentifierNumber)
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showError("Error find identification")
    }

    @Test
    fun testSearchIdentificationsFailDueToInternet() {
        `when`(mockedDataManagerCustomer
                .searchIdentifications(customerIdentifier,searchIdentifierNumber))
                .thenReturn(Observable.error(NoConnectivityException()))
        identificationPresenter.searchIdentifications(customerIdentifier,searchIdentifierNumber)
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showNoInternetConnection()
    }

    @After
    fun tearDown() {
        identificationPresenter.detachView()
    }

}