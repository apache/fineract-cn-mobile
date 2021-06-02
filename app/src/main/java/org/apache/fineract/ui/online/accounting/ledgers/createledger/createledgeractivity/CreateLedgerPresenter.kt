package org.apache.fineract.ui.online.accounting.ledgers.createledger.createledgeractivity

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import org.apache.fineract.R
import org.apache.fineract.data.datamanager.DataManagerAccounting
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.ui.base.BasePresenter
import javax.inject.Inject

class CreateLedgerPresenter @Inject constructor(
        @ApplicationContext context: Context,
        private val dataManagerAccounting: DataManagerAccounting) :
        BasePresenter<CreateLedgerContract.View>(context), CreateLedgerContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun updateLedger(identifier: String, ledger: Ledger) {
        checkViewAttached()
        mvpView.showProgressbar()
        compositeDisposable.add(dataManagerAccounting.updateLedger(identifier, ledger)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        mvpView.hideProgressbar()
                        mvpView.ledgerUpdatedSuccessfully()
                    }

                    override fun onError(e: Throwable) {
                        mvpView.hideProgressbar()
                        showExceptionError(e, context.getString(R.string.error_while_updating_ledger))
                    }
                }))
    }

    override fun createLedger(ledger: Ledger) {
        checkViewAttached()
        mvpView.showProgressbar()
        compositeDisposable.add(dataManagerAccounting.createLedger(ledger)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        mvpView.hideProgressbar()
                        mvpView.ledgerCreatedSuccessfully()
                    }

                    override fun onError(e: Throwable) {
                        mvpView.hideProgressbar()
                        showExceptionError(e, context.getString(R.string.error_while_creating_ledger))
                    }
                }))
    }
}