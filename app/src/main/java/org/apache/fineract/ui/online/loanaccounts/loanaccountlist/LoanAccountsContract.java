package org.apache.fineract.ui.online.loanaccounts.loanaccountlist;

import org.apache.fineract.data.models.loan.LoanAccount;
import org.apache.fineract.ui.base.MvpView;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 07/07/17.
 */
public interface LoanAccountsContract {

    interface View extends MvpView {

        void showUserInterface();

        void showLoanAccounts(List<LoanAccount> loanAccounts);

        void showMoreLoanAccounts(List<LoanAccount> loanAccounts);

        void showEmptyLoanAccounts(String message);

        void showRecyclerView(boolean visible);

        void showMessage(String message);

        void showProgressbar();

        void hideProgressbar();

        void searchedLoanAccounts(List<LoanAccount> loanAccountList);
    }

    interface Presenter {

        void searchLoanAccounts(List<LoanAccount> loanAccountList, String query);

        void fetchCustomerLoanAccounts(String customerIdentifier, Integer pageIndex,
                Boolean loadmore);

        void fetchCustomerLoanAccounts(String customerIdentifier, Integer pageIndex);

        void showCustomerLoanAccounts(List<LoanAccount> loanAccounts);
    }
}
