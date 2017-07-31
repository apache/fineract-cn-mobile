package com.mifos.apache.fineract.ui.online.identification.createidentification.identificationactivity;


import android.os.Bundle;
import android.view.View;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.customer.identification.Identification;
import com.mifos.apache.fineract.ui.adapters.CreateIdentificationStepAdapter;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.Toaster;
import com.mifos.apache.fineract.ui.online.identification.createidentification
        .OnNavigationBarListener;
import com.mifos.apache.fineract.ui.online.identification.createidentification.OverViewContract;
import com.mifos.apache.fineract.utils.ConstantKeys;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 31/07/17.
 */
public class CreateIdentificationActivity extends MifosBaseActivity
        implements StepperLayout.StepperListener, CreateIdentificationContract.View,
        OnNavigationBarListener.IdentificationCard {

    private static final String CURRENT_STEP_POSITION = "position";
    private static final String LOG_TAG = CreateIdentificationActivity.class.getSimpleName();

    @BindView(R.id.stepperLayout)
    StepperLayout stepperLayout;

    @Inject
    CreateIdentificationPresenter createIdentificationPresenter;

    private int currentPosition = 0;

    private Identification identification;
    private CreateIdentificationStepAdapter stepAdapter;
    private String customerIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_create_identification);
        ButterKnife.bind(this);
        createIdentificationPresenter.attachView(this);
        identification = new Identification();

        customerIdentifier = getIntent().getExtras().getString(ConstantKeys.CUSTOMER_IDENTIFIER);

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(CURRENT_STEP_POSITION);
        }

        stepAdapter = new CreateIdentificationStepAdapter(
                getSupportFragmentManager(), this);
        stepperLayout.setAdapter(stepAdapter, currentPosition);
        stepperLayout.setListener(this);
        stepperLayout.setOffscreenPageLimit(stepAdapter.getCount());
        setToolbarTitle(getString(R.string.create_new_identification));

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
        createIdentificationPresenter.createIdentification(customerIdentifier, identification);
        stepperLayout.setNextButtonEnabled(false);
        stepperLayout.setBackButtonEnabled(false);
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
    public void identificationCreatedSuccessfully() {
        finish();
    }

    @Override
    public void setIdentificationDetails(Identification identificationDetails) {
        identification = identificationDetails;
        ((OverViewContract) stepAdapter.findStep(1)).setIdentification(identificationDetails);
    }

    @Override
    public void showProgressbar() {
        stepperLayout.showProgress(getString(R.string.creating_identification_card_please_wait));
    }

    @Override
    public void hideProgressbar() {
        stepperLayout.hideProgress();
    }

    @Override
    public void showError(String message) {
        stepperLayout.setNextButtonEnabled(true);
        stepperLayout.setBackButtonEnabled(true);
        Toaster.show(findViewById(android.R.id.content), message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stepperLayout.hideProgress();
        createIdentificationPresenter.detachView();
    }

}
