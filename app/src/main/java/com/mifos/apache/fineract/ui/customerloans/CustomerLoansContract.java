package com.mifos.apache.fineract.ui.customerloans;

import com.mifos.apache.fineract.data.models.loan.LoanAccount;
import com.mifos.apache.fineract.ui.base.MvpView;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 07/07/17.
 */
public interface CustomerLoansContract {

    interface View extends MvpView {

        void showUserInterface();

        void showLoanAccounts(List<LoanAccount> loanAccounts);

        void showMoreLoanAccounts(List<LoanAccount> loanAccounts);

        void showEmptyLoanAccounts(String message);

        void showRecyclerView(boolean visible);

        void showMessage(String message);

        void showError();

        void showProgressbar();

        void hideProgressbar();
    }

    interface Presenter {

        void fetchCustomerLoanAccounts(String customerIdentifier, Integer pageIndex,
                Boolean loadmore);

        void fetchCustomerLoanAccounts(String customerIdentifier, Integer pageIndex);

        void showCustomerLoanAccounts(List<LoanAccount> loanAccounts);
    }
}
