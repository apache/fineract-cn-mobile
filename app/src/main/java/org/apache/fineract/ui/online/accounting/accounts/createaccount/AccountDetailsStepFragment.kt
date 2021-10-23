package org.apache.fineract.ui.online.accounting.accounts.createaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.fragment_step_account_details.*
import kotlinx.android.synthetic.main.fragment_step_account_details.view.*
import kotlinx.android.synthetic.main.fragment_step_group_details.*
import kotlinx.android.synthetic.main.fragment_step_group_details.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.AccountType
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.base.OnItemClickListener
import org.apache.fineract.ui.online.accounting.accounts.AccountAction
import org.apache.fineract.utils.Constants


/*
 * Created by Varun Jain on 21/July/2021
*/

class AccountDetailsStepFragment : FineractBaseFragment(), Step {


    lateinit var rootView: View
    private lateinit var accountAction: AccountAction

    companion object {
        fun newInstance(accountAction: AccountAction) =
            AccountDetailsStepFragment().apply {
                val bundle = Bundle().apply {
                    putSerializable(Constants.ACCOUNT_ACTION, accountAction)
                }
                arguments = bundle
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getSerializable(Constants.ACCOUNT_ACTION)?.let {
            accountAction = it as AccountAction
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_step_account_details, container, false)
        if (accountAction == AccountAction.EDIT) {
            showDataOnViews()
        }

        return rootView
    }

    private fun showDataOnViews() {
        val account = (activity as CreateAccountActivity).getAccount()
        rootView.etIdentifierAccountDetailsStep.setText(account.identifier)
        rootView.etIdentifierAccountDetailsStep.isEnabled = false
        rootView.spinnerTypeAccountDetailsStep.setSelection(getIndexFromAccountType(account.type))
        rootView.etNameAccountDetailsStep.setText(account.name)
        rootView.etAltAccountNumAccountDetailsStep.setText(account.alternativeAccountNumber)
        rootView.etLedgerAccountDetailsStep.setText(account.ledger)
        rootView.etRefAccountNumAccountDetailsStep.setText(account.referenceAccount)
        rootView.etBalanceAccountDetailsStep.setText(account.balance.toString())
    }

    private fun getIndexFromAccountType(accountType: AccountType?): Int {
        when (accountType) {
            AccountType.ASSET -> return 0
            AccountType.LIABILITY -> return 1
            AccountType.EQUITY -> return 2
            AccountType.REVENUE -> return 3
            AccountType.EXPENSE -> return 4
            else -> return 0
        }
    }

    private fun getAccountTypeFromIndex(index: Int): AccountType {
        when (index) {
            0 -> return AccountType.ASSET
            1 -> return AccountType.LIABILITY
            2 -> return AccountType.EQUITY
            3 -> return AccountType.REVENUE
            4 -> return AccountType.EXPENSE
            else -> return AccountType.ASSET
        }
    }

    override fun verifyStep(): VerificationError? {
        if (!validateAlternateAccNo() || !validateBalance() || !validateName()
            || !validateLedger() || !validateRefAccountNum()
        ) {
            return VerificationError(null)
        }
        (activity as CreateAccountActivity).setAccountDetails(
            etIdentifierAccountDetailsStep.text.toString(),
            etBalanceAccountDetailsStep.text.toString().toDouble(),
            if (getAccountTypeFromIndex(spinnerTypeAccountDetailsStep.selectedItemPosition) != null) getAccountTypeFromIndex(
                spinnerTypeAccountDetailsStep.selectedItemPosition
            ) else AccountType.ASSET,
            etAltAccountNumAccountDetailsStep.text.toString(),
            etNameAccountDetailsStep.text.toString(),
            etLedgerAccountDetailsStep.text.toString(),
            etRefAccountNumAccountDetailsStep.text.toString()
        )
        return null
    }

    override fun onSelected() {}

    override fun onError(p0: VerificationError) {}

    private fun validateLedger(): Boolean {
        return etLedgerAccountDetailsStep.validator()
            .minLength(5)
            .addErrorCallback {
                etLedgerAccountDetailsStep.error = it
            }
            .check()
    }

    private fun validateName(): Boolean {
        return etNameAccountDetailsStep.validator()
            .nonEmpty()
            .addErrorCallback {
                etNameAccountDetailsStep.error = it
            }
            .check()
    }

    private fun validateBalance(): Boolean {
        return etBalanceAccountDetailsStep.validator()
            .addErrorCallback {
                etBalanceAccountDetailsStep.error = it
            }
            .nonEmpty()
            .check()
    }

    private fun validateRefAccountNum(): Boolean {
        return etRefAccountNumAccountDetailsStep.validator()
            .minLength(5)
            .addErrorCallback {
                etRefAccountNumAccountDetailsStep.error = it
            }
            .check()
    }

    private fun validateAlternateAccNo(): Boolean {
        return etAltAccountNumAccountDetailsStep.validator()
            .minLength(5)
            .addErrorCallback {
                etAltAccountNumAccountDetailsStep.error = it
            }
            .check()
    }
}