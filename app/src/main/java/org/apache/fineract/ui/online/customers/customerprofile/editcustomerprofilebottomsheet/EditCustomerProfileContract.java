package org.apache.fineract.ui.online.customers.customerprofile.editcustomerprofilebottomsheet;

import androidx.annotation.NonNull;

import org.apache.fineract.ui.base.MvpView;
import org.apache.fineract.ui.refreshcallback.RefreshProfileImage;

import java.io.File;

/**
 * @author Rajan Maurya
 *         On 06/08/17.
 */
public interface EditCustomerProfileContract {

    interface View extends MvpView {

        void requestedPermissionResult(int requestCode, @NonNull String[] permissions,
                                       @NonNull int[] grantResults);

        void showUserInterface();

        void setCustomerIdentifier(String customerIdentifier);

        void setRefreshProfileImage(RefreshProfileImage refreshProfileImage);

        void checkWriteExternalStoragePermission();

        void checkReadExternalStoragePermission();

        void openCamera();

        void viewGallery();

        void showImageSizeExceededOrNot();

        void requestWriteExternalStorageAndCameraPermission();

        void requestReadExternalStoragePermission();

        void showPortraitUploadedSuccessfully();

        void showPortraitDeletedSuccessfully();

        void showProgressDialog(String message);

        void hideProgressDialog();

        void showMessage(String message);
    }

    interface Presenter {

        void uploadCustomerPortrait(String customerIdentifier, File file);

        void deleteCustomerPortrait(String customerIdentifier);
    }
}
