package org.apache.fineract.ui.online.customers.customerpayroll.editcustomerpayroll

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import org.apache.fineract.R
import org.apache.fineract.data.datamanager.api.DataManagerPayroll
import org.apache.fineract.data.models.payroll.PayrollConfiguration
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.ui.base.BasePresenter
import org.apache.fineract.ui.online.accounting.accounts.EditPayrollContract
import javax.inject.Inject

class EditPayrollPresenter @Inject constructor(@ApplicationContext context: Context,
                                               val dataManagerPayroll: DataManagerPayroll) :
        BasePresenter<EditPayrollContract.View>(context), EditPayrollContract.Presenter {

    val compositeDisposable = CompositeDisposable()

    override fun updatePayrollConfiguration(identifier: String,
                                            payrollConfiguration: PayrollConfiguration) {

        compositeDisposable.add(dataManagerPayroll
                .editPayrollConfig(identifier, payrollConfiguration)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        mvpView.updatePayrollSuccess()
                    }

                    override fun onError(e: Throwable) {
                        mvpView.showError(context.getString(R.string.error_updating_payroll))
                    }
                })
        )
    }
}