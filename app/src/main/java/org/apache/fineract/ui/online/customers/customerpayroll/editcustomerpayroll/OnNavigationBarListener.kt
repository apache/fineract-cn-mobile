package org.apache.fineract.ui.online.customers.customerpayroll.editcustomerpayroll

import org.apache.fineract.data.models.payroll.PayrollAllocation

interface OnNavigationBarListener {

    interface Payroll {

        fun setPayrollConfig(accountNo: String, lastModifiedBy: String,
                             lastModifiedOn: String, createdBy: String,
                             createdOn: String)

        fun setPayrollAllocations(payrollAllocations: List<PayrollAllocation>)
    }
}