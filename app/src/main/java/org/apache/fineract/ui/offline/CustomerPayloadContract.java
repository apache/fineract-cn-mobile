package org.apache.fineract.ui.offline;

import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.ui.base.MvpView;

import java.util.List;

public interface CustomerPayloadContract {
    interface View extends MvpView {

        void showCustomers(List<Customer> customers);

        void showEmptyCustomers();

    }

    interface Presenter {

        void fetchCustomers();

    }
}
