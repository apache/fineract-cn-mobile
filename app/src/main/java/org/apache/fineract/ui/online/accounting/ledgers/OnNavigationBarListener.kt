package org.apache.fineract.ui.online.accounting.ledgers

import org.apache.fineract.data.models.accounts.Ledger

interface OnNavigationBarListener {

    interface LedgerDetails {
        fun setLedgerDetails(
                type: String,
                identifier: String,
                name: String,
                description: String,
                showAccountInChart: Boolean
        )
    }

    interface LedgerReview {
        fun getLedger(): Ledger?
    }

    interface SubLedger {
        fun setSubLedger(subLedgerList: List<Ledger>)
    }
}