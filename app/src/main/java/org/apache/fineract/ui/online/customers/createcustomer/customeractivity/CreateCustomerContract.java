package org.apache.fineract.ui.online.customers.createcustomer.customeractivity;

import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 27/07/17.
 */
public interface CreateCustomerContract {

    interface View extends MvpView {

        void customerCreatedSuccessfully();

        void customerUpdatedSuccessfully();

        void showProgressbar();

        void hideProgressbar();

        void showError(String message);
    }

    interface Presenter {

        void createCustomer(Customer customer);

        void updateCustomer(String customerIdentifier, Customer customer);
    }
}
