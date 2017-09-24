package org.apache.fineract.ui.online.customers.customeractivities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.Command;
import org.apache.fineract.ui.adapters.CustomerActivitiesAdapter;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.utils.ConstantKeys;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 15/08/17.
 */
public class CustomerActivitiesFragment extends FineractBaseFragment implements
        CustomerActivitiesContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_customer_activities)
    RecyclerView rvCustomerActivities;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.layout_error)
    View layoutError;

    @Inject
    CustomerActivitiesPresenter customerActivitiesPresenter;

    @Inject
    CustomerActivitiesAdapter customerActivitiesAdapter;

    View rootView;

    private String customerIdentifier;

    public static CustomerActivitiesFragment newInstance(String customerIdentifier) {
        CustomerActivitiesFragment fragment = new CustomerActivitiesFragment();
        Bundle args = new Bundle();
        args.putString(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((FineractBaseActivity) getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            customerIdentifier = getArguments().getString(ConstantKeys.CUSTOMER_IDENTIFIER);
        }
        setToolbarTitle(getString(R.string.activities));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customer_activities, container, false);
        ButterKnife.bind(this, rootView);
        initializeFineractUIErrorHandler(getContext(), rootView);
        customerActivitiesPresenter.attachView(this);

        showUserInterface();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        customerActivitiesPresenter.fetchCustomerCommands(customerIdentifier);
    }

    @Override
    public void onRefresh() {
        customerActivitiesPresenter.fetchCustomerCommands(customerIdentifier);
    }

    @OnClick(R.id.btn_try_again)
    void onRetry() {
        rvCustomerActivities.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
        customerActivitiesPresenter.fetchCustomerCommands(customerIdentifier);
    }

    @Override
    public void showUserInterface() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCustomerActivities.setLayoutManager(layoutManager);
        rvCustomerActivities.setHasFixedSize(true);
        rvCustomerActivities.setAdapter(customerActivitiesAdapter);
        swipeRefreshLayout.setColorSchemeColors(getActivity()
                .getResources().getIntArray(R.array.swipeRefreshColors));
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void showCustomerCommands(List<Command> commands) {
        showRecyclerView(true);
        customerActivitiesAdapter.setCommands(commands);
    }

    @Override
    public void showEmptyCommands(String message) {
        showRecyclerView(false);
        showFineractEmptyUI(getString(R.string.activities), getString(R.string.activities),
                R.drawable.ic_event_black_24dp);
    }

    @Override
    public void showRecyclerView(boolean status) {
        if (status) {
            rvCustomerActivities.setVisibility(View.VISIBLE);
            layoutError.setVisibility(View.GONE);
        } else {
            rvCustomerActivities.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
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
    public void showNoInternetConnection() {
        showRecyclerView(false);
        showFineractNoInternetUI();
    }

    @Override
    public void showError(String message) {
        showRecyclerView(false);
        showFineractErrorUI(getString(R.string.activities));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        customerActivitiesPresenter.detachView();
    }
}
