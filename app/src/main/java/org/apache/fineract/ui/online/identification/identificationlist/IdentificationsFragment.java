package org.apache.fineract.ui.online.identification.identificationlist;

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
import org.apache.fineract.data.models.customer.identification.Identification;
import org.apache.fineract.ui.adapters.IdentificationAdapter;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.ui.base.OnItemClickListener;
import org.apache.fineract.ui.base.Toaster;
import org.apache.fineract.ui.online.identification.createidentification.Action;
import org.apache.fineract.ui.online.identification.createidentification.identificationactivity
        .CreateIdentificationActivity;
import org.apache.fineract.ui.online.identification.identificationdetails
        .IdentificationDetailsFragment;
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

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    @BindView(R.id.tv_error)
    TextView tvError;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_identification_list, container, false);
        ((FineractBaseActivity) getActivity()).getActivityComponent().inject(this);
        identificationsPresenter.attachView(this);
        ButterKnife.bind(this, rootView);

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
            rlError.setVisibility(View.GONE);
        } else {
            rvIdentifications.setVisibility(View.GONE);
            rlError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMessage(String message) {
        showRecyclerView(false);
        tvError.setText(message);
        Toaster.show(rootView, message);
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
