package org.apache.fineract.ui.online.loanaccounts.loanaccountlist;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.fineract.R;
import org.apache.fineract.data.models.loan.LoanAccount;
import org.apache.fineract.ui.adapters.LoanAccountListAdapter;
import org.apache.fineract.ui.base.EndlessRecyclerViewScrollListener;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.ui.base.OnItemClickListener;
import org.apache.fineract.ui.base.Toaster;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.loanactivity.LoanApplicationActivity;
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
public class LoanAccountsFragment extends FineractBaseFragment implements LoanAccountsContract.View,
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    @BindView(R.id.rv_customers_loans)
    RecyclerView rvCustomerLoans;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.layout_error)
    View layoutError;

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
        ((FineractBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        initializeFineractUIErrorHandler(getActivity(), rootView);
        customerLoansPresenter.attachView(this);
        setToolbarTitle(getString(R.string.loan_accounts));

        showUserInterface();
        setHasOptionsMenu(true);

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

    @OnClick(R.id.btn_try_again)
    void onRetry() {
        rvCustomerLoans.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
        customerLoansPresenter.fetchCustomerLoanAccounts(customerIdentifier, 0, false);
    }

    @OnClick(R.id.fab_add_customer_loan)
    void createNewLoan() {
        Intent intent = new Intent(getActivity(), LoanApplicationActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
        showFineractEmptyUI(getString(R.string.loan_accounts), getString(R.string.loan_account),
                R.drawable.ic_payment_black_24dp);
    }

    @Override
    public void showRecyclerView(boolean status) {
        if (status) {
            rvCustomerLoans.setVisibility(View.VISIBLE);
            layoutError.setVisibility(View.GONE);
        } else {
            rvCustomerLoans.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMessage(String message) {
        Toaster.show(rootView, message);
    }

    @Override
    public void showNoInternetConnection() {
        showRecyclerView(false);
        showFineractNoInternetUI();
    }

    @Override
    public void showError(String message) {
        showRecyclerView(false);
        showMessage(message);
        showFineractErrorUI(getString(R.string.loan_accounts));
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
        ((FineractBaseActivity) getActivity()).replaceFragment(
                CustomerLoanDetailsFragment.newInstance(
                        loanAccounts.get(position).getProductIdentifier(),
                        loanAccounts.get(position).getIdentifier()), true, R.id.container);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_loan_account_search, menu);
        setUpSearchInterface(menu);
    }

    private void setUpSearchInterface(Menu menu) {
        SearchManager searchManager = (SearchManager) getActivity().
                getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.loan_account_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity()
                .getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                customerLoansPresenter.searchLoanAccounts(loanAccounts, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    searchedLoanAccounts(loanAccounts);
                }
                customerLoansPresenter.searchLoanAccounts(loanAccounts, newText);
                return false;
            }
        });

    }

    @Override
    public void searchedLoanAccounts(List<LoanAccount> loanAccountList) {
        customerLoanAdapter.setCustomerLoanAccounts(loanAccountList);
    }
}
