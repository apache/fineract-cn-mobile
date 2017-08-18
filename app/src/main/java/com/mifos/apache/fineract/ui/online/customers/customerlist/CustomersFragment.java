package com.mifos.apache.fineract.ui.online.customers.customerlist;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.customer.Customer;
import com.mifos.apache.fineract.ui.adapters.CustomerAdapter;
import com.mifos.apache.fineract.ui.base.EndlessRecyclerViewScrollListener;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.mifos.apache.fineract.ui.base.OnItemClickListener;
import com.mifos.apache.fineract.ui.base.Toaster;
import com.mifos.apache.fineract.ui.online.customers.createcustomer.customeractivity
        .CreateCustomerActivity;
import com.mifos.apache.fineract.ui.online.customers.customerdetails.CustomerDetailsActivity;
import com.mifos.apache.fineract.utils.ConstantKeys;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 20/06/17.
 */
public class CustomersFragment extends MifosBaseFragment implements CustomersContract.View,
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    private static final Integer CUSTOMER_STATUS = 1;

    @BindView(R.id.rv_customers)
    RecyclerView rvCustomers;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    @BindView(R.id.iv_retry)
    ImageView ivRetry;

    @BindView(R.id.tv_error)
    TextView tvStatus;

    View rootView;

    @Inject
    CustomersPresenter customerPresenter;

    @Inject
    CustomerAdapter customerAdapter;

    private List<Customer> customers;
    private Integer detailsCustomerPosition;

    public static CustomersFragment newInstance() {
        CustomersFragment fragment = new CustomersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customers = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customer_list, container, false);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        customerPresenter.attachView(this);
        setToolbarTitle(getString(R.string.customers));

        showUserInterface();

        customerPresenter.fetchCustomers(0, false);

        return rootView;
    }

    @Override
    public void onRefresh() {
        customerPresenter.fetchCustomers(0, false);
    }

    @OnClick(R.id.iv_retry)
    void onRetry() {
        customerPresenter.fetchCustomers(0, false);
    }

    @Override
    public void showUserInterface() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCustomers.setLayoutManager(layoutManager);
        rvCustomers.setHasFixedSize(true);
        customerAdapter.setOnItemClickListener(this);
        rvCustomers.setAdapter(customerAdapter);
        swipeRefreshLayout.setColorSchemeColors(getActivity()
                .getResources().getIntArray(R.array.swipeRefreshColors));
        swipeRefreshLayout.setOnRefreshListener(this);

        rvCustomers.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customerPresenter.fetchCustomers(page, true);
            }
        });
    }

    @OnClick(R.id.fab_add_customer)
    void addCustomer() {
        Intent intent = new Intent(getActivity(), CreateCustomerActivity.class);
        startActivity(intent);
    }

    @Override
    public void showCustomers(List<Customer> customers) {
        showRecyclerView(true);
        this.customers = customers;
        customerAdapter.setCustomers(customers);
    }

    @Override
    public void showMoreCustomers(List<Customer> customers) {
        showRecyclerView(true);
        this.customers.addAll(customers);
        customerAdapter.setMoreCustomers(customers);
    }

    @Override
    public void showEmptyCustomers(String message) {
        showRecyclerView(false);
        tvStatus.setText(getString(R.string.empty_customer_list));
        showMessage(getString(R.string.empty_customer_list));
    }

    @Override
    public void showRecyclerView(boolean status) {
        if (status) {
            rvCustomers.setVisibility(View.VISIBLE);
            rlError.setVisibility(View.GONE);
        } else {
            rvCustomers.setVisibility(View.GONE);
            rlError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showProgressbar() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressbar() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String errorMessage) {
        Toaster.show(rootView, errorMessage);
    }

    @Override
    public void showError() {
        showRecyclerView(false);
        tvStatus.setText(getString(R.string.error_loading_customers));
        showMessage(getString(R.string.error_loading_customers));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        customerPresenter.detachView();
    }

    @Override
    public void onItemClick(View childView, int position) {
        detailsCustomerPosition = position;
        Intent customerDetailsIntent = new Intent(getActivity(), CustomerDetailsActivity.class);
        customerDetailsIntent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER,
                customers.get(position).getIdentifier());
        startActivityForResult(customerDetailsIntent, CUSTOMER_STATUS);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == CUSTOMER_STATUS) && (resultCode == RESULT_OK)) {
            Customer.State state = (Customer.State) data.getSerializableExtra(
                    ConstantKeys.CUSTOMER_STATUS);
            customers.get(detailsCustomerPosition).setCurrentState(state);
            customerAdapter.notifyItemChanged(detailsCustomerPosition);
        }
    }
}
