package org.apache.fineract.ui.online.customers.customerlist;

import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.ui.base.MvpView;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 20/06/17.
 */
public interface CustomersContract {

    interface View extends MvpView {

        void showUserInterface();

        void showCustomers(List<Customer> customers);

        void showMoreCustomers(List<Customer> customers);

        void showEmptyCustomers(String message);

        void showRecyclerView(boolean visible);

        void showProgressbar();

        void hideProgressbar();

        void showMessage(String message);

        void searchCustomerList(Customer searchedCustomer);
    }

    interface Presenter {

        void fetchCustomers(Integer pageIndex, Boolean loadmore);

        void fetchCustomers(Integer pageIndex, Integer size);

        void showCustomers(List<Customer> customers);

        void searchCustomerOnline(String query);
    }
}
