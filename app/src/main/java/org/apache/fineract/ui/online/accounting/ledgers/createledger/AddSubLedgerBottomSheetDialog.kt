package org.apache.fineract.ui.online.accounting.ledgers.createledger

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_add_debt_income.view.btn_cancel
import kotlinx.android.synthetic.main.bottom_sheet_add_debt_income.view.et_description
import kotlinx.android.synthetic.main.bottom_sheet_add_sub_ledger.view.*
import kotlinx.android.synthetic.main.fragment_create_ledger_details.view.cb_show_acc_in_chart
import kotlinx.android.synthetic.main.fragment_create_ledger_details.view.et_identifier
import kotlinx.android.synthetic.main.fragment_create_ledger_details.view.et_name
import kotlinx.android.synthetic.main.fragment_create_ledger_details.view.sp_account_type
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.AccountType
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.ui.online.accounting.ledgers.LedgerAction
import org.apache.fineract.ui.online.accounting.ledgers.createledger.createledgeractivity.CreateLedgerActivity
import org.apache.fineract.utils.ValidateIdentifierUtil

class AddSubLedgerBottomSheetDialog constructor(
        private var ledger: Ledger?,
        private var action: LedgerAction,
        private var position: Int,
        private var onAddSubLedgerBottomSheetListener: OnAddSubLedgerBottomSheetListener
) : BottomSheetDialogFragment() {

    private lateinit var rootView: View
    private lateinit var behavior: BottomSheetBehavior<View>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =
                super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        rootView = View.inflate(context,
                R.layout.bottom_sheet_add_sub_ledger, null)
        dialog.setContentView(rootView)
        behavior = BottomSheetBehavior.from(rootView.parent as View)
        rootView.btn_cancel.setOnClickListener {
            dismiss()
        }
        rootView.btn_add_sub_ledger.setOnClickListener {
            if (validateIdentifier() && validateName()) {
                saveLedgerDetails()
                onAddSubLedgerBottomSheetListener.onAddButtonClick(action, ledger, position)
                dismiss()
            }
        }
        if (action == LedgerAction.EDIT) {
            showDataOnViews()
            rootView.tv_title.text = getString(R.string.edit_sub_ledger)
            rootView.btn_add_sub_ledger.text = getString(R.string.update_sub_ledger)
        } else {
            ledger = Ledger(AccountType.ASSET, showAccountsInChart = false)
        }
        return dialog
    }

    private fun saveLedgerDetails() {
        ledger?.type = CreateLedgerActivity.getAccountType(
                context!!, rootView.sp_account_type.selectedItem.toString())
        ledger?.identifier = rootView.et_identifier.text.toString()
        ledger?.name = rootView.et_name.text.toString()
        ledger?.description = rootView.et_description.text.toString()
        ledger?.showAccountsInChart =
                rootView.cb_show_acc_in_chart.isChecked == true
    }

    private fun showDataOnViews() {
        setSpinnerItem(ledger?.type)
        rootView.et_identifier.setText(ledger?.identifier)
        rootView.et_name.setText(ledger?.name)
        rootView.et_description.setText(ledger?.description)
        rootView.cb_show_acc_in_chart.isChecked = ledger?.showAccountsInChart == true
    }

    private fun setSpinnerItem(type: AccountType?) {
        when (type) {
            AccountType.ASSET -> setSpinnerSelection(getString(R.string.asset))
            AccountType.LIABILITY -> setSpinnerSelection(getString(R.string.liability))
            AccountType.EQUITY -> setSpinnerSelection(getString(R.string.equity))
            AccountType.REVENUE -> setSpinnerSelection(getString(R.string.revenue))
            AccountType.EXPENSE -> setSpinnerSelection(getString(R.string.expense))
        }
    }

    private fun setSpinnerSelection(text: String) {
        val accountTypes: Array<String> = resources.getStringArray(R.array.ledger_account_types)
        for ((index, item) in accountTypes.withIndex()) {
            if (text == item) rootView.sp_account_type.setSelection(index)
        }
    }

    override fun onStart() {
        super.onStart()
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun validateName(): Boolean {
        if (TextUtils.isEmpty(rootView.et_name.text.toString().trim { it <= ' ' })) {
            ValidateIdentifierUtil.showTextInputLayoutError(
                    rootView.til_name, getString(R.string.required))
            return false
        }
        ValidateIdentifierUtil.showTextInputLayoutError(
                rootView.til_name, null)
        return true
    }

    private fun validateIdentifier(): Boolean {
        if (TextUtils.isEmpty(rootView.et_identifier.text.toString().trim { it <= ' ' })) {
            ValidateIdentifierUtil.showTextInputLayoutError(
                    rootView.til_identifier, getString(R.string.required))
            return false
        }
        ValidateIdentifierUtil.showTextInputLayoutError(
                rootView.til_identifier, null)
        return true
    }
}

class OnAddSubLedgerBottomSheetListener(
        val listener: (action: LedgerAction, ledger: Ledger?, position: Int) -> Unit) {

    fun onAddButtonClick(action: LedgerAction, ledger: Ledger?, position: Int) =
            listener(action, ledger, position)
}
