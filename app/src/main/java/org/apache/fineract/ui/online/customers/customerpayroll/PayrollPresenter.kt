package org.apache.fineract.ui.online.customers.customerpayroll

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.apache.fineract.R
import org.apache.fineract.data.datamanager.api.DataManagerPayroll
import org.apache.fineract.data.models.payroll.PayrollConfiguration
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.ui.base.BasePresenter
import javax.inject.Inject

class PayrollPresenter @Inject constructor(@ApplicationContext context: Context,
                                           val dataManagerPayroll: DataManagerPayroll)
    : BasePresenter<PayrollContract.View>(context), PayrollContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun getPayrollConfiguration(identifier: String) {

        checkViewAttached()
        mvpView.showProgressbar()

        compositeDisposable.add(dataManagerPayroll.fetchPayrollConfig(identifier)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<PayrollConfiguration>() {
                    override fun onComplete() {

                    }

                    override fun onNext(payrollConfiguration: PayrollConfiguration) {
                        mvpView.hideProgressbar()
                        mvpView.showPayrollConfiguration(payrollConfiguration)
                    }

                    override fun onError(throwable: Throwable) {
                        mvpView.hideProgressbar()
                        showExceptionError(throwable,
                                context.getString(R.string.error_fetching_payrollConfig))
                    }
                })
        )
    }
}