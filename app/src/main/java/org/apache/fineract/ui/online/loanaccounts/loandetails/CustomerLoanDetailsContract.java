package org.apache.fineract.ui.online.loanaccounts.loandetails;

import org.apache.fineract.data.models.loan.LoanAccount;
import org.apache.fineract.ui.base.MvpView;

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
