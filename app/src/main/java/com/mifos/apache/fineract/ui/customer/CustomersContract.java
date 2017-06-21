package com.mifos.apache.fineract.ui.customer;

import com.mifos.apache.fineract.data.models.customer.CustomerPage;
import com.mifos.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 20/06/17.
 */
public interface CustomersContract {

    interface View extends MvpView {

        void showUserInterface();

        void showCustomers(CustomerPage customerPage);

        void showMoreCustomers(CustomerPage customerPage);

        void showProgressbar();

        void hideProgressbar();

        void showError(String errorMessage);
    }

    interface Presenter {

        void fetchCustomers(Integer pageIndex, Integer size);
    }
}
