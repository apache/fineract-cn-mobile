package org.apache.fineract.ui.online.accounting.ledgers.subledger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sub_ledger_list.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.ui.adapters.SubLedgerAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.utils.ConstantKeys
import javax.inject.Inject

class SubLedgerListActivity : FineractBaseActivity() {

    @Inject
    lateinit var subLedgerAdapter: SubLedgerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_ledger_list)
        activityComponent.inject(this)
        rv_sub_ledgers.adapter = subLedgerAdapter
        val ledger = intent?.getParcelableExtra(ConstantKeys.LEDGER) as Ledger
        ledger.subLedgers?.let {
            subLedgerAdapter.isToReview(true)
            subLedgerAdapter.submitList(it as ArrayList<Ledger>)
        }
        title = getString(R.string.sub_ledger)
        showBackButton()
    }
}
