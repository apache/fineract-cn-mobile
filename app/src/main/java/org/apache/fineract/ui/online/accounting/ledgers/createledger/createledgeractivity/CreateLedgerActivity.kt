package org.apache.fineract.ui.online.accounting.ledgers.createledger.createledgeractivity

import android.content.Context
import android.os.Bundle
import android.view.View
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.activity_edit_payroll.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.AccountType
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.ui.adapters.CreateLedgerStepAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.Toaster
import org.apache.fineract.ui.online.accounting.ledgers.LedgerAction
import org.apache.fineract.ui.online.accounting.ledgers.OnNavigationBarListener
import org.apache.fineract.utils.ConstantKeys
import javax.inject.Inject


const val CURRENT_STEP_POSITION = "position"

class CreateLedgerActivity : FineractBaseActivity(),
        StepperLayout.StepperListener,
        OnNavigationBarListener.LedgerDetails,
        OnNavigationBarListener.SubLedger,
        OnNavigationBarListener.LedgerReview,
        CreateLedgerContract.View {

    private var currentPosition = 0
    private var ledger: Ledger? = null
    private var ledgerAction = LedgerAction.CREATE

    @Inject
    lateinit var createLedgerPresenter: CreateLedgerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(R.layout.activity_create_ledger)
        createLedgerPresenter.attachView(this)
        intent?.let {
            ledger = it.getParcelableExtra(ConstantKeys.LEDGER)
            ledgerAction = it.getSerializableExtra(ConstantKeys.LEDGER_ACTION) as LedgerAction
        }

        showBackButton()

        val stepAdapter = CreateLedgerStepAdapter(
                supportFragmentManager, this, ledger, ledgerAction)

        savedInstanceState?.let {
            currentPosition = it.getInt(CURRENT_STEP_POSITION)
        }

        stepperLayout.setAdapter(stepAdapter, currentPosition)
        stepperLayout.setListener(this)
        stepperLayout.setOffscreenPageLimit(stepAdapter.count)

        when (ledgerAction) {
            LedgerAction.CREATE -> {
                setTitle(R.string.create_ledger)
                ledger = Ledger(showAccountsInChart = false, type = AccountType.ASSET)
            }
            LedgerAction.EDIT -> {
                setTitle(R.string.edit_ledger)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_STEP_POSITION, stepperLayout.currentStepPosition)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentPosition = savedInstanceState.getInt(CURRENT_STEP_POSITION)
    }

    override fun onStepSelected(newStepPosition: Int) {
    }

    override fun onError(verificationError: VerificationError?) {
        verificationError?.errorMessage?.let {
            Toaster.show(findViewById<View>(android.R.id.content), it)
        }
    }

    override fun onReturn() {
        finish()
    }

    override fun onCompleted(completeButton: View?) {
        stepperLayout.setNextButtonEnabled(false)
        when (ledgerAction) {
            LedgerAction.EDIT -> {
                ledger?.let {
                    createLedgerPresenter.updateLedger(it.identifier!!, it)
                }
            }
            LedgerAction.CREATE -> {
                ledger?.let {
                    createLedgerPresenter.createLedger(it)
                }
            }
        }
        createLedgerPresenter
    }

    override fun setLedgerDetails(type: String, identifier: String, name: String,
                                  description: String, showAccountInChart: Boolean) {
        ledger?.type = getAccountType(this, type)
        ledger?.identifier = identifier
        ledger?.name = name
        ledger?.description = description
        ledger?.showAccountsInChart = showAccountInChart
    }

    override fun setSubLedger(subLedgerList: List<Ledger>) {
        ledger?.subLedgers = subLedgerList
    }

    override fun getLedger(): Ledger? = ledger

    companion object {
        fun getAccountType(context: Context, type: String): AccountType {
            return when (type) {
                context.getString(R.string.asset) -> AccountType.ASSET
                context.getString(R.string.expense) -> AccountType.EXPENSE
                context.getString(R.string.revenue) -> AccountType.REVENUE
                context.getString(R.string.liability) -> AccountType.LIABILITY
                else -> AccountType.EQUITY
            }
        }
    }

    override fun ledgerCreatedSuccessfully() {
        Toaster.show(findViewById(android.R.id.content), getString(R.string.ledger_created_sucessfully))
        finish()
    }

    override fun ledgerUpdatedSuccessfully() {
        Toaster.show(findViewById(android.R.id.content), getString(R.string.ledger_updated_sucessfully))
        finish()
    }

    override fun showProgressbar() {
        when (ledgerAction) {
            LedgerAction.CREATE ->
                showMifosProgressDialog(getString(R.string.creating_ledger_please_wait))
            LedgerAction.EDIT ->
                showMifosProgressDialog(getString(R.string.updating_ledger_please_wait))
        }
    }

    override fun hideProgressbar() {
        hideMifosProgressDialog()
    }

    override fun showError(message: String?) {
        stepperLayout.setNextButtonEnabled(true)
        Toaster.show(findViewById(android.R.id.content), message)
    }

    override fun showNoInternetConnection() {
        showNoInternetConnection()
    }
}
