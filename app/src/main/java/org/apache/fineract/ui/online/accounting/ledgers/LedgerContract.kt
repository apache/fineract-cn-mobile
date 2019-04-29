package org.apache.fineract.ui.online.accounting.accounts

import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.ui.base.MvpView

interface LedgerContract {

    interface View : MvpView {

        fun showUserInterface()

        fun showLedgers(ledgers: List<Ledger>)

        fun showEmptyLedgers()

        fun showRecyclerView(status: Boolean)

        fun showProgressbar()

        fun hideProgressbar()

        fun searchedLedger(ledgers: List<Ledger>)

    }

    interface Presenter {

        fun getLedgersPage()

        fun searchLedger(legderList: List<Ledger>, identifier: String)
    }
}