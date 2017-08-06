package com.mifos.apache.fineract.ui.online.customer.customerlist;

import com.mifos.apache.fineract.data.models.customer.Customer;
import com.mifos.apache.fineract.ui.base.MvpView;

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

        void showError();
    }

    interface Presenter {

        void fetchCustomers(Integer pageIndex, Boolean loadmore);

        void fetchCustomers(Integer pageIndex, Integer size);

        void showCustomers(List<Customer> customers);
    }
}
