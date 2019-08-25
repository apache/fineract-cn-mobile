package org.apache.fineract.ui.online.accounting.ledgers.ledgerdetails

import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_ledger_detail.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.utils.ConstantKeys

class LedgerDetailActivity : FineractBaseActivity() {

    lateinit var ledger: Ledger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ledger_detail)

        ledger = intent.getParcelableExtra(ConstantKeys.LEDGER)
        setToolbarTitle(getString(R.string.ledger_details))
        showBackButton()
        populateUserInterface()
    }

    private fun populateUserInterface() {
        tvType.text = ledger.type.toString()
        tvIdentifier.text = ledger.identifier
        tvName.text = ledger.name
        tvParentLedgerIdentifier.text = ledger.parentLedgerIdentifier
        tvDescription.text = ledger.description
        tvTotalValue.text = "$ ${ledger.totalValue}"
        tvSubLedger.text = "\n"
        if (ledger.subLedgers!!.size > 0) {
            var ledgerIterator = ledger.subLedgers?.iterator()
            while (ledgerIterator!!.hasNext())
                tvSubLedger.append("${ledgerIterator.next()}\n")
        } else {
            tvSubLedger.text = getString(R.string.no_sub_ledger_available)
        }
        tvShowAccountInChart.text = ledger.showAccountsInChart.toString()
        tvCreateOn.text = ledger.createdOn
        tvCreatedBy.text = ledger.createdBy
        tvLastModifiedOn.text = ledger.lastModifiedOn
        tvLastModifiedBy.text = ledger.lastModifiedBy
    }
}