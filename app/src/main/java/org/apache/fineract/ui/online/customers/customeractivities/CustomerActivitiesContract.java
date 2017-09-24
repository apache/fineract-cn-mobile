package org.apache.fineract.ui.online.customers.customeractivities;

import org.apache.fineract.data.models.customer.Command;
import org.apache.fineract.ui.base.MvpView;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 15/08/17.
 */
public interface CustomerActivitiesContract {

    interface View extends MvpView {

        void showUserInterface();

        void showCustomerCommands(List<Command> commands);

        void showEmptyCommands(String message);

        void showRecyclerView(boolean status);

        void showProgressbar();

        void hideProgressbar();
    }

    interface Presenter {

        void fetchCustomerCommands(String customerIdentifier);
    }
}
