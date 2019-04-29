package org.apache.fineract.ui.online.teller

import android.content.Context
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Predicate
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.apache.fineract.R
import org.apache.fineract.data.datamanager.DataManagerTeller
import org.apache.fineract.data.models.teller.Teller
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.ui.base.BasePresenter
import javax.inject.Inject

class TellerPresenter @Inject constructor(@ApplicationContext context: Context,
                                          private val dataManagerTeller: DataManagerTeller) :
        BasePresenter<TellerContract.View>(context), TellerContract.Presenter {

    val compositeDisposable = CompositeDisposable()

    override fun fetchTellers() {

        checkViewAttached()
        mvpView.showProgressbar()

        compositeDisposable.add(dataManagerTeller.getTellers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<Teller>>() {
                    override fun onComplete() {

                    }

                    override fun onNext(tellerList: List<Teller>) {
                        mvpView.hideProgressbar()

                        if (!tellerList.isEmpty()) {
                            mvpView.showTellers(tellerList)
                        } else {
                            mvpView.showEmptyTellers()
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        mvpView.hideProgressbar()
                        showExceptionError(throwable,
                                context.getString(R.string.error_fetching_teller))
                    }
                }))
    }

    override fun searchTeller(tellers: List<Teller>, query: String) {
        checkViewAttached()
        mvpView.searchedTeller(Observable.fromIterable(tellers)
                .filter(object: Predicate<Teller> {
                    override fun test(teller: Teller): Boolean {
                        return teller.tellerAccountIdentifier?.toLowerCase()
                                ?.contains(query.toLowerCase()).toString().toBoolean()
                    }
                }).toList().blockingGet())
    }


}