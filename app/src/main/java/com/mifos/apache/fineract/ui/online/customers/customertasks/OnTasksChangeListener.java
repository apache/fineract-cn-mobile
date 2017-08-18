package com.mifos.apache.fineract.ui.online.customers.customertasks;

import com.mifos.apache.fineract.data.models.customer.Customer;

/**
 * @author Rajan Maurya
 *         On 18/08/17.
 */
public interface OnTasksChangeListener {

    void changeCustomerStatus(Customer.State state);
}
