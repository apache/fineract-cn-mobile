package org.apache.fineract.ui.online.loanaccounts.loandetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import com.google.gson.Gson;

import org.apache.fineract.R;
import org.apache.fineract.data.models.loan.LoanAccount;
import org.apache.fineract.data.models.loan.PaymentCycle;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.ui.online.loanaccounts.debtincomereport.DebtIncomeReportActivity;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.LoanApplicationAction;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.loanactivity.LoanApplicationActivity;
import org.apache.fineract.ui.online.loanaccounts.plannedpayment.PlannedPaymentActivity;
import org.apache.fineract.utils.ConstantKeys;
import org.apache.fineract.utils.DateUtils;
import org.apache.fineract.utils.StatusUtils;
import org.apache.fineract.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 * On 11/07/17.
 */
public class CustomerLoanDetailsFragment extends FineractBaseFragment implements
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

    @BindView(R.id.ncv_customer_loan_details)
    NestedScrollView ncvCustomerLoanDetails;

    @BindView(R.id.layout_disburse_btn)
    RelativeLayout layoutDisburseButton;

    @BindView(R.id.tv_alert_text_1)
    TextView tvAlertText1;

    @BindView(R.id.tv_alert_text_2)
    TextView tvAlertText2;

    @BindView(R.id.tv_customer_deposit_account)
    TextView tvCustomerDepositAccount;

    @BindView(R.id.tv_create_by)
    TextView tvCreatedBy;

    @BindView(R.id.tv_last_modified_by)
    TextView tvLastModifiedBy;

    @BindView(R.id.layout_error)
    View layoutError;

    @Inject
    CustomerLoanDetailsPresenter customerLoanDetailsPresenter;

    View rootView;

    private String productIdentifier;
    private String caseIdentifier;
    private LoanAccount loanAccount;
    private String[] weeksName, repayOnMonths, timeSlots, monthsName;
    private Menu menu;

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
        weeksName = getActivity().getResources().getStringArray(R.array.week_names);
        repayOnMonths = getActivity().getResources().getStringArray(R.array.repay_on_months);
        timeSlots = getActivity().getResources().getStringArray(R.array.time_slots);
        monthsName = getActivity().getResources().getStringArray(R.array.month_names);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customer_loan_details, container, false);
        ((FineractBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        initializeFineractUIErrorHandler(getActivity(), rootView);
        customerLoanDetailsPresenter.attachView(this);

        customerLoanDetailsPresenter.fetchCustomerLoanDetails(productIdentifier, caseIdentifier);

        return rootView;
    }

    @OnClick(R.id.btn_try_again)
    void tryAgainOnError() {
        ncvCustomerLoanDetails.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
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
        ncvCustomerLoanDetails.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
        setToolbarTitle(loanAccount.getIdentifier());

        tvPaymentAmount.setText(
                String.valueOf(loanAccount.getLoanParameters().getMaximumBalance()));
        String term = loanAccount.getLoanParameters().getTermRange().getMaximum() + " " +
                loanAccount.getLoanParameters().getTermRange().getTemporalUnit();
        tvTerm.setText(term);

        PaymentCycle paymentCycle = loanAccount.getLoanParameters().getPaymentCycle();
        LoanAccount.RepayUnitType repayUnitType =
                LoanAccount.RepayUnitType.valueOf(paymentCycle.getTemporalUnit());
        switch (repayUnitType) {
            case WEEKS:
                tvPaymentCycle.setText(
                        getString(R.string.payment_cycle_week, paymentCycle.getPeriod(),
                                paymentCycle.getTemporalUnit().toLowerCase(),
                                weeksName[paymentCycle.getAlignmentDay()]));
                break;
            case MONTHS:
                if (paymentCycle.getAlignmentWeek() == null) {
                    tvPaymentCycle.setText(
                            getString(R.string.payment_cycle_month_day, paymentCycle.getPeriod(),
                                    paymentCycle.getTemporalUnit().toLowerCase(),
                                    repayOnMonths[paymentCycle.getAlignmentDay()]));
                } else {
                    tvPaymentCycle.setText(
                            getString(R.string.payment_cycle_month_day_week,
                                    paymentCycle.getPeriod(),
                                    paymentCycle.getTemporalUnit().toLowerCase(),
                                    timeSlots[paymentCycle.getAlignmentWeek()],
                                    weeksName[paymentCycle.getAlignmentDay()]));
                }
                break;
            case YEARS:
                if (paymentCycle.getAlignmentWeek() == null) {
                    tvPaymentCycle.setText(
                            getString(R.string.payment_cycle_month_day, paymentCycle.getPeriod(),
                                    paymentCycle.getTemporalUnit().toLowerCase(),
                                    repayOnMonths[paymentCycle.getAlignmentDay()]));
                } else {
                    tvPaymentCycle.setText(
                            getString(R.string.payment_cycle_year_day_week,
                                    paymentCycle.getPeriod(),
                                    paymentCycle.getTemporalUnit().toLowerCase(),
                                    timeSlots[paymentCycle.getAlignmentWeek()],
                                    weeksName[paymentCycle.getAlignmentDay()],
                                    monthsName[paymentCycle.getAlignmentMonth()]));
                }
                break;
        }

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

        if (loanAccount.getCurrentState() == LoanAccount.State.APPROVED ||
                loanAccount.getCurrentState() == LoanAccount.State.CLOSED) {
            hideEditMenu(menu);
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
    public void showNoInternetConnection() {
        ncvCustomerLoanDetails.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        showFineractNoInternetUI();
    }

    @Override
    public void showError(String message) {
        ncvCustomerLoanDetails.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        showFineractErrorUI(getString(R.string.loan_account));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_loan_account_details, menu);
        Utils.setToolbarIconColor(getActivity(), menu, R.color.white);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void hideEditMenu(@NonNull Menu menu) {
        menu.findItem(R.id.menu_loan_account_edit).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_loan_account_edit:
                Intent intent = new Intent(getActivity(), LoanApplicationActivity.class)
                        .putExtra(ConstantKeys.CASE_IDENTIFIER, caseIdentifier)
                        .putExtra(ConstantKeys.LOAN_APPLICATION_ACTION, LoanApplicationAction.EDIT)
                        .putExtra(ConstantKeys.LOAN_ACCOUNT, loanAccount);
                startActivity(intent);
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
