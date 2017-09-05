package org.apache.fineract.ui.online.customers.customertasks;

import org.apache.fineract.data.models.customer.Customer;

/**
 * @author Rajan Maurya
 *         On 18/08/17.
 */
public interface OnTasksChangeListener {

    void changeCustomerStatus(Customer.State state);
}
