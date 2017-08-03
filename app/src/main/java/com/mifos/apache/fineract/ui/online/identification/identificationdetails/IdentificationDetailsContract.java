package com.mifos.apache.fineract.ui.online.identification.identificationdetails;

import com.mifos.apache.fineract.data.models.customer.identification.ScanCard;
import com.mifos.apache.fineract.ui.base.MvpView;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 01/08/17.
 */
public interface IdentificationDetailsContract {

    interface View extends MvpView {

        void showUserInterface();

        void showScanCards(List<ScanCard> scanCards);

        void showRecyclerView(boolean status);

        void showScansStatus(String message);

        void showIdentifierDeletedSuccessfully();

        void showProgressDialog();

        void hideProgressDialog();

        void showError(String message);
    }

    interface Presenter {

        void fetchIdentificationScanCards(String customerIdentifier, String identificationNumber);

        void deleteIdentificationCard(String customerIdentifier, String identificationNumber);
    }
}
