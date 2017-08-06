package com.mifos.apache.fineract.ui.online;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.online.customer.customerlist.CustomersFragment;
import com.mifos.apache.fineract.ui.online.dashboard.DashboardFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 19/06/17.
 */
public class DashboardActivity extends MifosBaseActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public static final String LOG_TAG = DashboardActivity.class.getSimpleName();

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        replaceFragment(DashboardFragment.newInstance(), false, R.id.container);

        setupNavigationBar();
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
                if (slideOffset != 0)
                    hideKeyboard(drawerLayout);
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
            case R.id.item_customer:
                replaceFragment(CustomersFragment.newInstance(), true, R.id.container);
                break;
        }
        drawerLayout.closeDrawer(Gravity.START);
        setTitle(item.getTitle());
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
