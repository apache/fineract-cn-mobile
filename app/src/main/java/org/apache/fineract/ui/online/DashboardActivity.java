package org.apache.fineract.ui.online;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.apache.fineract.R;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.jobs.StartSyncJob;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.Toaster;
import org.apache.fineract.ui.offline.CustomerPayloadFragment;
import org.apache.fineract.ui.online.accounting.ledgers.LedgerFragment;
import org.apache.fineract.ui.online.accounting.accounts.AccountsFragment;
import org.apache.fineract.ui.online.customers.customerlist.CustomersFragment;
import org.apache.fineract.ui.online.dashboard.DashboardFragment;
import org.apache.fineract.ui.online.launcher.LauncherActivity;
import org.apache.fineract.ui.online.roles.roleslist.RolesFragment;
import org.apache.fineract.ui.online.teller.TellerFragment;
import org.apache.fineract.ui.product.ProductFragment;
import org.apache.fineract.utils.MaterialDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 * On 19/06/17.
 */
public class DashboardActivity extends FineractBaseActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public static final String LOG_TAG = DashboardActivity.class.getSimpleName();

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Inject
    PreferencesHelper preferencesHelper;

    private boolean isBackPressedOnce = false;

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
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        if (item.getItemId() != R.id.item_logout) {
            setTitle(item.getTitle());
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setNavigationViewSelectedItem(int id) {
        navigationView.setCheckedItem(id);
    }
}
