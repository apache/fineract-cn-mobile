package com.mifos.apache.fineract.ui.online.loanapplication.loanactivity;

import com.mifos.apache.fineract.data.models.loan.LoanAccount;
import com.mifos.apache.fineract.ui.base.MvpView;

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
