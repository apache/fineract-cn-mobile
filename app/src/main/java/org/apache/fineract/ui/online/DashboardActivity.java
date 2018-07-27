package org.apache.fineract.ui.online;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import org.apache.fineract.R;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.jobs.StartSyncJob;
import org.apache.fineract.ui.base.FineractBaseActivity;
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
 *         On 19/06/17.
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

        drawerLayout.closeDrawer(Gravity.START);
        setTitle(item.getTitle());
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                preferencesHelper.clear();
                                Intent intent = new Intent(DashboardActivity.this,
                                        LauncherActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                .setNegativeButton(getString(R.string.dialog_action_cancel))
                .createMaterialDialog()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
