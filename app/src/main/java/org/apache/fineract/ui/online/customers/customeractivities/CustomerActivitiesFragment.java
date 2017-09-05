package org.apache.fineract.ui.online.customers.customeractivities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.Command;
import org.apache.fineract.ui.adapters.CustomerActivitiesAdapter;
import org.apache.fineract.ui.base.MifosBaseActivity;
import org.apache.fineract.ui.base.MifosBaseFragment;
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
public class CustomerActivitiesFragment extends MifosBaseFragment implements
        CustomerActivitiesContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_customer_activities)
    RecyclerView rvCustomerActivities;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    @BindView(R.id.iv_retry)
    ImageView ivRetry;

    @BindView(R.id.tv_error)
    TextView tvStatus;

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
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
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

    @OnClick(R.id.iv_retry)
    void onRetry() {
        rvCustomerActivities.setVisibility(View.GONE);
        rlError.setVisibility(View.GONE);
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
        tvStatus.setText(message);
        ivRetry.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.ic_assignment_turned_in_black_24dp));
        ivRetry.setEnabled(false);
    }

    @Override
    public void showRecyclerView(boolean status) {
        if (status) {
            rvCustomerActivities.setVisibility(View.VISIBLE);
            rlError.setVisibility(View.GONE);
        } else {
            rvCustomerActivities.setVisibility(View.GONE);
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
    public void showError(String message) {
        showRecyclerView(false);
        tvStatus.setText(message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        customerActivitiesPresenter.detachView();
    }
}
