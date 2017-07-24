package com.mifos.apache.fineract.ui.loanapplication.loanactivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.loan.CreditWorthinessSnapshot;
import com.mifos.apache.fineract.data.models.loan.LoanAccount;
import com.mifos.apache.fineract.data.models.loan.LoanParameters;
import com.mifos.apache.fineract.data.models.loan.PaymentCycle;
import com.mifos.apache.fineract.data.models.loan.TermRange;
import com.mifos.apache.fineract.ui.adapters.LoanApplicationStepAdapter;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.Toaster;
import com.mifos.apache.fineract.ui.loanapplication.OnNavigationBarListener;
import com.mifos.apache.fineract.utils.ConstantKeys;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 17/07/17.
 */
public class LoanApplicationActivity extends MifosBaseActivity
        implements StepperLayout.StepperListener, OnNavigationBarListener.LoanDetailsData,
        OnNavigationBarListener.LoanDebtIncomeData, OnNavigationBarListener.LoanCoSignerData,
        LoanApplicationContract.View {

    private static final String CURRENT_STEP_POSITION = "position";
    private static final String LOG_TAG = LoanApplicationActivity.class.getSimpleName();

    @BindView(R.id.stepperLayout)
    StepperLayout stepperLayout;

    @Inject
    LoanApplicationPresenter loanApplicationPresenter;

    private int currentPosition = 0;

    private LoanAccount loanAccount;
    private List<CreditWorthinessSnapshot> creditWorthinessSnapshots;
    private String customerIdentifier;
    private LoanParameters loanParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_loan_application);
        ButterKnife.bind(this);
        loanApplicationPresenter.attachView(this);
        creditWorthinessSnapshots = new ArrayList<>();
        loanAccount = new LoanAccount();
        loanParameters = new LoanParameters();

        customerIdentifier = getIntent().getExtras().getString(ConstantKeys.CUSTOMER_IDENTIFIER);

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(CURRENT_STEP_POSITION);
        }
        LoanApplicationStepAdapter stepAdapter = new LoanApplicationStepAdapter(
                getSupportFragmentManager(), this);
        stepperLayout.setAdapter(stepAdapter, currentPosition);
        stepperLayout.setListener(this);
        stepperLayout.setOffscreenPageLimit(stepAdapter.getCount());
        setToolbarTitle("Create new loan");
        showBackButton();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_STEP_POSITION, stepperLayout.getCurrentStepPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentPosition = savedInstanceState.getInt(CURRENT_STEP_POSITION);
    }

    @Override
    public void onCompleted(View completeButton) {
        loanParameters.setCreditWorthinessSnapshots(creditWorthinessSnapshots);
        loanAccount.setParameters(new Gson().toJson(loanParameters));
        loanApplicationPresenter.createLoan(loanAccount.getProductIdentifier(), loanAccount);
    }

    @Override
    public void onError(VerificationError verificationError) {
        if (verificationError.getErrorMessage() != null) {
            Toaster.show(findViewById(android.R.id.content), verificationError.getErrorMessage());
        }
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {
        finish();
    }

    @Override
    public void applicationCreatedSuccessfully() {
        finish();
    }

    @Override
    public void showProgressbar(String message) {
        stepperLayout.showProgress(message);
    }

    public void hideProgressbar() {
        stepperLayout.hideProgress();
    }

    @Override
    public void showError(String message) {
        Toaster.show(findViewById(android.R.id.content), message);
    }

    @Override
    public void setLoanDetails(LoanAccount.State currentState, String identifier,
            String productIdentifier, Double maximumBalance, PaymentCycle paymentCycle,
            TermRange termRange) {
        loanAccount.setCurrentState(currentState);
        loanAccount.setIdentifier(identifier);
        loanAccount.setProductIdentifier(productIdentifier);

        loanParameters.setCustomerIdentifier(customerIdentifier);
        loanParameters.setMaximumBalance(maximumBalance);
        loanParameters.setPaymentCycle(paymentCycle);
        loanParameters.setTermRange(termRange);
    }

    @Override
    public void setDebtIncome(CreditWorthinessSnapshot debtIncome) {
        debtIncome.setForCustomer(customerIdentifier);
        creditWorthinessSnapshots.add(debtIncome);
    }

    @Override
    public void setCoSignerDebtIncome(CreditWorthinessSnapshot coSignerDebtIncome) {
        if (TextUtils.isEmpty(coSignerDebtIncome.getForCustomer())) {
            coSignerDebtIncome.setForCustomer(customerIdentifier);
        }
        creditWorthinessSnapshots.add(coSignerDebtIncome);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressbar();
        loanApplicationPresenter.detachView();
    }
}
