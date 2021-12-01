package org.apache.fineract.ui.online.identification.identificationlist;

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
import org.apache.fineract.data.models.customer.identification.Identification;
import org.apache.fineract.ui.adapters.IdentificationAdapter;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.ui.base.OnItemClickListener;
import org.apache.fineract.ui.base.Toaster;
import org.apache.fineract.ui.online.identification.createidentification.Action;
import org.apache.fineract.ui.online.identification.createidentification.identificationactivity.CreateIdentificationActivity;
import org.apache.fineract.ui.online.identification.identificationdetails.IdentificationDetailsFragment;
import org.apache.fineract.utils.ConstantKeys;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 31/07/17.
 */
public class IdentificationsFragment extends FineractBaseFragment implements
        IdentificationsContract.View, OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_identifications)
    RecyclerView rvIdentifications;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.layout_error)
    View layoutError;

    @Inject
    IdentificationsPresenter identificationsPresenter;

    @Inject
    IdentificationAdapter identificationAdapter;

    View rootView;

    private List<Identification> identifications;
    private String customerIdentifier;

    public static IdentificationsFragment newInstance(String identifier) {
        IdentificationsFragment fragment = new IdentificationsFragment();
        Bundle args = new Bundle();
        args.putString(ConstantKeys.CUSTOMER_IDENTIFIER, identifier);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        identifications = new ArrayList<>();
        if (getArguments() != null) {
            customerIdentifier = getArguments().getString(ConstantKeys.CUSTOMER_IDENTIFIER);
        }

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_identification_list, container, false);
        ((FineractBaseActivity) getActivity()).getActivityComponent().inject(this);
        identificationsPresenter.attachView(this);
        ButterKnife.bind(this, rootView);
        initializeFineractUIErrorHandler(getActivity(), rootView);

        showUserInterface();

        return rootView;
    }

    @OnClick(R.id.fab_add_identification_card)
    void createIdentifier() {
        Intent intent = new Intent(getActivity(), CreateIdentificationActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        intent.putExtra(ConstantKeys.IDENTIFICATION_ACTION, Action.CREATE);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        identificationsPresenter.fetchIdentifications(customerIdentifier);
    }

    @Override
    public void onRefresh() {
        identificationsPresenter.fetchIdentifications(customerIdentifier);
    }

    @Override
    public void showUserInterface() {
        setToolbarTitle(getString(R.string.identification_cards));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvIdentifications.setLayoutManager(layoutManager);
        rvIdentifications.setHasFixedSize(true);
        identificationAdapter.setOnItemClickListener(this);
        identificationAdapter.setIdentifications(identifications);
        rvIdentifications.setAdapter(identificationAdapter);

        swipeRefreshLayout.setColorSchemeColors(getActivity()
                .getResources().getIntArray(R.array.swipeRefreshColors));
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void showIdentification(List<Identification> identifications) {
        showRecyclerView(true);
        this.identifications = identifications;
        identificationAdapter.setIdentifications(identifications);
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
    public void showRecyclerView(boolean status) {
        if (status) {
            rvIdentifications.setVisibility(View.VISIBLE);
            layoutError.setVisibility(View.GONE);
        } else {
            rvIdentifications.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showEmptyIdentifications() {
        showRecyclerView(false);
        showFineractEmptyUI(getString(R.string.identification_cards),
                getString(R.string.identification_card), R.drawable.ic_person_outline_black_24dp);
    }

    @Override
    public void showMessage(String message) {
        Toaster.show(rootView, message);
    }

    @Override
    public void searchedIdentifications(List<Identification> identification) {
        identificationAdapter.setIdentifications(identification);
    }

    @Override
    public void showNoInternetConnection() {
        showRecyclerView(false);
        showFineractNoInternetUI();
    }

    @Override
    public void showError(String message) {
        showRecyclerView(false);
        showFineractErrorUI(getString(R.string.identification_cards));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_identification_search, menu);
        setUpSearchInterface(menu);
    }

    private void setUpSearchInterface(Menu menu) {

        SearchManager manager = (SearchManager) getActivity().
                getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(
                R.id.identification_search).getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                identificationsPresenter.searchIdentifications(identifications, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    identificationAdapter.setIdentifications(identifications);
                }
                identificationsPresenter.searchIdentifications(identifications, newText);
                return false;
            }
        });

        baseSearchView = searchView;
    }

    @Override
    public void onItemClick(View childView, int position) {
        ((FineractBaseActivity) getActivity()).replaceFragment(
                IdentificationDetailsFragment.newInstance(customerIdentifier,
                        identificationAdapter.getItem(position)), true, R.id.container);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        identificationsPresenter.detachView();
    }
}
