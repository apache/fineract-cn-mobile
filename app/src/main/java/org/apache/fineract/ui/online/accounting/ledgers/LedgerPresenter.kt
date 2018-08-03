package org.apache.fineract.ui.online.accounting.ledgers

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.apache.fineract.R
import org.apache.fineract.data.datamanager.DataManagerAccounting
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.data.models.accounts.LedgerPage
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.ui.base.BasePresenter
import org.apache.fineract.ui.online.accounting.accounts.LedgerContract
import javax.inject.Inject

class LedgerPresenter @Inject constructor(@ApplicationContext context: Context,
                                          val dataManagerAccounting: DataManagerAccounting)
    : BasePresenter<LedgerContract.View>(context), LedgerContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun getLedgersPage() {
        checkViewAttached()
        mvpView.showProgressbar()

        compositeDisposable.add(dataManagerAccounting.fetchLedgers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<LedgerPage>() {
                    override fun onComplete() {

                    }

                    override fun onNext(ledgerPage: LedgerPage) {

                        mvpView.hideProgressbar()

                        if (ledgerPage.ledgers != null) {

                            if (ledgerPage.ledgers.isEmpty()) {
                                mvpView.showEmptyLedgers()
                            } else {
                                mvpView.showLedgers(ledgerPage.ledgers)
                            }
                        }

                    }

                    override fun onError(throwable: Throwable) {
                        mvpView.hideProgressbar()
                        showExceptionError(throwable,
                                context.getString(R.string.error_fetching_ledger))
                    }
                })
        )
    }

    override fun searchLedger(identifier: String) {
        checkViewAttached()
        mvpView.showProgressbar()

        compositeDisposable.add(dataManagerAccounting.findLedger(identifier)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Ledger>() {
                    override fun onComplete() {

                    }

                    override fun onNext(ledger: Ledger) {
                        mvpView.hideProgressbar()
                        mvpView.searchedLedger(ledger)

                    }

                    override fun onError(throwable: Throwable) {
                        mvpView.hideProgressbar()
                        showExceptionError(throwable,
                                context.getString(R.string.error_fetching_ledger))
                    }
                })
        )
    }


}