package org.apache.fineract.ui.online.identification.createidentification;


import org.apache.fineract.data.models.customer.identification.Identification;

/**
 * @author Rajan Maurya
 *         On 17/07/17.
 */
public interface OnNavigationBarListener {

    interface IdentificationCard {
        void setIdentificationDetails(Identification identificationDetails);
    }
}
