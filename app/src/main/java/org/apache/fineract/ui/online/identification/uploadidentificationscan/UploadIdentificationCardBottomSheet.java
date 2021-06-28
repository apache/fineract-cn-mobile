package org.apache.fineract.ui.online.identification.uploadidentificationscan;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.fineract.R;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseBottomSheetDialogFragment;
import org.apache.fineract.ui.base.Toaster;
import org.apache.fineract.utils.CheckSelfPermissionAndRequest;
import org.apache.fineract.utils.ConstantKeys;
import org.apache.fineract.utils.FileUtils;
import org.apache.fineract.utils.ValidateIdentifierUtil;
import org.apache.fineract.utils.ValidationUtil;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

/**
 * @author Rajan Maurya
 * On 01/08/17.
 */
public class UploadIdentificationCardBottomSheet extends FineractBaseBottomSheetDialogFragment
        implements UploadIdentificationCardContract.View, TextWatcher {

    public static final String LOG_TAG = UploadIdentificationCardBottomSheet.class.getSimpleName();

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_PHOTO_FROM_GALLERY = 2;

    @BindView(R.id.et_identifier)
    EditText etIdentifier;

    @BindView(R.id.et_description)
    EditText etDescription;

    @BindView(R.id.et_selected_file)
    EditText etSelectFile;

    @BindView(R.id.til_identifier)
    TextInputLayout tilIdentifier;

    @BindView(R.id.til_description)
    TextInputLayout tilDescription;

    @BindView(R.id.til_selected_file)
    TextInputLayout tilSelectedFile;

    @Inject
    UploadIdentificationCardPresenter uploadIdentificationCardPresenter;

    private Compressor imageCompressor;

    View rootView;

    private BottomSheetBehavior behavior;
    private File cameraImage, compressedImage;
    private String customerIdentifier;
    private String identificationNumber;

    private AddScanIdentificationListener addScanIdentificationListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        rootView = View.inflate(getContext(),
                R.layout.bottom_sheet_upload_identification_scan_card, null);
        dialog.setContentView(rootView);
        behavior = BottomSheetBehavior.from((View) rootView.getParent());
        ((FineractBaseActivity) getActivity()).getActivityComponent().inject(this);
        uploadIdentificationCardPresenter.attachView(this);
        imageCompressor = new Compressor(getActivity());
        ButterKnife.bind(this, rootView);

        showUserInterface();

        return dialog;
    }

    @Override
    public void showUserInterface() {
        etIdentifier.addTextChangedListener(this);
        etSelectFile.addTextChangedListener(this);
        etDescription.addTextChangedListener(this);
    }

    @OnClick(R.id.btn_upload_identification_card_scan)
    public void onUploadIdentificationCard() {
        if (validateIdentifier() && validateDescription() && validateSelectFile()) {

            uploadIdentificationCardPresenter.uploadIdentificationCardScan(customerIdentifier,
                    identificationNumber, etIdentifier.getText().toString().trim(),
                    etDescription.getText().toString().trim(), compressedImage);
        }
    }

    @OnClick(R.id.btn_cancel)
    void onCancel() {
        dismiss();
    }

    @OnClick(R.id.btn_take_photo)
    void browseDocument() {
        checkCameraPermission();
    }

    @OnClick(R.id.btn_browse_document)
    void takePhotoOfDocument() {
        checkReadExternalStoragePermission();
    }

    @Override
    public void checkCameraPermission() {
        if (CheckSelfPermissionAndRequest.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)) {
            openCamera();
        } else {
            requestPermission();
        }
    }

    @Override
    public void checkReadExternalStoragePermission() {
        if (CheckSelfPermissionAndRequest.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            viewGallery();
        } else {
            requestReadExternalStoragePermission();
        }
    }

    @Override
    public void openCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            cameraImage = new File(getActivity().getExternalCacheDir() +
                    File.separator + "scan.png");

            if (cameraImage.exists()) {
                cameraImage.delete();
            }

            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                    "org.apache.fineract.fileprovider", cameraImage);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void viewGallery() {
        Intent intentDocument = new Intent(Intent.ACTION_GET_CONTENT);
        intentDocument.setType("image/*");
        startActivityForResult(intentDocument, REQUEST_PHOTO_FROM_GALLERY);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            etSelectFile.setText(getString(R.string.scan_file));
            try {
                compressedImage = imageCompressor.compressToFile(cameraImage);
            } catch (IOException e) {
                Log.d(UploadIdentificationCardBottomSheet.class.getSimpleName(), e.toString());
            }
        } else if (requestCode == REQUEST_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            etSelectFile.setText(getString(R.string.scan_file));
            Uri uri = data.getData();
            File file = new File(FileUtils.getPathReal(getActivity(), uri));
            try {
                compressedImage = imageCompressor.compressToFile(file);
            } catch (IOException e) {
                Log.d(UploadIdentificationCardBottomSheet.class.getSimpleName(), e.toString());
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void requestPermission() {
        CheckSelfPermissionAndRequest.requestPermission(
                (FineractBaseActivity) getActivity(),
                Manifest.permission.CAMERA,
                ConstantKeys.PERMISSIONS_REQUEST_CAMERA,
                getResources().getString(
                        R.string.dialog_message_camera_permission_denied_prompt),
                getResources().getString(R.string.dialog_message_camera_permission_never_ask_again),
                ConstantKeys.PERMISSIONS_CAMERA_STATUS);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void requestReadExternalStoragePermission() {
        CheckSelfPermissionAndRequest.requestPermission(
                (FineractBaseActivity) getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE,
                ConstantKeys.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE,
                getResources().getString(
                        R.string.dialog_message_read_permission_denied_prompt),
                getResources().getString(R.string.dialog_message_read_permission_never_ask_again),
                ConstantKeys.PERMISSIONS_READ_EXTERNAL_STORAGE_STATUS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case ConstantKeys.PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toaster.show(rootView, getString(R.string.permission_denied_camera));
                }
                break;

            case ConstantKeys.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewGallery();
                } else {
                    Toaster.show(rootView, getString(R.string.permission_denied_read));
                }
                break;
        }
    }

    @Override
    public void showScanUploadedSuccessfully() {
        addScanIdentificationListener.updateScanUploadedIdentification();
        dismiss();
    }

    @Override
    public void showProgressDialog() {
        showMifosProgressDialog(getString(R.string.uploading_identification_scan_card));
    }

    @Override
    public void hideProgressDialog() {
        hideMifosProgressDialog();
    }

    @Override
    public void showNoInternetConnection() {
        Toaster.show(rootView, getString(R.string.no_internet_connection));
    }

    @Override
    public void showError(String message) {
        Toaster.show(rootView, message);
    }

    public void setIdentifierAndNumber(String identifier, String identificationNumber) {
        customerIdentifier = identifier;
        this.identificationNumber = identificationNumber;
    }

    public void setAddScanIdentificationListener(AddScanIdentificationListener listener) {
        addScanIdentificationListener = listener;
    }

    @Override
    public boolean validateIdentifier() {
        return ValidateIdentifierUtil.isValid(getActivity(),
                etIdentifier.getText().toString().trim(), tilIdentifier);
    }

    @Override
    public boolean validateDescription() {
        return ValidationUtil.isEmpty(getActivity(),
                etDescription.getText().toString().trim(), tilDescription);
    }

    @Override
    public boolean validateSelectFile() {
        return ValidationUtil.isEmpty(getActivity(),
                etSelectFile.getText().toString().trim(), tilSelectedFile);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if ((etIdentifier.getText().hashCode() == s.hashCode())) {
            validateIdentifier();
        } else if (etDescription.getText().hashCode() == s.hashCode()) {
            validateDescription();
        } else if (etSelectFile.getText().hashCode() == s.hashCode()) {
            validateSelectFile();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideMifosProgressDialog();
        uploadIdentificationCardPresenter.detachView();
    }

    @Override
    public void onStart() {
        super.onStart();
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
