package org.apache.fineract.ui.online.depositaccounts.createdepositaccount.createdepositactivity;

import org.apache.fineract.data.models.deposit.DepositAccount;
import org.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 15/08/17.
 */
public interface CreateDepositContract {

    interface View extends MvpView {

        void depositCreatedSuccessfully();

        void depositUpdatedSuccessfully();

        void showProgressbar();

        void hideProgressbar();

        void showError(String message);
    }

    interface Presenter {

        void createDepositAccount(DepositAccount depositAccount);

        void updateDepositAccount(String accountIdentifier, DepositAccount depositAccount);
    }
}
