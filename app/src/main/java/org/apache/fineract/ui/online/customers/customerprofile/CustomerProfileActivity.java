package org.apache.fineract.ui.online.customers.customerprofile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler;

import org.apache.fineract.R;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.Toaster;
import org.apache.fineract.ui.online.customers.customerprofile.editcustomerprofilebottomsheet
        .EditCustomerProfileBottomSheet;
import org.apache.fineract.ui.refreshcallback.RefreshProfileImage;
import org.apache.fineract.utils.CheckSelfPermissionAndRequest;
import org.apache.fineract.utils.ConstantKeys;
import org.apache.fineract.utils.ImageLoaderUtils;
import org.apache.fineract.utils.Utils;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 * On 06/08/17.
 */
public class CustomerProfileActivity extends FineractBaseActivity
        implements CustomerProfileContract.View, RefreshProfileImage {
    private SweetUIErrorHandler sweetUIErrorHandler;
    @BindView(R.id.iv_customer_picture)
    ImageView ivCustomerProfile;

    @BindView(R.id.layout_error)
    CoordinatorLayout errorView;

    private String customerIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        ButterKnife.bind(this);

        customerIdentifier = getIntent().getExtras().getString(ConstantKeys.CUSTOMER_IDENTIFIER);

        sweetUIErrorHandler = new SweetUIErrorHandler(this, findViewById(android.R.id.content));
        loadCustomerPortrait();

        showBackButton();
        setToolbarTitle(getString(R.string.customer_image));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer_profile, menu);
        Utils.setToolbarIconColor(this, menu, R.color.white);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_customer_profile_edit:
                EditCustomerProfileBottomSheet profileBottomSheet =
                        new EditCustomerProfileBottomSheet();
                profileBottomSheet.setCustomerIdentifier(customerIdentifier);
                profileBottomSheet.setRefreshProfileImage(this);
                profileBottomSheet.show(getSupportFragmentManager(),
                        getString(R.string.customer_image));
                return true;
            case R.id.menu_customer_profile_share:
                checkCameraPermission();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareImage() {
        Uri bitmapUri = getImageUri(this, getBitmapFromView(ivCustomerProfile));
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        startActivity(Intent.createChooser(shareIntent,
                getString(R.string.share_customer_profile)));
    }

    public static Bitmap getBitmapFromView(ImageView view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, customerIdentifier, null);
        return Uri.parse(path);
    }

    @Override
    public void checkCameraPermission() {
            if (CheckSelfPermissionAndRequest.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                shareImage();
            } else {
                requestPermission();
            }
        }

    @Override
    public void requestPermission() {
        CheckSelfPermissionAndRequest.requestPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ConstantKeys.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE,
                getResources().getString(
                        R.string.dialog_message_write_permission_for_share_denied_prompt),
                getResources().getString(
                        R.string.dialog_message_write_permission_for_share_never_ask_again),
                ConstantKeys.PERMISSIONS_WRITE_EXTERNAL_STORAGE_STATUS);
    }

    @Override
    public void loadCustomerPortrait() {
        ImageLoaderUtils imageLoaderUtils = new ImageLoaderUtils(this);
        imageLoaderUtils.loadImage(imageLoaderUtils.buildCustomerPortraitImageUrl(
                customerIdentifier), ivCustomerProfile, R.drawable.mifos_logo_new);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case ConstantKeys.PERMISSIONS_REQUEST_CAMERA: {switch (requestCode) {
                case ConstantKeys.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: {
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        shareImage();
                    } else {
                        Toaster.show(findViewById(android.R.id.content),
                                getString(R.string.permission_denied_write));
                    }
                }}
            }
        }
    }
    @Override
    public void refreshUI() {
        loadCustomerPortrait();
        sweetUIErrorHandler.hideSweetErrorLayoutUI(ivCustomerProfile, errorView);
    }

    @Override
    public void showNoInternetConnection() {
        sweetUIErrorHandler.showSweetNoInternetUI(ivCustomerProfile, errorView);
    }

    @Override
    public void showError(String message) {
        sweetUIErrorHandler.showSweetCustomErrorUI(message,
                R.drawable.ic_error_black_24dp, ivCustomerProfile, errorView);
    }
}
