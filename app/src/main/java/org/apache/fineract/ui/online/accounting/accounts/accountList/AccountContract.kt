package org.apache.fineract.ui.online.accounting.accounts.accountList

import org.apache.fineract.data.models.accounts.Account
import org.apache.fineract.ui.base.MvpView

interface AccountContract {

    interface View : MvpView {

        fun showUserInterface()

        fun showAccounts(accounts: List<Account>)

        fun showEmptyAccounts()

        fun showRecyclerView(status: Boolean)

        fun showProgressbar()

        fun hideProgressbar()

        fun searchedAccount(accounts: List<Account>)


    }

    interface Presenter {

        fun getAccountsPage()

        fun searchAccount(account: List<Account>, query: String)
    }
}