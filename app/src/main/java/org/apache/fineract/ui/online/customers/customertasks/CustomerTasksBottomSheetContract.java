package org.apache.fineract.ui.online.customers.customertasks;

import org.apache.fineract.data.models.customer.Command;
import org.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 27/07/17.
 */

public interface CustomerTasksBottomSheetContract {

    interface View extends MvpView {

        void statusChangedSuccessfully();

        void showProgressbar();

        void hideProgressbar();
    }

    interface Presenter {

        void changeCustomerStatus(String identifier, Command command);
    }
}
