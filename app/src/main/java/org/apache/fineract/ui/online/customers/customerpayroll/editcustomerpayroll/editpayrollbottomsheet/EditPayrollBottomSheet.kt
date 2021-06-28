package org.apache.fineract.ui.online.customers.customerpayroll.editcustomerpayroll.editpayrollbottomsheet

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.View
import kotlinx.android.synthetic.main.bottom_sheet_edit_payroll_allocations.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.payroll.PayrollAllocation
import java.math.BigDecimal

class EditPayrollBottomSheet : BottomSheetDialogFragment() {

    lateinit var payrollAllocation: PayrollAllocation
    lateinit var payrollSource: PayrollSource
    lateinit var onBottomSheetDialogListener: OnBottomSheetDialogListener
    var position: Int = -1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)
        val rootView = View.inflate(context, R.layout.bottom_sheet_edit_payroll_allocations, null)
        bottomSheetDialog.setContentView(rootView)

        rootView.btnCancel.setOnClickListener {
            dismiss()
        }

        rootView.btnAddAllocation.setOnClickListener {

            if (accountValidation(rootView) && amountValidation(rootView)) {
                val updatedPayrollAllocation = PayrollAllocation(accountNumber =
                rootView.etAccount.text.toString(),
                        amount = BigDecimal(rootView.etAmount.text.toString()),
                        proportional = rootView.cbProportional.isChecked)

                when (payrollSource) {

                    PayrollSource.ADD -> onBottomSheetDialogListener
                            .addPayrollAllocation(updatedPayrollAllocation)

                    PayrollSource.EDIT -> onBottomSheetDialogListener
                            .editPayrollAllocation(updatedPayrollAllocation, position)
                }

                dismiss()
            }
        }

        when (payrollSource) {

            PayrollSource.EDIT -> {
                rootView.btnAddAllocation.text = getString(R.string.edit)
                rootView.etAmount.setText("${payrollAllocation.amount}")
                rootView.etAccount.setText(payrollAllocation.accountNumber)
                rootView.cbProportional.isChecked = payrollAllocation.proportional
            }

            PayrollSource.ADD -> {
                rootView.btnAddAllocation.text = getString(R.string.add)
                rootView.etAmount.setText("0")
                rootView.etAccount.setText("")
                rootView.cbProportional.isChecked = false
            }
        }


        return bottomSheetDialog
    }

    private fun accountValidation(rootView: View): Boolean {
        return if (rootView.etAccount.text.isEmpty()) {
            rootView.tlEtAccount.error = getString(R.string.account_should_not_be_empty)
            false
        } else true
    }

    private fun amountValidation(rootView: View): Boolean {
        return if (rootView.etAmount.text.isEmpty() ||
                rootView.etAmount.text.toString().toBigDecimal() <= BigDecimal.ZERO) {
            rootView.tlEtAmount.error = getString(R.string.amount_should_be_greater_than_zero)
            false
        } else true
    }

    fun addPayrollSource(payrollSource: PayrollSource) {
        this.payrollSource = payrollSource
    }

    fun editPayrollAllocation(payrollAllocation: PayrollAllocation, position: Int) {
        this.payrollAllocation = payrollAllocation
        this.position = position
    }

    fun setBottomSheetListener(onBottomSheetDialogListener: OnBottomSheetDialogListener) {
        this.onBottomSheetDialogListener = onBottomSheetDialogListener
    }
}