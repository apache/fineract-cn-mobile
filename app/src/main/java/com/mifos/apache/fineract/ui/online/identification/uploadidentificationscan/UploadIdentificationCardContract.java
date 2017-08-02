package com.mifos.apache.fineract.ui.online.identification.uploadidentificationscan;

import android.graphics.Bitmap;

import com.mifos.apache.fineract.ui.base.MvpView;

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
                Bitmap bitmap);
    }
}
