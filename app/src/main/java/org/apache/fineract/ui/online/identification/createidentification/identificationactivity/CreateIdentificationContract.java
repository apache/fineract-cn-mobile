package org.apache.fineract.ui.online.identification.createidentification.identificationactivity;

import org.apache.fineract.data.models.customer.identification.Identification;
import org.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 31/07/17.
 */
public interface CreateIdentificationContract {

    interface View extends MvpView {

        void identificationCreatedSuccessfully();

        void identificationCardEditedSuccessfully();

        void showProgressbar();

        void hideProgressbar();

        void showError(String message);
    }

    interface Presenter {

        void createIdentification(String identifier, Identification identification);

        void updateIdentificationCard(String identifier, String identificationNumber,
                Identification identification);
    }
}
