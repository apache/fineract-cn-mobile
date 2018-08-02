package org.apache.fineract.ui.online.accounting.accounts

import org.apache.fineract.data.models.payroll.PayrollConfiguration
import org.apache.fineract.ui.base.MvpView

interface EditPayrollContract {

    interface View : MvpView {

        fun updatePayrollSuccess()
    }

    interface Presenter {

        fun updatePayrollConfiguration(identifier: String,
                                       payrollConfiguration: PayrollConfiguration)
    }
}