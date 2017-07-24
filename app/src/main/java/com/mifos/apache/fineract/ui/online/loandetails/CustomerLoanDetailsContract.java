package com.mifos.apache.fineract.ui.online.loandetails;

import com.mifos.apache.fineract.data.models.loan.LoanAccount;
import com.mifos.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 11/07/17.
 */

public interface CustomerLoanDetailsContract {

    interface View extends MvpView {

        void showLoanAccountDetails(LoanAccount loanAccount);

        void showProgressbar();

        void hideProgressbar();

        void showError(String message);
    }

    interface Presenter {

        void fetchCustomerLoanDetails(String productIdentifier, String caseIdentifier);
    }
}
