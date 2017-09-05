package org.apache.fineract.ui.online.loanaccounts.loanapplication.loanactivity;

import org.apache.fineract.data.models.loan.LoanAccount;
import org.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 24/07/17.
 */
public interface LoanApplicationContract {

    interface View extends MvpView {

        void applicationCreatedSuccessfully();

        void showProgressbar(String message);

        void hideProgressbar();

        void showError(String message);
    }

    interface Presenter {

        void createLoan(String productIdentifier, LoanAccount loanAccount);
    }
}
