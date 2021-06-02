package org.apache.fineract.ui.online.loanaccounts.loantasks

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import org.apache.fineract.R
import org.apache.fineract.data.datamanager.api.DataManagerLoans
import org.apache.fineract.data.models.customer.Command
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.injection.ConfigPersistent
import org.apache.fineract.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Ahmad Jawid Muhammadi on 6/7/20.
 */

/**
 * @author Rajan Maurya
 * On 28/07/17.
 */
@ConfigPersistent
class LoanTasksBottomSheetPresenter @Inject constructor(@ApplicationContext context: Context?,
                                                            private val dataManagerLoans: DataManagerLoans)
    : BasePresenter<LoanTasksBottomSheetContract.View?>(context),
        LoanTasksBottomSheetContract.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    override fun attachView(mvpView: LoanTasksBottomSheetContract.View?) {
        super.attachView(mvpView)
    }

    override fun changeLoanStatus(identifier: String?, command: Command?) {
        checkViewAttached()
        mvpView!!.showProgressbar()
        compositeDisposable.add(dataManagerLoans.loanCommand(identifier, command)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        mvpView?.hideProgressbar()
                        mvpView?.statusChangedSuccessfully()
                    }

                    override fun onError(throwable: Throwable) {
                        mvpView?.hideProgressbar()
                        showExceptionError(throwable,
                                context.getString(R.string.error_updating_loan_status))
                    }
                })
        )
    }
}