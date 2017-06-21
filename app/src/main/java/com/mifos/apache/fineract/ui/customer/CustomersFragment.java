package com.mifos.apache.fineract.ui.customer;

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
import com.mifos.apache.fineract.data.models.customer.CustomerPage;
import com.mifos.apache.fineract.ui.adapters.CustomerAdapter;
import com.mifos.apache.fineract.ui.base.EndlessRecyclerViewScrollListener;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 20/06/17.
 */
public class CustomersFragment extends MifosBaseFragment implements CustomersContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_customers)
    RecyclerView rvCustomers;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    @BindView(R.id.iv_status)
    ImageView ivStatus;

    @BindView(R.id.tv_status)
    TextView tvStatus;

    View rootView;

    @Inject
    CustomersPresenter customerPresenter;

    @Inject
    CustomerAdapter customerAdapter;

    public static CustomersFragment newInstance() {
        CustomersFragment fragment = new CustomersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customer_list, container, false);
        ButterKnife.bind(this, rootView);
        customerPresenter.attachView(this);
        setToolbarTitle(getString(R.string.customers));

        showUserInterface();

        customerPresenter.fetchCustomers(0, 50);

        return rootView;
    }

    @Override
    public void showUserInterface() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCustomers.setLayoutManager(layoutManager);
        rvCustomers.setHasFixedSize(true);
        rvCustomers.setAdapter(customerAdapter);
        swipeRefreshLayout.setColorSchemeColors(getActivity()
                .getResources().getIntArray(R.array.swipeRefreshColors));
        swipeRefreshLayout.setOnRefreshListener(this);

        rvCustomers.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // logic for load more
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void showCustomers(CustomerPage customerPage) {
        customerAdapter.setCustomers(customerPage.getCustomers());
    }

    @Override
    public void showMoreCustomers(CustomerPage customerPage) {

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
    public void showError(String errorMessage) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        customerPresenter.detachView();
    }
}
