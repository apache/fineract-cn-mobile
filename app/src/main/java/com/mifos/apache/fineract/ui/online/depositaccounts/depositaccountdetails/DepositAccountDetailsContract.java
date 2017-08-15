package com.mifos.apache.fineract.ui.online.depositaccounts.depositaccountdetails;

import com.mifos.apache.fineract.data.models.deposit.CustomerDepositAccounts;
import com.mifos.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 12/07/17.
 */

public interface DepositAccountDetailsContract {

    interface View extends MvpView {

        void showDepositDetails(CustomerDepositAccounts customerDepositAccounts);

        void showProgressbar();

        void hideProgressbar();

        void showError(String message);

    }

    interface Presenter {

        void fetchDepositAccountDetails(String accountIdentifier);

    }
}
