package com.mifos.apache.fineract.ui.online.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.mifos.apache.fineract.ui.online.customers.createcustomer.customeractivity.CreateCustomerActivity;
import com.mifos.apache.fineract.ui.online.customers.customerlist.CustomersFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 27/07/17.
 */
public class DashboardFragment extends MifosBaseFragment {

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
        return rootView;
    }

    @OnClick(R.id.btn_view_customer)
    void viewCustomer() {
        ((MifosBaseActivity) getActivity()).replaceFragment(CustomersFragment.newInstance(), true,
                R.id.container);
    }

    @OnClick(R.id.btn_create_customer)
    void createCustomer() {
        Intent intent = new Intent(getActivity(), CreateCustomerActivity.class);
        startActivity(intent);
    }
}
