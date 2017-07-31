package com.mifos.apache.fineract.ui.online.identification.createidentification
        .identificationactivity;

import com.mifos.apache.fineract.data.models.customer.identification.Identification;
import com.mifos.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 31/07/17.
 */
public interface CreateIdentificationContract {

    interface View extends MvpView {

        void identificationCreatedSuccessfully();

        void showProgressbar();

        void hideProgressbar();

        void showError(String message);
    }

    interface Presenter {

        void createIdentification(String identifier, Identification identification);
    }
}
