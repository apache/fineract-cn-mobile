package com.mifos.apache.fineract.ui.online.customerdeposit;

import com.mifos.apache.fineract.data.models.deposit.CustomerDepositAccounts;
import com.mifos.apache.fineract.ui.base.MvpView;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 07/07/17.
 */
public interface CustomerDepositContract {

    interface View extends MvpView {

        void showUserInterface();

        void showCustomerDeposits(List<CustomerDepositAccounts> customerDepositAccounts);

        void showError(String errorMessage);

        void showRecyclerView(boolean status);

        void showProgressbar();

        void hideProgressbar();
    }

    interface Presenter {

        void fetchCustomerDepositAccounts(String customerIdentifier);
    }
}
