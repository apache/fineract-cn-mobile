package org.apache.fineract.ui.online.roles.createrole

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import org.apache.fineract.R
import org.apache.fineract.data.datamanager.api.DataManagerRoles
import org.apache.fineract.data.models.rolesandpermission.Role
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.ui.base.BasePresenter
import javax.inject.Inject

class CreateRolePresenter @Inject constructor(
        @ApplicationContext context: Context,
        private val dataManagerRoles: DataManagerRoles
) : BasePresenter<CreateRoleContract.View>(context),
        CreateRoleContract.Presenter {

    private var compositeDisposable = CompositeDisposable()

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    override fun createRole(role: Role?) {
        checkViewAttached()
        mvpView.showProgressbar()
        compositeDisposable.add(dataManagerRoles.createRole(role)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        mvpView.hideProgressbar()
                        mvpView.roleCreatedSuccessfully()
                    }

                    override fun onError(throwable: Throwable) {
                        mvpView.hideProgressbar()
                        showExceptionError(throwable,
                                context.getString(R.string.error_creating_role))
                    }
                }))
    }

    override fun updateRole(roleIdentifier: String?, role: Role?) {
        checkViewAttached()
        mvpView.showProgressbar()
        compositeDisposable.add(dataManagerRoles.updateRole(
                roleIdentifier, role)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        mvpView.hideProgressbar()
                        mvpView.roleUpdatedSuccessfully()
                    }

                    override fun onError(throwable: Throwable) {
                        mvpView.hideProgressbar()
                        showExceptionError(throwable,
                                context.getString(R.string.error_updating_role))
                    }
                }))
    }

    override fun deleteRole(identifier: String?) {
        checkViewAttached()
        mvpView.showProgressbar()
        compositeDisposable.add(dataManagerRoles.deleteRole(identifier)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        mvpView.hideProgressbar()
                        mvpView.roleDeletedSuccessfully()
                    }

                    override fun onError(e: Throwable) {
                        mvpView.hideProgressbar()
                        showExceptionError(e,
                                context.getString(R.string.error_deleting_role))
                    }

                }))
    }
}