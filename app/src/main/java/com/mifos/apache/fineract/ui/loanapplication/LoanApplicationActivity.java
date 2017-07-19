package com.mifos.apache.fineract.ui.loanapplication;

import android.os.Bundle;
import android.view.View;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.ui.adapters.LoanApplicationStepAdapter;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 17/07/17.
 */
public class LoanApplicationActivity extends MifosBaseActivity
        implements  StepperLayout.StepperListener, OnNavigationBarListener{

    private static final String CURRENT_STEP_POSITION = "position";

    @BindView(R.id.stepperLayout)
    StepperLayout stepperLayout;

    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(CURRENT_STEP_POSITION);
        }
        stepperLayout.setAdapter(new LoanApplicationStepAdapter(getSupportFragmentManager(), this), currentPosition);
        stepperLayout.setListener(this);
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
    public void onChangeEndButtonsEnabled(boolean enable) {

    }
}
