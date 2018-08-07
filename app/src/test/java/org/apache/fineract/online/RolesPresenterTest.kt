package org.apache.fineract.online

import android.content.Context
import io.reactivex.Observable
import org.apache.fineract.FakeRemoteDataSource
import org.apache.fineract.data.datamanager.api.DataManagerRoles
import org.apache.fineract.data.models.rolesandpermission.Role
import org.apache.fineract.exceptions.NoConnectivityException
import org.apache.fineract.ui.online.roles.roleslist.RolesContract
import org.apache.fineract.ui.online.roles.roleslist.RolesPresenter
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
class RolesPresenterTest {

    @Rule
    @JvmField
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    lateinit var mockedView: RolesContract.View

    @Mock
    lateinit var mockedRoleDataManager: DataManagerRoles

    @Mock
    lateinit var mockedContext: Context

    lateinit var rolePresenter: RolesPresenter

    lateinit var roles: List<Role>

    @Before
    fun setup() {
        rolePresenter = RolesPresenter(mockedContext, mockedRoleDataManager)
        rolePresenter.attachView(mockedView)
        roles = FakeRemoteDataSource.getRoles()
    }

    @Test
    fun testGetRoles() {
        `when`(mockedRoleDataManager.getRoles()).thenReturn(Observable.just(roles))
        rolePresenter.fetchRoles()
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showRoles(roles)
    }

    @Test
    fun testGetRolesEmpty() {
        `when`(mockedRoleDataManager.getRoles()).thenReturn(Observable.just(ArrayList()))
        rolePresenter.fetchRoles()
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showEmptyRoles()
    }

    @Test
    fun testGetAccountsPageFail() {
        val exception = NoConnectivityException()
        `when`(mockedRoleDataManager.getRoles()).thenReturn(Observable.error(exception))
        rolePresenter.fetchRoles()
        verify(mockedView).showProgressbar()
        verify(mockedView).hideProgressbar()
        verify(mockedView).showNoInternetConnection()
    }

    @After
    fun tearDown() {
        rolePresenter.detachView()
    }

}