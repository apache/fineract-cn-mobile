package org.apache.fineract.ui.online.accounting.accounts

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.apache.fineract.R
import org.apache.fineract.data.datamanager.DataManagerAccounting
import org.apache.fineract.data.models.accounts.Account
import org.apache.fineract.data.models.accounts.AccountPage
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.ui.base.BasePresenter
import javax.inject.Inject

class AccountsPresenter @Inject constructor(@ApplicationContext context: Context,
                                            val dataManagerAccounting: DataManagerAccounting)
    : BasePresenter<AccountContract.View>(context), AccountContract.Presenter {

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun getAccountsPage() {
        checkViewAttached()
        mvpView.showProgressbar()

        compositeDisposable.add(dataManagerAccounting.getAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<AccountPage>() {
                    override fun onComplete() {

                    }

                    override fun onNext(accountPage: AccountPage) {

                        mvpView.hideProgressbar()

                        if (accountPage.accounts != null) {
                            if (accountPage.accounts.isEmpty()) {
                                mvpView.showEmptyAccounts()
                            } else {
                                mvpView.showAccounts(accountPage.accounts)
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        mvpView.hideProgressbar()
                        showExceptionError(e, context.getString(R.string.error_fetching_accounts))
                    }
                }))
    }

    override fun searchAccount(query: String) {
        checkViewAttached()
        mvpView.showProgressbar()

        compositeDisposable.add(dataManagerAccounting
                .findAccount(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Account>() {
                    override fun onComplete() {

                    }

                    override fun onNext(account: Account) {
                        mvpView.hideProgressbar()
                        mvpView.searchedAccount(account)

                    }

                    override fun onError(e: Throwable) {

                        mvpView.hideProgressbar()
                        showExceptionError(e, context.getString(R.string.error_fetching_accounts))
                    }
                }))
    }


}