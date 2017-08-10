package com.mifos.apache.fineract.ui.online.loans.loandetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.loan.LoanAccount;
import com.mifos.apache.fineract.data.models.loan.PaymentCycle;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.mifos.apache.fineract.ui.base.Toaster;
import com.mifos.apache.fineract.ui.online.debtincomereport.DebtIncomeReportActivity;
import com.mifos.apache.fineract.ui.online.plannedpayment.PlannedPaymentActivity;
import com.mifos.apache.fineract.utils.ConstantKeys;
import com.mifos.apache.fineract.utils.DateUtils;
import com.mifos.apache.fineract.utils.StatusUtils;
import com.mifos.apache.fineract.utils.Utils;

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

    @BindView(R.id.iv_loan_current_status)
    ImageView ivLoanCurrentStatus;

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

    @BindView(R.id.tv_customer_deposit_account)
    TextView tvCustomerDepositAccount;

    @BindView(R.id.tv_create_by)
    TextView tvCreatedBy;

    @BindView(R.id.tv_last_modified_by)
    TextView tvLastModifiedBy;

    @Inject
    CustomerLoanDetailsPresenter customerLoanDetailsPresenter;

    View rootView;

    private String productIdentifier;
    private String caseIdentifier;
    private LoanAccount loanAccount;

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
        setHasOptionsMenu(true);
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

    @OnClick(R.id.ll_debt_income_report)
    void showDebtIncomeReport() {
        Intent intent = new Intent(getActivity(), DebtIncomeReportActivity.class);
        intent.putExtra(ConstantKeys.LOAN_CREDITWORTHINESSSNAPSHOTS, (new Gson()).toJson(
                loanAccount.getLoanParameters().getCreditWorthinessSnapshots()));
        startActivity(intent);
    }

    @Override
    public void showLoanAccountDetails(LoanAccount loanAccount) {
        this.loanAccount = loanAccount;
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
        StatusUtils.setLoanAccountStatusIcon(loanAccount.getCurrentState(),
                ivLoanCurrentStatus, getActivity());

        if (loanAccount.getCurrentState() == LoanAccount.State.APPROVED) {
            clAlertMessage.setVisibility(View.VISIBLE);
            tvAlertText1.setText(getString(R.string.customer_loan_approved));
            tvAlertText2.setText(getString(R.string.to_activate_loan_disburse));
        }

        if (loanAccount.getAccountAssignments().size() != 0) {
            tvCustomerDepositAccount.setText(
                    loanAccount.getAccountAssignments().get(0).getAccountIdentifier());
        } else {
            tvCustomerDepositAccount.setText(R.string.no_deposit_account);
        }

        tvCreatedBy.setText(getString(R.string.loan_created_by, loanAccount.getCreatedBy(),
                DateUtils.getDateTime(loanAccount.getCreatedOn())));

        tvLastModifiedBy.setText(getString(R.string.loan_last_modified_by,
                loanAccount.getLastModifiedBy(),
                DateUtils.getDateTime(loanAccount.getLastModifiedOn())));
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_loan_account_details, menu);
        Utils.setToolbarIconColor(getActivity(), menu, R.color.white);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_loan_account_edit:
                Toaster.show(rootView, "Under construction");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideMifosProgressBar();
        customerLoanDetailsPresenter.detachView();
    }
}
