package com.mifos.apache.fineract.ui.customerdeposit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.deposit.CustomerDepositAccounts;
import com.mifos.apache.fineract.ui.adapters.CustomerDepositAdapter;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.mifos.apache.fineract.ui.base.OnItemClickListener;
import com.mifos.apache.fineract.utils.ConstantKeys;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 07/07/17.
 */
public class CustomerDepositFragment extends MifosBaseFragment
        implements CustomerDepositContract.View, OnItemClickListener {

    @BindView(R.id.rv_customers_deposit_accounts)
    RecyclerView rvCustomerDepositAccounts;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    @BindView(R.id.iv_retry)
    ImageView ivRetry;

    @BindView(R.id.tv_error)
    TextView tvError;

    @Inject
    CustomerDepositPresenter customerDepositPresenter;

    @Inject
    CustomerDepositAdapter customerDepositAdapter;

    View rootView;

    private String customerIdentifier;

    public static CustomerDepositFragment newInstance(String customerIdentifier) {
        CustomerDepositFragment fragment = new CustomerDepositFragment();
        Bundle args = new Bundle();
        args.putString(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customerIdentifier = getArguments().getString(ConstantKeys.CUSTOMER_IDENTIFIER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customer_deposit, container, false);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        setToolbarTitle(getString(R.string.deposit_accounts));

        customerDepositPresenter.attachView(this);

        showUserInterface();

        customerDepositPresenter.fetchCustomerDepositAccounts(customerIdentifier);

        return rootView;
    }

    @OnClick(R.id.iv_retry)
    void onRetry() {
        showRecyclerView(true);
        customerDepositPresenter.fetchCustomerDepositAccounts(customerIdentifier);
    }

    @Override
    public void showUserInterface() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCustomerDepositAccounts.setLayoutManager(layoutManager);
        rvCustomerDepositAccounts.setHasFixedSize(true);
        customerDepositAdapter.setOnItemClickListener(this);
        rvCustomerDepositAccounts.setAdapter(customerDepositAdapter);
    }

    @Override
    public void showCustomerDeposits(List<CustomerDepositAccounts> customerDepositAccounts) {
        showRecyclerView(true);
        customerDepositAdapter.setCustomerDepositAccounts(customerDepositAccounts);
    }

    @Override
    public void showError(String errorMessage) {
        showRecyclerView(false);
        tvError.setText(errorMessage);
    }

    @Override
    public void showRecyclerView(boolean status) {
        if (status) {
            rvCustomerDepositAccounts.setVisibility(View.VISIBLE);
            rlError.setVisibility(View.GONE);
        } else {
            rvCustomerDepositAccounts.setVisibility(View.GONE);
            rlError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showProgressbar() {
        showMifosProgressBar();
    }

    @Override
    public void hideProgressbar() {
        hideMifosProgressBar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideMifosProgressBar();
        customerDepositPresenter.detachView();
    }

    @Override
    public void onItemClick(View childView, int position) {

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }
}
