package org.apache.fineract.ui.online.accounting.ledgers.ledgerdetails

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import org.apache.fineract.R
import org.apache.fineract.data.datamanager.DataManagerAccounting
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.ui.base.BasePresenter
import javax.inject.Inject

class LedgerDetailsPresenter @Inject constructor(
        @ApplicationContext context: Context,
        private val dataManagerAccounting: DataManagerAccounting)
    : BasePresenter<LedgerDetailsContract.View>(context), LedgerDetailsContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun deleteLedger(identifier: String) {
        checkViewAttached()
        mvpView.showProgressbar()
        compositeDisposable.add(dataManagerAccounting.deleteLedger(identifier)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        mvpView.hideProgressbar()
                        mvpView.ledgerDeletedSuccessfully()
                    }

                    override fun onError(e: Throwable) {
                        mvpView.hideProgressbar()
                        showExceptionError(e, context.getString(R.string.error_while_deleting_ledger))
                    }
                }))
    }
}