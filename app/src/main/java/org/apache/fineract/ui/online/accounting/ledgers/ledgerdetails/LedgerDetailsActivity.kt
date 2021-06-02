package org.apache.fineract.ui.online.accounting.ledgers.ledgerdetails

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_ledger_details_layout.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.Toaster
import org.apache.fineract.ui.online.accounting.ledgers.LedgerAction
import org.apache.fineract.ui.online.accounting.ledgers.createledger.createledgeractivity.CreateLedgerActivity
import org.apache.fineract.ui.online.accounting.ledgers.subledger.SubLedgerListActivity
import org.apache.fineract.utils.ConstantKeys
import org.apache.fineract.utils.MaterialDialog
import org.apache.fineract.utils.Utils
import javax.inject.Inject

class LedgerDetailsActivity : FineractBaseActivity(), LedgerDetailsContract.View {

    private lateinit var ledger: Ledger

    @Inject
    lateinit var ledgerDetailsPresenter: LedgerDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(R.layout.activity_ledger_details_layout)
        ledgerDetailsPresenter.attachView(this)
        setTitle(R.string.ledger_details)
        ledger = intent?.getParcelableExtra(ConstantKeys.LEDGER)!!
        showDataOnViews()

        ll_sub_ledgers.setOnClickListener {
            if (ledger.subLedgers?.size != 0) {
                val intent = Intent(
                        this, SubLedgerListActivity::class.java).apply {
                    putExtra(ConstantKeys.LEDGER, ledger)
                }
                startActivity(intent)
            }
        }

        showBackButton()
    }

    override fun ledgerDeletedSuccessfully() {
        Toaster.show(findViewById(android.R.id.content), getString(R.string.ledger_deleted_sucessfully))
        finish()
    }

    override fun showDataOnViews() {
        tv_type.text = ledger.type.toString()
        tv_identifier.text = ledger.identifier
        tv_name.text = ledger.name
        tv_description.text = ledger.description
        tv_total_value.text = Utils.getPrecision(ledger.totalValue?.toDouble())
        tv_description.text = ledger.description
        tv_parent_ledger_identifier.text = ledger.parentLedgerIdentifier
        cb_show_content_in_chart.isChecked = ledger.showAccountsInChart == true
        tv_created_on.text = ledger.createdOn
        tv_created_by.text = ledger.createdBy
        tv_last_modified_on.text = ledger.lastModifiedOn
        tv_last_modified_by.text = ledger.lastModifiedBy

        if (ledger.subLedgers?.size == 0) {
            tv_sub_ledgers.text = getString(R.string.no_sub_ledger)
        } else {
            tv_sub_ledgers.text = getString(R.string.view_sub_ledgers,
                    ledger.subLedgers?.size)
        }
    }

    override fun showProgressbar() {
        showMifosProgressDialog(getString(R.string.deleting_ledger_please_wait))
    }

    override fun hideProgressbar() {
        hideMifosProgressDialog()
    }

    override fun showError(message: String?) {
        Toaster.show(findViewById(android.R.id.content), message)
    }

    override fun showNoInternetConnection() {
        showNoInternetConnection()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ledger_details_activity_menu, menu)
        Utils.setToolbarIconColor(this, menu, R.color.white)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_edit -> {
                val intent = Intent(this,
                        CreateLedgerActivity::class.java).apply {
                    putExtra(ConstantKeys.LEDGER_ACTION, LedgerAction.EDIT)
                    putExtra(ConstantKeys.LEDGER, ledger)
                }
                startActivity(intent)
            }
            R.id.menu_delete -> {
                MaterialDialog.Builder().apply {
                    init(this@LedgerDetailsActivity)
                    setTitle(getString(R.string.dialog_title_confirm_deletion))
                    setMessage(getString(
                            R.string.dialog_message_confirmation_delete_ledger))
                    setPositiveButton(getString(R.string.delete)
                    ) { _, _ ->
                        ledger.identifier?.let {
                            ledgerDetailsPresenter.deleteLedger(it)
                        }
                    }
                    setNegativeButton(getString(R.string.cancel))
                    createMaterialDialog()
                }.run { show() }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
