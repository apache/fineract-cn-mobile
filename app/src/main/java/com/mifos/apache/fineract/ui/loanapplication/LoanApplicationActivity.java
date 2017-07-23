package com.mifos.apache.fineract.ui.loanapplication;

import android.os.Bundle;
import android.view.View;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.loan.CreditWorthinessFactor;
import com.mifos.apache.fineract.data.models.loan.PaymentCycle;
import com.mifos.apache.fineract.data.models.loan.TermRange;
import com.mifos.apache.fineract.ui.adapters.LoanApplicationStepAdapter;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.Toaster;
import com.mifos.apache.fineract.utils.ConstantKeys;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 17/07/17.
 */
public class LoanApplicationActivity extends MifosBaseActivity
        implements StepperLayout.StepperListener, OnNavigationBarListener.LoanDetailsData,
        OnNavigationBarListener.LoanDebtIncomeData {

    private static final String CURRENT_STEP_POSITION = "position";

    @BindView(R.id.stepperLayout)
    StepperLayout stepperLayout;

    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);
        ButterKnife.bind(this);

        String customerIdentifier = getIntent().getExtras().getString(
                ConstantKeys.CUSTOMER_IDENTIFIER);

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
        // called when step will be completed
    }

    @Override
    public void onError(VerificationError verificationError) {
        if (verificationError.getErrorMessage() != null) {
            Toaster.show(findViewById(android.R.id.content), verificationError.getErrorMessage());
        }
        // If any condition failed during the verification, show error to user or change UI
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {
        finish();
    }

    @Override
    public void setLoanDetails(String currentState, String identifier, String productIdentifier,
            Double maximumBalance, PaymentCycle paymentCycle, TermRange termRange) {

    }

    @Override
    public void setDebtIncome(List<CreditWorthinessFactor> debts,
            List<CreditWorthinessFactor> income) {

    }
}
