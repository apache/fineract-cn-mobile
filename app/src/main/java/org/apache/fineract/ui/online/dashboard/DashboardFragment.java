package org.apache.fineract.ui.online.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.fineract.R;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.ui.offline.CustomerPayloadFragment;
import org.apache.fineract.ui.online.DashboardActivity;
import org.apache.fineract.ui.online.accounting.accounts.AccountsFragment;
import org.apache.fineract.ui.online.accounting.ledgers.LedgerFragment;
import org.apache.fineract.ui.online.customers.createcustomer.CustomerAction;
import org.apache.fineract.ui.online.customers.createcustomer.customeractivity
        .CreateCustomerActivity;
import org.apache.fineract.ui.online.customers.customerlist.CustomersFragment;
import org.apache.fineract.ui.online.roles.roleslist.RolesFragment;
import org.apache.fineract.ui.online.teller.TellerFragment;
import org.apache.fineract.ui.product.ProductFragment;
import org.apache.fineract.utils.ConstantKeys;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 * On 27/07/17.
 */
public class DashboardFragment extends FineractBaseFragment {

    View rootView;

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, rootView);
        setToolbarTitle(getString(R.string.dashboard));
        ((DashboardActivity) getActivity()).setNavigationViewSelectedItem(R.id.item_dashboard);
        return rootView;
    }

    @OnClick(R.id.btn_view_customer)
    void viewCustomer() {
        ((DashboardActivity) getActivity()).setNavigationViewSelectedItem(R.id.item_customer);
        ((DashboardActivity) getActivity()).replaceFragment(CustomersFragment.newInstance(),
                true,
                R.id.container);
    }

    @OnClick(R.id.btn_create_customer)
    void createCustomer() {
        Intent intent = new Intent(getActivity(), CreateCustomerActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_ACTION, CustomerAction.CREATE);
        startActivity(intent);
    }

    @OnClick(R.id.di_tellers)
    void navigateToTeller() {
        ((DashboardActivity) getActivity()).replaceFragment(
                TellerFragment.Companion.newInstance(),
                true,
                R.id.container);
    }

    @OnClick(R.id.di_ledger)
    void navigateToLedger() {
        ((DashboardActivity) getActivity()).replaceFragment(
                LedgerFragment.Companion.newInstance(),
                true,
                R.id.container);
    }

    @OnClick(R.id.di_customer_payload)
    void navigateToCustomerPayloads() {
        ((DashboardActivity) getActivity()).replaceFragment(
                CustomerPayloadFragment.newInstance(),
                true,
                R.id.container);
    }

    @OnClick(R.id.di_products)
    void navigateToProducts() {
        ((DashboardActivity) getActivity()).replaceFragment(
                ProductFragment.Companion.newInstance(),
                true,
                R.id.container);
    }

    @OnClick(R.id.di_roles_permission)
    void navigateToRoles() {
        ((DashboardActivity) getActivity()).replaceFragment(
                RolesFragment.newInstance(),
                true,
                R.id.container);
    }

    @OnClick(R.id.di_accounts)
    void navigateToAccounts() {
        ((DashboardActivity) getActivity()).replaceFragment(
                AccountsFragment.Companion.newInstance(),
                true,
                R.id.container);
    }

}
