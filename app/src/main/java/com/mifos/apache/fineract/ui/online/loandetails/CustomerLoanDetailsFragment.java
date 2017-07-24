package com.mifos.apache.fineract.ui.online.loandetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.loan.LoanAccount;
import com.mifos.apache.fineract.data.models.loan.PaymentCycle;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.mifos.apache.fineract.ui.base.Toaster;
import com.mifos.apache.fineract.ui.online.plannedpayment.PlannedPaymentActivity;
import com.mifos.apache.fineract.utils.ConstantKeys;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 11/07/17.
 */
public class CustomerLoanDetailsFragment extends MifosBaseFragment implements
        CustomerLoanDetailsContract.View {

    @BindView(R.id.tv_principal_amount)
    TextView tvPaymentAmount;

    @BindView(R.id.tv_repayment_cycle)
    TextView tvPaymentCycle;

    @BindView(R.id.tv_term)
    TextView tvTerm;

    @BindView(R.id.cl_alert_message)
    CoordinatorLayout clAlertMessage;

    @BindView(R.id.tv_loan_current_status)
    TextView tvLoanCurrentStatus;

    @BindView(R.id.cl_customer_loan_details)
    CoordinatorLayout clCustomerLoanDetails;

    @BindView(R.id.layout_disburse_btn)
    RelativeLayout layoutDisburseButton;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    @BindView(R.id.tv_alert_text_1)
    TextView tvAlertText1;

    @BindView(R.id.tv_alert_text_2)
    TextView tvAlertText2;

    @BindView(R.id.tv_error)
    TextView tvError;

    @BindView(R.id.fab_edit_customer_loan)
    FloatingActionButton fabEditCustomerLoan;

    @Inject
    CustomerLoanDetailsPresenter customerLoanDetailsPresenter;

    View rootView;

    private String productIdentifier;
    private String caseIdentifier;

    public static CustomerLoanDetailsFragment newInstance(String productIdentifier,
            String caseIdentifier) {
        CustomerLoanDetailsFragment fragment = new CustomerLoanDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ConstantKeys.PRODUCT_IDENTIFIER, productIdentifier);
        args.putString(ConstantKeys.CASE_IDENTIFIER, caseIdentifier);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productIdentifier = getArguments().getString(ConstantKeys.PRODUCT_IDENTIFIER);
            caseIdentifier = getArguments().getString(ConstantKeys.CASE_IDENTIFIER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customer_loan_details, container, false);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        customerLoanDetailsPresenter.attachView(this);

        customerLoanDetailsPresenter.fetchCustomerLoanDetails(productIdentifier, caseIdentifier);

        return rootView;
    }

    @OnClick(R.id.iv_retry)
    void onRetry() {
        clCustomerLoanDetails.setVisibility(View.GONE);
        rlError.setVisibility(View.GONE);
        customerLoanDetailsPresenter.fetchCustomerLoanDetails(productIdentifier, caseIdentifier);
    }

    @OnClick(R.id.ll_planned_payment)
    void showPlannedPayment() {
        Intent intent = new Intent(getActivity(), PlannedPaymentActivity.class);
        intent.putExtra(ConstantKeys.PRODUCT_IDENTIFIER, productIdentifier);
        intent.putExtra(ConstantKeys.CASE_IDENTIFIER, caseIdentifier);
        startActivity(intent);
    }

    @Override
    public void showLoanAccountDetails(LoanAccount loanAccount) {
        clCustomerLoanDetails.setVisibility(View.VISIBLE);
        rlError.setVisibility(View.GONE);
        setToolbarTitle(loanAccount.getIdentifier());

        tvPaymentAmount.setText(
                String.valueOf(loanAccount.getLoanParameters().getMaximumBalance()));
        String term = loanAccount.getLoanParameters().getTermRange().getMaximum() + " " +
                loanAccount.getLoanParameters().getTermRange().getTemporalUnit();
        tvTerm.setText(term);

        PaymentCycle paymentCycle = loanAccount.getLoanParameters().getPaymentCycle();
        tvPaymentCycle.setText(getString(R.string.payment_cycle_value, paymentCycle.getPeriod(),
                paymentCycle.getTemporalUnit(), (paymentCycle.getAlignmentDay() + 1)));

        tvLoanCurrentStatus.setText(loanAccount.getCurrentState().name());

        switch (loanAccount.getCurrentState()) {
            case ACTIVE:
                tvLoanCurrentStatus.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_check_circle_black_24dp, 0, 0, 0);
                layoutDisburseButton.setVisibility(View.VISIBLE);
                break;
            case APPROVED:
                clAlertMessage.setVisibility(View.VISIBLE);
                tvAlertText1.setText(getString(R.string.customer_loan_approved));
                tvAlertText2.setText(getString(R.string.to_activate_loan_disburse));
                fabEditCustomerLoan.setVisibility(View.GONE);
                tvLoanCurrentStatus.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_done_all_black_24dp, 0, 0, 0);
                layoutDisburseButton.setVisibility(View.VISIBLE);
                break;
            case CLOSED:
                break;
            case PENDING:
                tvLoanCurrentStatus.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_hourglass_empty_black_24dp, 0, 0, 0);
                break;
            case CREATED:
                tvLoanCurrentStatus.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_hourglass_empty_black_24dp, 0, 0, 0);
                break;
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
    public void showError(String message) {
        clCustomerLoanDetails.setVisibility(View.GONE);
        rlError.setVisibility(View.VISIBLE);
        tvError.setText(message);
        Toaster.show(rootView, message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideMifosProgressBar();
        customerLoanDetailsPresenter.detachView();
    }
}
