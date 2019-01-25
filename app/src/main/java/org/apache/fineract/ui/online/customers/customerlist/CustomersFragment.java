package org.apache.fineract.ui.online.customers.customerlist;

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

import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler;

import org.apache.fineract.R;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.ui.adapters.CustomerAdapter;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.ui.base.OnItemClickListener;
import org.apache.fineract.ui.base.Toaster;
import org.apache.fineract.ui.online.customers.createcustomer.CustomerAction;
import org.apache.fineract.ui.online.customers.createcustomer.customeractivity.CreateCustomerActivity;
import org.apache.fineract.ui.online.customers.customerdetails.CustomerDetailsActivity;
import org.apache.fineract.utils.ConstantKeys;

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
public class CustomersFragment extends FineractBaseFragment implements CustomersContract.View,
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    private static final Integer CUSTOMER_STATUS = 1;

    @BindView(R.id.rv_customers)
    RecyclerView rvCustomers;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.layout_error)
    View layoutError;

    View rootView;

    @Inject
    CustomersPresenter customerPresenter;

    @Inject
    CustomerAdapter customerAdapter;

    @Inject
    PreferencesHelper preferencesHelper;

    private List<Customer> customers;
    private Integer detailsCustomerPosition;
    private boolean isNewCustomer = false;
    private SweetUIErrorHandler sweetUIErrorHandler;

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
        ((FineractBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        initializeFineractUIErrorHandler(getActivity(), rootView);
        customerPresenter.attachView(this);
        setToolbarTitle(getString(R.string.customers));

        showUserInterface();
        sweetUIErrorHandler = new SweetUIErrorHandler(getActivity(), rootView);
        if (preferencesHelper.isFetching()) {
            sweetUIErrorHandler.showSweetCustomErrorUI(getString(R.string.syncing_please_wait),
                    R.drawable.ic_error_black_24dp, swipeRefreshLayout, layoutError);
        } else {
            customerPresenter.fetchCustomers(0, false);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNewCustomer) {
            isNewCustomer = false;
            customerPresenter.fetchCustomers(0, false);
        }
    }

    @Override
    public void onRefresh() {
        customerPresenter.fetchCustomers(0, false);
        sweetUIErrorHandler.hideSweetErrorLayoutUI(rvCustomers,layoutError);
    }

    @OnClick(R.id.btn_try_again)
    void onRetry() {
        customerPresenter.fetchCustomers(0, false);
        sweetUIErrorHandler.hideSweetErrorLayoutUI(rvCustomers,layoutError);
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
        sweetUIErrorHandler = new SweetUIErrorHandler(getActivity(),rootView);
    }

    @OnClick(R.id.fab_add_customer)
    void addCustomer() {
        isNewCustomer = true;
        Intent intent = new Intent(getActivity(), CreateCustomerActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_ACTION, CustomerAction.CREATE);
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
        sweetUIErrorHandler.showSweetCustomErrorUI(getString(R.string.customer),
                getString(Integer.parseInt(message)),R.drawable.ic_customer_black_24dp,rvCustomers,layoutError);
    }

    @Override
    public void showRecyclerView(boolean status) {
        if (status) {
            rvCustomers.setVisibility(View.VISIBLE);
            layoutError.setVisibility(View.GONE);
        } else {
            rvCustomers.setVisibility(View.GONE);
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
    public void showMessage(String errorMessage) {
        Toaster.show(rootView, errorMessage);
    }

    @Override
    public void showNoInternetConnection() {
        showRecyclerView(false);
        showFineractNoInternetUI();
        sweetUIErrorHandler.showSweetNoInternetUI(swipeRefreshLayout,layoutError);
    }

    @Override
    public void showError(String message) {
        if (customerAdapter.getItemCount() != 0) {
            sweetUIErrorHandler.showSweetCustomErrorUI(message,
                    R.drawable.ic_error_black_24dp,swipeRefreshLayout,layoutError);
        } else {
            showRecyclerView(false);
            sweetUIErrorHandler.showSweetCustomErrorUI(getString(R.string.customers),
                    R.drawable.ic_error_black_24dp,swipeRefreshLayout,layoutError);
        }
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
