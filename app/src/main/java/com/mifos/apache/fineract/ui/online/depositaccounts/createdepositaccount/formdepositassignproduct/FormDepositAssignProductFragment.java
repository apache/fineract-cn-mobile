package com.mifos.apache.fineract.ui.online.depositaccounts.createdepositaccount
        .formdepositassignproduct;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.customer.Customer;
import com.mifos.apache.fineract.data.models.deposit.DepositAccount;
import com.mifos.apache.fineract.ui.adapters.BeneficiaryAdapter;
import com.mifos.apache.fineract.ui.adapters.BeneficiaryAutoCompleteAdapter;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.mifos.apache.fineract.ui.online.depositaccounts.createdepositaccount.DepositAction;
import com.mifos.apache.fineract.ui.online.depositaccounts.createdepositaccount
        .DepositOnNavigationBarListener;
import com.mifos.apache.fineract.ui.views.DelayAutoCompleteTextView;
import com.mifos.apache.fineract.utils.ConstantKeys;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 13/08/17.
 */
public class FormDepositAssignProductFragment extends MifosBaseFragment implements Step,
        FormDepositAssignProductContract.View, AdapterView.OnItemSelectedListener,
        AdapterView.OnItemClickListener {

    private static final Integer THRESHOLD = 1;

    @BindView(R.id.sp_products)
    Spinner spProducts;

    @BindView(R.id.tv_select_product_header)
    TextView tvSelectProductHeader;

    @BindView(R.id.et_search_beneficiary)
    DelayAutoCompleteTextView etSearchBeneficiary;

    @BindView(R.id.til_search_beneficiary)
    TextInputLayout tilSearchBeneficiary;

    @BindView(R.id.rv_beneficiary)
    RecyclerView rvBeneficiary;

    @BindView(R.id.cl_deposit_assign_product)
    CoordinatorLayout clDepositAssignProduct;

    @BindView(R.id.pb_search_beneficiary)
    ProgressBar pbSearchBeneficiary;

    @Inject
    FormDepositAssignProductPresenter formDepositAssignProductPresenter;

    @Inject
    BeneficiaryAutoCompleteAdapter beneficiaryAutoCompleteAdapter;

    @Inject
    BeneficiaryAdapter beneficiaryAdapter;

    View rootView;

    private DepositAction depositAction;
    private DepositAccount depositAccount;
    private List<String> products;
    private ArrayAdapter<String> productsAdapter;
    private String productIdentifier;
    private DepositOnNavigationBarListener.ProductInstanceDetails onNavigationBarListener;

    public static FormDepositAssignProductFragment newInstance(DepositAction action,
            DepositAccount depositAccounts) {
        FormDepositAssignProductFragment fragment = new FormDepositAssignProductFragment();
        Bundle args = new Bundle();
        args.putSerializable(ConstantKeys.DEPOSIT_ACTION, action);
        args.putParcelable(ConstantKeys.DEPOSIT_ACCOUNT, depositAccounts);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        products = new ArrayList<>();
        depositAccount = new DepositAccount();
        if (getArguments() != null) {
            depositAction = (DepositAction) getArguments().getSerializable(
                    ConstantKeys.DEPOSIT_ACTION);
            depositAccount = getArguments().getParcelable(ConstantKeys.DEPOSIT_ACCOUNT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_form_deposit_assign_product,
                container, false);
        ButterKnife.bind(this, rootView);
        formDepositAssignProductPresenter.attachView(this);

        showUserInterface();

        switch (depositAction) {
            case CREATE:
                formDepositAssignProductPresenter.fetchProductDefinitions();
                break;
            case EDIT:
                editDepositAccountDetails(depositAccount);
                break;
        }

        return rootView;
    }

    @Override
    public VerificationError verifyStep() {
        if (productIdentifier == null) {
            return new VerificationError(null);
        } else {
            if (depositAction == DepositAction.CREATE) {
                DepositAccount depositAccount = new DepositAccount();
                depositAccount.setBeneficiaries(beneficiaryAdapter.getAllBeneficiary());
                depositAccount.setProductIdentifier(productIdentifier);
                onNavigationBarListener.setProductInstance(depositAccount,
                        spProducts.getSelectedItem().toString());
            } else {
                depositAccount.setBeneficiaries(beneficiaryAdapter.getAllBeneficiary());
                onNavigationBarListener.setProductInstance(depositAccount, null);
            }
        }
        return null;
    }

    @Override
    public void onSelected() {
    }

    @Override
    public void onError(@NonNull VerificationError error) {
    }

    @Override
    public void showUserInterface() {
        productsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_gallery_item, products);
        productsAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spProducts.setAdapter(productsAdapter);
        spProducts.setOnItemSelectedListener(this);

        etSearchBeneficiary.setThreshold(THRESHOLD);
        etSearchBeneficiary.setAdapter(beneficiaryAutoCompleteAdapter);
        etSearchBeneficiary.setLoadingIndicator(pbSearchBeneficiary);
        etSearchBeneficiary.setOnItemClickListener(this);

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvBeneficiary.setLayoutManager(staggeredGridLayoutManager);
        rvBeneficiary.setAdapter(beneficiaryAdapter);
    }

    @Override
    public void editDepositAccountDetails(DepositAccount depositAccount) {
        clDepositAssignProduct.setVisibility(View.VISIBLE);
        beneficiaryAdapter.setAllBeneficiary(depositAccount.getBeneficiaries());
        spProducts.setVisibility(View.GONE);
        tvSelectProductHeader.setVisibility(View.GONE);
        productIdentifier = depositAccount.getProductIdentifier();
    }

    @Override
    public void showProductDefinitions(List<String> products) {
        clDepositAssignProduct.setVisibility(View.VISIBLE);
        this.products.addAll(products);
        productsAdapter.notifyDataSetChanged();
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
    public void showError(String message) {
        clDepositAssignProduct.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        productIdentifier = formDepositAssignProductPresenter.getProductIdentifier(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Customer customer = (Customer) parent.getItemAtPosition(position);
        beneficiaryAdapter.setBeneficiary(customer.getIdentifier());
        etSearchBeneficiary.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideMifosProgressBar();
        formDepositAssignProductPresenter.detachView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = context instanceof Activity ? (Activity) context : null;
        try {
            onNavigationBarListener = (DepositOnNavigationBarListener.ProductInstanceDetails)
                    activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DepositOnNavigationBarListener.ProductInstanceDetails");
        }
    }
}
