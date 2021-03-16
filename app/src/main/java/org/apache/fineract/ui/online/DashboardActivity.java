package org.apache.fineract.ui.online;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.apache.fineract.R;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.jobs.StartSyncJob;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.Toaster;
import org.apache.fineract.ui.offline.CustomerPayloadFragment;
import org.apache.fineract.ui.online.accounting.accounts.AccountsFragment;
import org.apache.fineract.ui.online.accounting.ledgers.LedgerFragment;
import org.apache.fineract.ui.online.customers.customerlist.CustomersFragment;
import org.apache.fineract.ui.online.dashboard.DashboardFragment;
import org.apache.fineract.ui.online.groups.grouplist.GroupListFragment;
import org.apache.fineract.ui.online.launcher.LauncherActivity;
import org.apache.fineract.ui.online.roles.roleslist.RolesFragment;
import org.apache.fineract.ui.online.teller.TellerFragment;
import org.apache.fineract.ui.product.ProductFragment;
import org.apache.fineract.utils.CheckSelfPermissionAndRequest;
import org.apache.fineract.utils.ConstantKeys;
import org.apache.fineract.utils.MaterialDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya On 19/06/17.
 */
public class DashboardActivity extends FineractBaseActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public static final String LOG_TAG = DashboardActivity.class.getSimpleName();
    public static final int REQUEST_PERMISSIONS = 0;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_PHOTO_FROM_GALLERY = 2;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Inject
    PreferencesHelper preferencesHelper;

    private boolean isBackPressedOnce = false;
    private ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        replaceFragment(DashboardFragment.newInstance(), false, R.id.container);

        setupNavigationBar();

        if (preferencesHelper.isFirstTime()) {
            StartSyncJob.scheduleItNow();
            preferencesHelper.setFetching(false);
        }
    }

    public void setupNavigationBar() {
        navigationView.setNavigationItemSelectedListener(this);

        userImage = navigationView.getHeaderView(0).findViewById(R.id.nav_user_image);
        loadUserImage();
        userImage.setOnClickListener(v -> selectImage());

        // setup drawer layout and sync to toolbar
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset != 0) {
                    hideKeyboard(drawerLayout);
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        clearFragmentBackStack();

        switch (item.getItemId()) {
            case R.id.item_dashboard:
                replaceFragment(DashboardFragment.newInstance(), true, R.id.container);
                break;
            case R.id.item_roles:
                replaceFragment(RolesFragment.newInstance(), true, R.id.container);
                break;
            case R.id.item_customer:
                replaceFragment(CustomersFragment.newInstance(), true, R.id.container);
                break;
            case R.id.item_customer_payload:
                replaceFragment(CustomerPayloadFragment.newInstance(), true,
                        R.id.container);
                break;
            case R.id.item_product:
                replaceFragment(ProductFragment.Companion.newInstance(), true,
                        R.id.container);
                break;
            case R.id.item_logout:
                logout();
                break;
            case R.id.item_ledger:
                replaceFragment(LedgerFragment.Companion.newInstance(), true, R.id.container);
                break;
            case R.id.item_accounts:
                replaceFragment(AccountsFragment.Companion.newInstance(), true, R.id.container);
                break;
            case R.id.item_teller:
                replaceFragment(TellerFragment.Companion.newInstance(), true, R.id.container);
                break;
            case R.id.item_groups:
                replaceFragment(GroupListFragment.Companion.newInstance(), true, R.id.container);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        setTitle(item.getTitle());
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        if (isBackPressedOnce) {
            super.onBackPressed();
            return;
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof DashboardFragment) {
            this.isBackPressedOnce = true;
            Toaster.show(drawerLayout, R.string.please_click_back_again_to_exit,
                    Snackbar.LENGTH_SHORT);
            new Handler().postDelayed(
                    () -> isBackPressedOnce = false
                    , 2000);
        } else {
            super.onBackPressed();
        }
    }

    public void logout() {
        new MaterialDialog.Builder()
                .init(this)
                .setTitle(getString(R.string.dialog_title_confirm_logout))
                .setMessage(getString(
                        R.string.dialog_message_confirmation_logout))
                .setPositiveButton(getString(R.string.dialog_action_logout),
                        (dialog, which) -> {
                            preferencesHelper.clear();
                            removeImage();
                            Intent intent = new Intent(DashboardActivity.this,
                                    LauncherActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(DashboardActivity.this,
                                    R.string.logged_out_successfully, Toast.LENGTH_SHORT)
                                    .show();

                        })
                .setNegativeButton(getString(R.string.dialog_action_cancel))
                .createMaterialDialog()
                .show();
    }

    public void setNavigationViewSelectedItem(int id) {
        navigationView.setCheckedItem(id);
    }

    private void selectImage() {
        if (CheckSelfPermissionAndRequest.checkMultiplePermissions(getThemedContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA) &&
                CheckSelfPermissionAndRequest.checkSelfPermission(getThemedContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
            showsUserImageOptions();
        } else {
            requestRWCPermissions();
        }
    }

    //RWC- read, write and camera
    private void requestRWCPermissions() {
        ActivityCompat.requestPermissions(DashboardActivity.this,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        ConstantKeys.PERMISSIONS_WRITE_EXTERNAL_STORAGE_STATUS,
                        ConstantKeys.PERMISSIONS_CAMERA_STATUS}, REQUEST_PERMISSIONS);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {

                case REQUEST_PERMISSIONS:
                    showsUserImageOptions();
                    break;

                case REQUEST_IMAGE_CAPTURE:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
                        File file = saveImage(capturedImage);
                        if (showImageSizeExceededOrNot(file)) {
                            userImage.setImageBitmap(capturedImage);
                            Toaster.show(navigationView,
                                    getString(R.string.user_image_update_success));
                        } else {
                            file.delete();
                            Toaster.show(navigationView, getString(R.string.user_image_size));
                        }
                    }
                    break;

                case REQUEST_PHOTO_FROM_GALLERY:
                    if (resultCode == RESULT_OK && data != null) {
                        try {
                            Uri imageUri = data.getData();
                            InputStream imageStream = getContentResolver().openInputStream(
                                    imageUri);
                            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                            File file = saveImage(selectedImage);
                            if (showImageSizeExceededOrNot(file)) {
                                userImage.setImageBitmap(selectedImage);
                                Toaster.show(navigationView,
                                        getString(R.string.user_image_update_success));
                            } else {
                                file.delete();
                                Toaster.show(navigationView,
                                        getString(R.string.user_image_size));
                            }
                        } catch (FileNotFoundException e) {
                            Logger.getLogger(e.getMessage());
                            Toaster.show(navigationView, getString(R.string.user_image_error));
                        }
                    }
                    break;
            }
        }
    }

    public boolean showImageSizeExceededOrNot(File file) {
        int fileSize = (int) (file.length() / 1024);
        return fileSize < 1024;
    }

    public void showsUserImageOptions() {
        CharSequence[] options =
                {getString(R.string.user_take_photo), getString(R.string.user_gallery),
                        getString(
                                R.string.user_remove_image), getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getThemedContext());
        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals(getString(R.string.user_take_photo))) {
                Intent takePicture = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);

            } else if (options[item].equals(getString(R.string.user_gallery))) {
                Intent intentDocument = new Intent(Intent.ACTION_GET_CONTENT);
                intentDocument.setType("image/*");
                startActivityForResult(intentDocument, REQUEST_PHOTO_FROM_GALLERY);

            } else if (options[item].equals(getString(R.string.user_remove_image))) {

                if (removeImage()) {
                    userImage.setImageDrawable(
                            getResources().getDrawable(R.drawable.mifos_logo_new));
                    Toaster.show(navigationView,
                            getString(R.string.user_removed_image_success));
                } else {
                    Toaster.show(navigationView, getString(R.string.user_image_not_exist));
                }

            } else if (options[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void loadUserImage() {
        ContextWrapper cw = new ContextWrapper(getThemedContext());
        File dir = cw.getDir(getString(R.string.fineract), MODE_PRIVATE);
        File file = new File(dir, preferencesHelper.getUserName() + ".png");
        if (file.exists()) {
            userImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        } else {
            userImage.setImageDrawable(getResources().getDrawable(R.drawable.mifos_logo_new));
        }
    }

    public boolean removeImage() {
        ContextWrapper cw = new ContextWrapper(getThemedContext());
        File dir = cw.getDir(getString(R.string.fineract), MODE_PRIVATE);
        File file = new File(dir, preferencesHelper.getUserName() + ".png");
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public File saveImage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getThemedContext());
        File dir = cw.getDir(getString(R.string.fineract), MODE_PRIVATE);
        File file = new File(dir, preferencesHelper.getUserName() + ".png");
        FileOutputStream fos = null;
        int ratio = 100;
        if (bitmapImage.getAllocationByteCount() < 1000) {
            ratio = 65;
        } else if (bitmapImage.getAllocationByteCount() <= 5000) {
            ratio = 10;
        } else if (bitmapImage.getAllocationByteCount() <= 7000) {
            ratio = 2;
        } else {
            ratio = 1;
        }
        try {
            fos = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, ratio, fos);
        } catch (Exception e) {
            Logger.getLogger(e.getMessage());
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                Logger.getLogger(e.getMessage());
            }
        }
        return file;
    }
}
