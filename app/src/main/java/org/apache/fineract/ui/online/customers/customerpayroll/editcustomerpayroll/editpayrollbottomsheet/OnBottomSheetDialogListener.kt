package org.apache.fineract.ui.online.customers.customerpayroll.editcustomerpayroll.editpayrollbottomsheet

import org.apache.fineract.data.models.payroll.PayrollAllocation


interface OnBottomSheetDialogListener {

    fun editPayrollAllocation(payrollAllocation: PayrollAllocation, position: Int)

    fun addPayrollAllocation(payrollAllocation: PayrollAllocation)
}
