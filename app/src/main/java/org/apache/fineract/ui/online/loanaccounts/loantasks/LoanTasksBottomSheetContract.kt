package org.apache.fineract.ui.online.loanaccounts.loantasks

import org.apache.fineract.data.models.customer.Command
import org.apache.fineract.ui.base.MvpView

/**
 * Created by Ahmad Jawid Muhammadi on 6/7/20.
 */

interface LoanTasksBottomSheetContract {
    interface View : MvpView {
        fun statusChangedSuccessfully()
        fun showProgressbar()
        fun hideProgressbar()
    }

    interface Presenter {
        fun changeLoanStatus(identifier: String?, command: Command?)
    }
}