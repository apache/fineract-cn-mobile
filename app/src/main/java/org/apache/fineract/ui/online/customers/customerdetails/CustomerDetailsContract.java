package org.apache.fineract.ui.online.customers.customerdetails;

import org.apache.fineract.data.models.customer.ContactDetail;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 26/06/17.
 */
public interface CustomerDetailsContract {

    interface View extends MvpView {

        void showUserInterface();

        void showCustomerDetails(Customer customer);

        void showContactDetails(ContactDetail contactDetail);

        void showToolbarTitleSubtitle(String title, String subtitle);

        void loadCustomerPortrait();

        Customer.State getCustomerStatus();

        void showProgressbar();

        void hideProgressbar();

        void showError(String errorMessage);
    }

    interface Presenter {

        void loanCustomerDetails(String identifier);
    }
}
