package org.apache.fineract.ui.online.loanaccounts.loanapplication.loancosigner;

import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.ui.base.MvpView;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 24/07/17.
 */

public interface LoanCoSignerContract {

    interface View extends MvpView {

        void showCustomers(List<String> customer);

        void showProgressbar();

        void hideProgressbar();

        void showError(String message);
    }

    interface Presenter {

        void searchCustomer(String term);

        List<String> filterCustomerNames(List<Customer> customers);

        Boolean findCustomer(String customer, String [] customers);
    }
}
