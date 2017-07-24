package com.mifos.apache.fineract.ui.online.depositdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.deposit.CustomerDepositAccounts;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.mifos.apache.fineract.ui.base.Toaster;
import com.mifos.apache.fineract.utils.ConstantKeys;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 12/07/17.
 */
public class CustomerDepositDetailsFragment extends MifosBaseFragment
        implements CustomerDepositDetailsContract.View {

    @BindView(R.id.cl_customer_deposit_details)
    CoordinatorLayout clCustomerDepositDetails;

    @BindView(R.id.tv_deposit_current_status)
    TextView tvDepositCurrentStatus;

    @BindView(R.id.tv_account)
    TextView tvAccount;

    @BindView(R.id.tv_balance)
    TextView tvBalance;

    @BindView(R.id.tv_beneficiaries)
    TextView tvBeneficiaries;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    @BindView(R.id.tv_error)
    TextView tvError;

    @Inject
    CustomerDepositDetailsPresenter customerDepositDetailsPresenter;

    View rootView;

    private String accountIdentifier;

    public static CustomerDepositDetailsFragment newInstance(String accountIdentifier) {
        CustomerDepositDetailsFragment fragment = new CustomerDepositDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ConstantKeys.ACCOUNT_IDENTIFIER, accountIdentifier);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            accountIdentifier = getArguments().getString(ConstantKeys.ACCOUNT_IDENTIFIER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customer_deposit_details, container, false);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        customerDepositDetailsPresenter.attachView(this);
        setToolbarTitle(getString(R.string.deposit_account));

        customerDepositDetailsPresenter.fetchDepositAccountDetails(accountIdentifier);

        return rootView;
    }

    @OnClick(R.id.iv_retry)
    void onRetry() {
        clCustomerDepositDetails.setVisibility(View.GONE);
        rlError.setVisibility(View.GONE);
        customerDepositDetailsPresenter.fetchDepositAccountDetails(accountIdentifier);
    }

    @Override
    public void showDepositDetails(CustomerDepositAccounts customerDepositAccounts) {
        clCustomerDepositDetails.setVisibility(View.VISIBLE);
        rlError.setVisibility(View.GONE);

        switch (customerDepositAccounts.getState()) {
            case ACTIVE:
                tvDepositCurrentStatus.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_check_circle_black_24dp, 0, 0, 0);
                break;
            case APPROVED:
                tvDepositCurrentStatus.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_done_all_black_24dp, 0, 0, 0);
                break;
            case CLOSED:
                tvDepositCurrentStatus.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_close_black_24dp, 0, 0, 0);
                break;
            case PENDING:
                tvDepositCurrentStatus.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_hourglass_empty_black_24dp, 0, 0, 0);
                break;
            case CREATED:
                tvDepositCurrentStatus.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_hourglass_empty_black_24dp, 0, 0, 0);
                break;
        }

        tvDepositCurrentStatus.setText(customerDepositAccounts.getState().name());
        tvAccount.setText(customerDepositAccounts.getAccountIdentifier());
        tvBalance.setText(String.valueOf(customerDepositAccounts.getBalance()));
        tvBeneficiaries.setText(customerDepositAccounts.getBeneficiaries().toString());
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
        clCustomerDepositDetails.setVisibility(View.GONE);
        rlError.setVisibility(View.VISIBLE);
        tvError.setText(message);
        Toaster.show(rootView, message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideMifosProgressBar();
        customerDepositDetailsPresenter.detachView();
    }
}
