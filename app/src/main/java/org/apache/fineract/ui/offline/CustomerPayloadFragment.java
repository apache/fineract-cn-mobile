package org.apache.fineract.ui.offline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.ui.adapters.CustomerAdapter;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerPayloadFragment extends FineractBaseFragment implements
        CustomerPayloadContract.View{

    public static CustomerPayloadFragment newInstance() {
        CustomerPayloadFragment fragment = new CustomerPayloadFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @BindView(R.id.rv_customers)
    RecyclerView rvCustomers;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.layout_error)
    View layoutError;

    @Inject
    CustomerAdapter customerAdapter;

    @Inject
    CustomerPayloadPresenter customerPayloadPresenter;

    private View rootView;
    private SweetUIErrorHandler sweetUIErrorHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customer_payload, container, false);
        ((FineractBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        sweetUIErrorHandler = new SweetUIErrorHandler(getActivity(), rootView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCustomers.setLayoutManager(layoutManager);
        rvCustomers.setHasFixedSize(true);
        rvCustomers.setAdapter(customerAdapter);

        customerPayloadPresenter.attachView(this);
        customerPayloadPresenter.fetchCustomers();
        return rootView;
    }

    @Override
    public void showCustomers(List<Customer> customers) {
        customerAdapter.setCustomers(customers);
    }

    @Override
    public void showEmptyCustomers() {
        sweetUIErrorHandler.showSweetCustomErrorUI(getString(R.string.no_customer_sync),
                R.drawable.ic_error_black_24dp, swipeRefreshLayout, layoutError);
    }

    @Override
    public void showNoInternetConnection() {

    }

    @Override
    public void showError(String message) {

    }
}
