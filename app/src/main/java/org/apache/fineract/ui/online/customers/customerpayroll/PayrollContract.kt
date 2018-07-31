package org.apache.fineract.ui.online.customers.customerpayroll

import org.apache.fineract.data.models.payroll.PayrollConfiguration
import org.apache.fineract.ui.base.MvpView

interface PayrollContract {

    interface View : MvpView {

        fun showUserInterface()

        fun showPayrollConfiguration(payrollConfiguration: PayrollConfiguration)

        fun showProgressbar()

        fun hideProgressbar()

    }

    interface Presenter {

        fun getPayrollConfiguration(identifier:String)
    }
}