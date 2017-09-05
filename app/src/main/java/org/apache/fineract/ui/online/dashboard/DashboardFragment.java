package org.apache.fineract.ui.online.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.fineract.R;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.ui.online.customers.createcustomer.CustomerAction;
import org.apache.fineract.ui.online.customers.createcustomer.customeractivity
        .CreateCustomerActivity;
import org.apache.fineract.ui.online.customers.customerlist.CustomersFragment;
import org.apache.fineract.utils.ConstantKeys;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 27/07/17.
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
        return rootView;
    }

    @OnClick(R.id.btn_view_customer)
    void viewCustomer() {
        ((FineractBaseActivity) getActivity()).replaceFragment(CustomersFragment.newInstance(),
                true,
                R.id.container);
    }

    @OnClick(R.id.btn_create_customer)
    void createCustomer() {
        Intent intent = new Intent(getActivity(), CreateCustomerActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_ACTION, CustomerAction.CREATE);
        startActivity(intent);
    }
}
