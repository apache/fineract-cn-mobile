package org.apache.fineract.ui.online.identification.identificationlist;

import org.apache.fineract.data.models.customer.identification.Identification;
import org.apache.fineract.ui.base.MvpView;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 31/07/17.
 */
public interface IdentificationsContract {

    interface View extends MvpView {

        void showUserInterface();

        void showIdentification(List<Identification> identifications);

        void showProgressbar();

        void hideProgressbar();

        void showRecyclerView(boolean status);

        void showEmptyIdentifications();

        void showMessage(String message);

        void searchIdentificationList(Identification identification);
    }

    interface Presenter {

        void fetchIdentifications(String customerIdentifier);

        void searchIdentifications(String identifier, String number);
    }
}
