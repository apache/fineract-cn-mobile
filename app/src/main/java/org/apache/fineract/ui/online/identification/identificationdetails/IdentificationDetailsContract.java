package org.apache.fineract.ui.online.identification.identificationdetails;

import org.apache.fineract.data.models.customer.identification.ScanCard;
import org.apache.fineract.ui.base.MvpView;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 01/08/17.
 */
public interface IdentificationDetailsContract {

    interface View extends MvpView {

        void showUserInterface();

        void initializeRecyclerView();

        void showScanCards(List<ScanCard> scanCards);

        void showRecyclerView(boolean status);

        void showScansStatus(String message);

        void showIdentifierDeletedSuccessfully();

        void showIdentificationCardScanDeletedSuccessfully(ScanCard scanCard);

        void showProgressDialog();

        void hideProgressDialog();

        void showError(String message);
    }

    interface Presenter {

        void fetchIdentificationScanCards(String customerIdentifier, String identificationNumber);

        void deleteIdentificationCard(String customerIdentifier, String identificationNumber);

        void deleteIdentificationCardScan(String customerIdentifier, String identificationNumber,
                ScanCard scanCard);
    }
}
