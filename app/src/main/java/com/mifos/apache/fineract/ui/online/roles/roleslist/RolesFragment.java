package com.mifos.apache.fineract.ui.online.roles.roleslist;

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

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.rolesandpermission.Role;
import com.mifos.apache.fineract.ui.adapters.RolesAdapter;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.mifos.apache.fineract.ui.base.OnItemClickListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 24/08/17.
 */
public class RolesFragment extends MifosBaseFragment implements RolesContract.View,
        OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_roles)
    RecyclerView rvRoles;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    @BindView(R.id.iv_retry)
    ImageView ivRetry;

    @BindView(R.id.tv_error)
    TextView tvError;

    View rootView;

    @Inject
    RolesPresenter rolesPresenter;

    @Inject
    RolesAdapter rolesAdapter;

    public static RolesFragment newInstance() {
        RolesFragment fragment = new RolesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_roles_list, container, false);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        rolesPresenter.attachView(this);
        setToolbarTitle(getString(R.string.manage_roles));

        showUserInterface();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        rolesPresenter.fetchRoles();
    }

    @Override
    public void onRefresh() {
        rolesPresenter.fetchRoles();
    }

    @OnClick(R.id.iv_retry)
    void reloadRoles() {
        rlError.setVisibility(View.GONE);
        rvRoles.setVisibility(View.GONE);
        rolesPresenter.fetchRoles();
    }

    @Override
    public void showUserInterface() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRoles.setLayoutManager(layoutManager);
        rvRoles.setHasFixedSize(true);
        rolesAdapter.setOnItemClickListener(this);
        rvRoles.setAdapter(rolesAdapter);
        swipeRefreshLayout.setColorSchemeColors(getActivity()
                .getResources().getIntArray(R.array.swipeRefreshColors));
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void showRoles(List<Role> roles) {
        showRecyclerView(true);
        rolesAdapter.setRoles(roles);
    }

    @Override
    public void showEmptyRoles() {
        showRecyclerView(false);
        tvError.setText(R.string.empty_roles);
        ivRetry.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.ic_assignment_turned_in_black_24dp));
    }

    @Override
    public void showRecyclerView(Boolean status) {
        if (status) {
            rvRoles.setVisibility(View.VISIBLE);
            rlError.setVisibility(View.GONE);
        } else {
            rvRoles.setVisibility(View.GONE);
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
        tvError.setText(message);
    }

    @Override
    public void onItemClick(View childView, int position) {

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rolesPresenter.detachView();
    }
}
