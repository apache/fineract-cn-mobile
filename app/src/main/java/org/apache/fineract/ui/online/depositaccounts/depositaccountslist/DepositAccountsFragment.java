package org.apache.fineract.ui.online.depositaccounts.depositaccountslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.fineract.R;
import org.apache.fineract.data.models.deposit.DepositAccount;
import org.apache.fineract.ui.adapters.CustomerDepositAdapter;
import org.apache.fineract.ui.base.MifosBaseActivity;
import org.apache.fineract.ui.base.MifosBaseFragment;
import org.apache.fineract.ui.base.OnItemClickListener;
import org.apache.fineract.ui.online.depositaccounts.createdepositaccount.DepositAction;
import org.apache.fineract.ui.online.depositaccounts.createdepositaccount.createdepositactivity
        .CreateDepositActivity;
import org.apache.fineract.ui.online.depositaccounts.depositaccountdetails
        .DepositAccountDetailsFragment;
import org.apache.fineract.ui.uierrorhandler.MifosUIErrorHandler;
import org.apache.fineract.ui.uierrorhandler.UIType;
import org.apache.fineract.utils.ConstantKeys;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 07/07/17.
 */
public class DepositAccountsFragment extends MifosBaseFragment
        implements DepositAccountsContract.View, OnItemClickListener {

    @BindView(R.id.rv_customers_deposit_accounts)
    RecyclerView rvCustomerDepositAccounts;

    @BindView(R.id.layout_error)
    View layoutError;

    @Inject
    DepositAccountsPresenter customerDepositPresenter;

    @Inject
    CustomerDepositAdapter customerDepositAdapter;

    View rootView;

    private String customerIdentifier;
    private List<DepositAccount> customerDepositAccounts;
    private MifosUIErrorHandler mifosUIErrorHandler;

    public static DepositAccountsFragment newInstance(String customerIdentifier) {
        DepositAccountsFragment fragment = new DepositAccountsFragment();
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
        customerDepositAccounts = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customer_deposit, container, false);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        mifosUIErrorHandler = new MifosUIErrorHandler(getActivity(), rootView);
        setToolbarTitle(getString(R.string.deposit_accounts));

        customerDepositPresenter.attachView(this);

        showUserInterface();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        rvCustomerDepositAccounts.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
        customerDepositPresenter.fetchCustomerDepositAccounts(customerIdentifier);
    }

    @OnClick(R.id.btn_try_again)
    public void reTry() {
        showRecyclerView(true);
        customerDepositPresenter.fetchCustomerDepositAccounts(customerIdentifier);
    }

    @OnClick(R.id.fab_add_deposit_accounts)
    void createDepositAccount() {
        Intent intent = new Intent(getActivity(), CreateDepositActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        intent.putExtra(ConstantKeys.DEPOSIT_ACTION, DepositAction.CREATE);
        startActivity(intent);
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
    public void showCustomerDeposits(List<DepositAccount> customerDepositAccounts) {
        showRecyclerView(true);
        this.customerDepositAccounts = customerDepositAccounts;
        customerDepositAdapter.setCustomerDepositAccounts(customerDepositAccounts);
    }

    @Override
    public void showEmptyDepositAccounts() {
        showRecyclerView(false);
        mifosUIErrorHandler.showEmptyOrErrorUI(UIType.EMPTY_UI,
                getString(R.string.deposit_accounts),
                getString(R.string.deposit_account),
                R.drawable.ic_monetization_on_black_24dp);
    }

    @Override
    public void showError(String errorMessage) {
        showRecyclerView(false);
        mifosUIErrorHandler.showEmptyOrErrorUI(UIType.ERROR_UI,
                getString(R.string.deposit_accounts),
                null, null);
    }

    @Override
    public void showRecyclerView(boolean status) {
        if (status) {
            rvCustomerDepositAccounts.setVisibility(View.VISIBLE);
            layoutError.setVisibility(View.GONE);
        } else {
            rvCustomerDepositAccounts.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
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
        ((MifosBaseActivity) getActivity()).replaceFragment(
                DepositAccountDetailsFragment.newInstance(customerDepositAccounts.get(position)
                        .getAccountIdentifier()), true, R.id.container);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }
}
