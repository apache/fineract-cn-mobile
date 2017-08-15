package com.mifos.apache.fineract.ui.online.depositaccounts.depositaccountdetails;

import com.mifos.apache.fineract.data.models.deposit.DepositAccount;
import com.mifos.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 12/07/17.
 */

public interface DepositAccountDetailsContract {

    interface View extends MvpView {

        void showDepositDetails(DepositAccount customerDepositAccounts);

        void showProgressbar();

        void hideProgressbar();

        void showError(String message);

    }

    interface Presenter {

        void fetchDepositAccountDetails(String accountIdentifier);

    }
}
