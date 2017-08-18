package com.mifos.apache.fineract.ui.online.customers.customerprofile.editcustomerprofilebottomsheet;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.MifosBaseBottomSheetDialogFragment;
import com.mifos.apache.fineract.ui.base.Toaster;
import com.mifos.apache.fineract.ui.refreshcallback.RefreshProfileImage;
import com.mifos.apache.fineract.utils.CheckSelfPermissionAndRequest;
import com.mifos.apache.fineract.utils.ConstantKeys;
import com.mifos.apache.fineract.utils.FileUtils;
import com.mifos.apache.fineract.utils.ImageLoaderUtils;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Rajan Maurya
 *         On 06/08/17.
 */
public class EditCustomerProfileBottomSheet extends MifosBaseBottomSheetDialogFragment
        implements EditCustomerProfileContract.View, View.OnClickListener {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_PHOTO_FROM_GALLERY = 2;

    @BindView(R.id.iv_customer_picture)
    CircleImageView ivCustomerPicture;

    @BindView(R.id.tv_header)
    TextView tvHeader;

    @BindView(R.id.tv_image_name)
    TextView tvImageName;

    @BindView(R.id.btn_choose_select_photo)
    Button btnChooseSelectPhoto;

    @BindView(R.id.ll_camera)
    LinearLayout llCamera;

    @BindView(R.id.ll_gallery)
    LinearLayout llGallery;

    @BindView(R.id.ll_delete)
    LinearLayout llDelete;

    @BindView(R.id.ll_edit_action_form)
    LinearLayout llEditActionForm;

    @BindView(R.id.ll_edit_actions)
    LinearLayout llEditActions;

    @BindView(R.id.tv_select_file)
    TextView tvSelectFile;

    @BindView(R.id.tv_image_size)
    TextView tvImageSize;

    @BindView(R.id.btn_upload_photo)
    Button btnUploadPhoto;

    @Inject
    EditCustomerProfilePresenter editCustomerProfilePresenter;

    View rootView;

    private BottomSheetBehavior behavior;
    private EditAction editAction;
    private String customerIdentifier;
    private File file;
    private RefreshProfileImage refreshProfileImage;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        rootView = View.inflate(getContext(),
                R.layout.bottom_sheet_edit_customer_profile, null);
        dialog.setContentView(rootView);
        behavior = BottomSheetBehavior.from((View) rootView.getParent());
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        editCustomerProfilePresenter.attachView(this);
        ButterKnife.bind(this, rootView);

        showUserInterface();

        return dialog;
    }

    @Override
    public void showUserInterface() {
        llCamera.setOnClickListener(this);
        llGallery.setOnClickListener(this);
        llDelete.setOnClickListener(this);

        ImageLoaderUtils imageLoaderUtils = new ImageLoaderUtils(getActivity());
        imageLoaderUtils.loadImage(imageLoaderUtils.buildCustomerPortraitImageUrl(
                customerIdentifier), ivCustomerPicture, R.drawable.ic_account_circle_black_24dp);
    }

    @Override
    public void setCustomerIdentifier(String customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
    }

    @Override
    public void setRefreshProfileImage(RefreshProfileImage refreshProfileImage) {
        this.refreshProfileImage = refreshProfileImage;
    }

    @Override
    public void checkWriteExternalStoragePermission() {
        if (CheckSelfPermissionAndRequest.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            openCamera();
        } else {
            requestWriteExternalStoragePermission();
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
    public void showImageSizeExceededOrNot() {
        int fileSize = Integer.parseInt(String.valueOf(file.length() / 1024));
        if (fileSize > 512) {
            btnUploadPhoto.setEnabled(false);
            tvImageSize.setVisibility(View.VISIBLE);
        } else {
            btnUploadPhoto.setEnabled(true);
            tvImageSize.setVisibility(View.GONE);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void requestWriteExternalStoragePermission() {
        CheckSelfPermissionAndRequest.requestPermission(
                (MifosBaseActivity) getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ConstantKeys.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE,
                getResources().getString(
                        R.string.dialog_message_write_permission_denied_prompt),
                getResources().getString(R.string.dialog_message_write_permission_never_ask_again),
                ConstantKeys.PERMISSIONS_WRITE_EXTERNAL_STORAGE_STATUS);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void requestReadExternalStoragePermission() {
        CheckSelfPermissionAndRequest.requestPermission(
                (MifosBaseActivity) getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE,
                ConstantKeys.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE,
                getResources().getString(
                        R.string.dialog_message_read_permission_denied_prompt),
                getResources().getString(R.string.dialog_message_read_permission_never_ask_again),
                ConstantKeys.PERMISSIONS_READ_EXTERNAL_STORAGE_STATUS);
    }

    @Override
    public void showPortraitUploadedSuccessfully() {
        Toast.makeText(getActivity(), R.string.portrait_uploaded_successfully,
                Toast.LENGTH_SHORT).show();
        refreshProfileImage.refreshUI();
        dismiss();
    }

    @Override
    public void showPortraitDeletedSuccessfully() {
        Toast.makeText(getActivity(), R.string.portrait_deleted_successfully,
                Toast.LENGTH_SHORT).show();
        refreshProfileImage.refreshUI();
        dismiss();
    }

    @Override
    public void showProgressDialog(String message) {
        showMifosProgressDialog(message);
    }

    @Override
    public void hideProgressDialog() {
        hideMifosProgressDialog();
    }

    @Override
    public void showMessage(String message) {
        Toaster.show(rootView, message);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case ConstantKeys.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toaster.show(rootView, getString(R.string.permission_denied_write));
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

    @OnClick(R.id.btn_cancel)
    void onCancel() {
        dismiss();
    }

    @OnClick(R.id.btn_upload_photo)
    void uploadPhoto() {
        if (editAction == EditAction.CAMERA || editAction == EditAction.GALLERY) {
            editCustomerProfilePresenter.uploadCustomerPortrait(customerIdentifier, file);
        } else if (editAction == EditAction.DELETE) {
            editCustomerProfilePresenter.deleteCustomerPortrait(customerIdentifier);
        }
    }

    @OnClick(R.id.btn_choose_select_photo)
    void chooseSelectDeletePhoto() {
        switch (editAction) {
            case CAMERA:
                checkWriteExternalStoragePermission();
                break;
            case GALLERY:
                checkReadExternalStoragePermission();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            file = FileUtils.createFile(getString(R.string.fineract), customerIdentifier + ".png");
            FileUtils.saveBitmap(imageBitmap, file);
            tvImageName.setText(file.getName());
            ivCustomerPicture.setImageBitmap(imageBitmap);

            showImageSizeExceededOrNot();

        } else if (requestCode == REQUEST_PHOTO_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            file = new File(FileUtils.getPathReal(getActivity(), uri));
            tvImageName.setText(file.getName());
            Glide.with(getActivity()).load(uri).asBitmap().into(ivCustomerPicture);

            showImageSizeExceededOrNot();
        }
    }

    @Override
    public void onClick(View v) {
        llEditActions.setVisibility(View.GONE);
        llEditActionForm.setVisibility(View.VISIBLE);
        switch (v.getId()) {
            case R.id.ll_gallery:
                editAction = EditAction.GALLERY;
                tvHeader.setText(R.string.gallery);
                btnChooseSelectPhoto.setText(R.string.choose_file);
                break;
            case R.id.ll_camera:
                editAction = EditAction.CAMERA;
                tvHeader.setText(R.string.camera);
                btnChooseSelectPhoto.setText(R.string.take_photo);
                btnChooseSelectPhoto.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_camera_enhance_black_24dp, 0, 0, 0);
                break;
            case R.id.ll_delete:
                editAction = EditAction.DELETE;
                tvHeader.setText(R.string.delete);
                btnChooseSelectPhoto.setVisibility(View.GONE);
                tvSelectFile.setVisibility(View.GONE);
                tvImageName.setText(R.string.are_sure_want_remove_portrait);
                btnUploadPhoto.setText(R.string.delete);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideMifosProgressDialog();
        editCustomerProfilePresenter.detachView();
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
