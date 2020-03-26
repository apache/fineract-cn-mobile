package org.apache.fineract.ui.online.accounting.ledgers.ledgerdetails

import org.apache.fineract.ui.base.MvpView

interface LedgerDetailsContract {

    interface View : MvpView {

        fun ledgerDeletedSuccessfully()

        fun showDataOnViews()

        fun showProgressbar()

        fun hideProgressbar()
    }

    interface Presenter {

        fun deleteLedger(identifier: String)
    }
}