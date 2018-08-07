package org.apache.fineract.online

import android.content.Context
import io.reactivex.Observable
import org.apache.fineract.FakeRemoteDataSource
import org.apache.fineract.data.datamanager.api.DataManagerAuth
import org.apache.fineract.data.models.Authentication
import org.apache.fineract.exceptions.NoConnectivityException
import org.apache.fineract.ui.online.login.LoginContract
import org.apache.fineract.ui.online.login.LoginPresenter
import org.apache.fineract.util.RxSchedulersOverrideRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginPresenterTest {

    @Rule
    @JvmField
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    lateinit var mockedView: LoginContract.View

    @Mock
    lateinit var mockedAuthDataManager: DataManagerAuth

    @Mock
    lateinit var mockedContext: Context

    lateinit var loginPresenter: LoginPresenter

    lateinit var authentication: Authentication

    val username = "username"

    val password = "password"

    @Before
    fun setup() {
        loginPresenter = LoginPresenter(mockedAuthDataManager, mockedContext)
        loginPresenter.attachView(mockedView)
        authentication = FakeRemoteDataSource.getAuth()
    }

    @Test
    fun testValidAuthentication() {
        `when`(mockedAuthDataManager.login(username, password)).thenReturn(Observable.just(authentication))
        loginPresenter.login(username, password)
        verify(mockedView).showProgressDialog()
        verify(mockedView).hideProgressDialog()
        verify(mockedView).showUserLoginSuccessfully(authentication)
    }

    @Test
    fun testFailedAuthenticationDueToConnectivity() {
        val exception = NoConnectivityException()
        `when`(mockedAuthDataManager.login(username, password)).thenReturn(Observable.error(exception))
        loginPresenter.login(username, password)
        verify(mockedView).showProgressDialog()
        verify(mockedView).hideProgressDialog()
        verify(mockedView, never()).showUserLoginSuccessfully(authentication)
    }

    @After
    fun tearDown() {
        loginPresenter.detachView()
    }

}