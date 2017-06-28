package com.mifos.apache.fineract.ui.customerdetails;

import com.mifos.apache.fineract.data.models.customer.Customer;
import com.mifos.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 26/06/17.
 */
public interface CustomerDetailsContract {

    interface View extends MvpView {

        void showUserInterface();

        void showCustomerDetails(Customer customer);

        void showToolbarTitleSubtitle(String title, String subtitle);

        void showProgressbar();

        void hideProgressbar();

        void showError(String errorMessage);
    }

    interface Presenter {

        void loanCustomerDetails(String identifier);
    }
}
