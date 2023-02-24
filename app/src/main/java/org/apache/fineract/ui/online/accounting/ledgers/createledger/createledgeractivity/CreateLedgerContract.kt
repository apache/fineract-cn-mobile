package org.apache.fineract.ui.online.accounting.ledgers.createledger.createledgeractivity

import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.ui.base.MvpView

interface CreateLedgerContract {

    interface View : MvpView {

        fun ledgerCreatedSuccessfully()

        fun ledgerUpdatedSuccessfully()

        fun showProgressbar()

        fun hideProgressbar()
    }

    interface Presenter {

        fun updateLedger(identifier: String, ledger: Ledger)

        fun createLedger(ledger: Ledger)
    }
}