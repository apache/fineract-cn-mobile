package org.apache.fineract.ui.online.customers.createcustomer;

import org.apache.fineract.data.models.customer.Address;
import org.apache.fineract.data.models.customer.ContactDetail;
import org.apache.fineract.data.models.customer.DateOfBirth;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 17/07/17.
 */
public interface OnNavigationBarListener {

    interface CustomerDetails {
        void setCustomerDetails(String identifier, String firstName, String surname,
                String middleName, DateOfBirth dateOfBirth, boolean isMember);
    }

    interface CustomerAddress {
        void setAddress(Address address);
    }

    interface CustomerContactDetails {
        void setContactDetails(List<ContactDetail> contactDetails);
    }
}
