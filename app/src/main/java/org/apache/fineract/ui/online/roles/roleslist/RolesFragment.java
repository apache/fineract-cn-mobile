package org.apache.fineract.ui.online.roles.roleslist;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.fineract.R;
import org.apache.fineract.data.models.rolesandpermission.Role;
import org.apache.fineract.ui.adapters.RolesAdapter;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.ui.base.OnItemClickListener;
import org.apache.fineract.ui.base.Toaster;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 24/08/17.
 */
public class RolesFragment extends FineractBaseFragment implements RolesContract.View,
        OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_roles)
    RecyclerView rvRoles;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.layout_error)
    View layoutError;

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
        ((FineractBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        initializeFineractUIErrorHandler(getActivity(), rootView);
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

    @OnClick(R.id.btn_try_again)
    void reloadRoles() {
        layoutError.setVisibility(View.GONE);
        rvRoles.setVisibility(View.GONE);
        rolesPresenter.fetchRoles();
    }
    @OnClick(R.id.fab_add_role)
    void showMessage() {
        Toaster.show(rootView, R.string.Under_construction);
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
        showFineractEmptyUI(getString(R.string.roles_and_permissions), getString(R.string.role),
                R.drawable.ic_lock_black_24dp);
    }

    @Override
    public void showRecyclerView(Boolean status) {
        if (status) {
            rvRoles.setVisibility(View.VISIBLE);
            layoutError.setVisibility(View.GONE);
        } else {
            rvRoles.setVisibility(View.GONE);
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
    public void showNoInternetConnection() {
        showRecyclerView(false);
        showFineractNoInternetUI();
    }

    @Override
    public void showError(String message) {
        showRecyclerView(false);
        showFineractErrorUI(getString(R.string.roles_and_permissions));
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
