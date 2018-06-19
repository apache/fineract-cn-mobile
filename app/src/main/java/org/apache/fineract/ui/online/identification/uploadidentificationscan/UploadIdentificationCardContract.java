package org.apache.fineract.ui.online.identification.uploadidentificationscan;

import org.apache.fineract.ui.base.MvpView;

import java.io.File;

/**
 * @author Rajan Maurya
 *         On 01/08/17.
 */
public interface UploadIdentificationCardContract {

    interface View extends MvpView {

        void showUserInterface();

        void checkCameraPermission();

        void openCamera();

        void requestPermission();

        void showScanUploadedSuccessfully();

        void showProgressDialog();

        void hideProgressDialog();

        void showError(String message);

        boolean validateIdentifier();

        boolean validateDescription();

        boolean validateSelectFile();
    }

    interface Presenter {

        void uploadIdentificationCardScan(String customerIdentifier,
                String identificationNumber, String scanIdentifier, String description,
                File file);
    }
}
