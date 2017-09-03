package com.mifos.apache.fineract.ui.online.depositaccounts.depositaccountslist;

import com.mifos.apache.fineract.data.models.deposit.DepositAccount;
import com.mifos.apache.fineract.ui.base.MvpView;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 07/07/17.
 */
public interface DepositAccountsContract {

    interface View extends MvpView {

        void showUserInterface();

        void showCustomerDeposits(List<DepositAccount> customerDepositAccounts);

        void showEmptyDepositAccounts();

        void showError(String errorMessage);

        void showRecyclerView(boolean status);

        void showProgressbar();

        void hideProgressbar();
    }

    interface Presenter {

        void fetchCustomerDepositAccounts(String customerIdentifier);
    }
}
