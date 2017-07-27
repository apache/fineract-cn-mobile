package com.mifos.apache.fineract.ui.online.tasks;

import com.mifos.apache.fineract.data.models.customer.Command;
import com.mifos.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 27/07/17.
 */

public interface TasksBottomSheetContract {

    interface View extends MvpView {

        void statusChangedSuccessfully();

        void showProgressbar();

        void hideProgressbar();

        void showError();
    }

    interface Presenter {

        void changeCustomerStatus(String identifier, Command command);
    }
}
