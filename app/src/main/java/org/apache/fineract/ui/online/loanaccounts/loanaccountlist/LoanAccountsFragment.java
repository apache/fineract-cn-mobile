package org.apache.fineract.ui.online.loanaccounts.loanaccountlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.models.loan.LoanAccount;
import org.apache.fineract.ui.adapters.LoanAccountListAdapter;
import org.apache.fineract.ui.base.EndlessRecyclerViewScrollListener;
import org.apache.fineract.ui.base.MifosBaseActivity;
import org.apache.fineract.ui.base.MifosBaseFragment;
import org.apache.fineract.ui.base.OnItemClickListener;
import org.apache.fineract.ui.base.Toaster;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.loanactivity
        .LoanApplicationActivity;
import org.apache.fineract.ui.online.loanaccounts.loandetails.CustomerLoanDetailsFragment;
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
public class LoanAccountsFragment extends MifosBaseFragment implements LoanAccountsContract.View,
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    @BindView(R.id.rv_customers_loans)
    RecyclerView rvCustomerLoans;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    @BindView(R.id.tv_error)
    TextView tvErrorMessage;

    @Inject
    LoanAccountsPresenter customerLoansPresenter;

    @Inject
    LoanAccountListAdapter customerLoanAdapter;

    View rootView;

    private String customerIdentifier;
    private List<LoanAccount> loanAccounts;

    public static LoanAccountsFragment newInstance(String customerIdentifier) {
        LoanAccountsFragment fragment = new LoanAccountsFragment();
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
        loanAccounts = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customer_loans, container, false);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        customerLoansPresenter.attachView(this);
        setToolbarTitle(getString(R.string.loan_accounts));

        showUserInterface();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        customerLoansPresenter.fetchCustomerLoanAccounts(customerIdentifier, 0, false);
    }

    @Override
    public void onRefresh() {
        customerLoansPresenter.fetchCustomerLoanAccounts(customerIdentifier, 0, false);
    }

    @OnClick(R.id.iv_retry)
    void onRetry() {
        customerLoansPresenter.fetchCustomerLoanAccounts(customerIdentifier, 0, false);
    }

    @OnClick(R.id.fab_add_customer_loan)
    void createNewLoan() {
        Intent intent = new Intent(getActivity(), LoanApplicationActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        startActivity(intent);
    }

    @Override
    public void showUserInterface() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCustomerLoans.setLayoutManager(layoutManager);
        rvCustomerLoans.setHasFixedSize(true);
        customerLoanAdapter.setOnItemClickListener(this);
        rvCustomerLoans.setAdapter(customerLoanAdapter);
        swipeRefreshLayout.setColorSchemeColors(getActivity()
                .getResources().getIntArray(R.array.swipeRefreshColors));
        swipeRefreshLayout.setOnRefreshListener(this);

        rvCustomerLoans.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customerLoansPresenter.fetchCustomerLoanAccounts(customerIdentifier, page, true);
            }
        });
    }

    @Override
    public void showLoanAccounts(List<LoanAccount> loanAccounts) {
        showRecyclerView(true);
        this.loanAccounts = loanAccounts;
        customerLoanAdapter.setCustomerLoanAccounts(loanAccounts);
    }

    @Override
    public void showMoreLoanAccounts(List<LoanAccount> loanAccounts) {
        showRecyclerView(true);
        this.loanAccounts.addAll(loanAccounts);
        customerLoanAdapter.setMoreCustomerLoanAccounts(loanAccounts);
    }

    @Override
    public void showEmptyLoanAccounts(String message) {
        showRecyclerView(false);
        tvErrorMessage.setText(getString(R.string.empty_customer_loans));
        showMessage(getString(R.string.empty_customer_loans));
    }

    @Override
    public void showRecyclerView(boolean status) {
        if (status) {
            rvCustomerLoans.setVisibility(View.VISIBLE);
            rlError.setVisibility(View.GONE);
        } else {
            rvCustomerLoans.setVisibility(View.GONE);
            rlError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMessage(String message) {
        Toaster.show(rootView, message);
    }

    @Override
    public void showError() {
        showRecyclerView(false);
        tvErrorMessage.setText(getString(R.string.error_loading_customer_loans));
        showMessage(getString(R.string.error_loading_customer_loans));
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
    public void onDestroyView() {
        super.onDestroyView();
        customerLoansPresenter.detachView();
    }

    @Override
    public void onItemClick(View childView, int position) {
        ((MifosBaseActivity) getActivity()).replaceFragment(CustomerLoanDetailsFragment.newInstance(
                        loanAccounts.get(position).getProductIdentifier(),
                        loanAccounts.get(position).getIdentifier()), true, R.id.container);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }
}
