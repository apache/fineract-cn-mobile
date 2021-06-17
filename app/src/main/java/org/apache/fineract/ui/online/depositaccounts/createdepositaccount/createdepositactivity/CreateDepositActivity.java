package org.apache.fineract.ui.online.depositaccounts.createdepositaccount.createdepositactivity;

import android.os.Bundle;
import android.view.View;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.apache.fineract.R;
import org.apache.fineract.data.models.deposit.DepositAccount;
import org.apache.fineract.ui.adapters.CreateDepositStepAdapter;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.Toaster;
import org.apache.fineract.ui.online.depositaccounts.createdepositaccount.DepositAction;
import org.apache.fineract.ui.online.depositaccounts.createdepositaccount
        .DepositOnNavigationBarListener;
import org.apache.fineract.ui.online.depositaccounts.createdepositaccount.DepositOverViewContract;
import org.apache.fineract.utils.ConstantKeys;
import org.apache.fineract.utils.MaterialDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 13/08/17.
 */
public class CreateDepositActivity extends FineractBaseActivity implements
        StepperLayout.StepperListener, DepositOnNavigationBarListener.ProductInstanceDetails,
        CreateDepositContract.View {

    private static final String CURRENT_STEP_POSITION = "position";
    private static final String LOG_TAG = CreateDepositActivity.class.getSimpleName();

    @BindView(R.id.stepperLayout)
    StepperLayout stepperLayout;

    @Inject
    CreateDepositPresenter createDepositPresenter;

    private int currentPosition = 0;

    private String customerIdentifier;
    private DepositAction depositAction;
    private DepositAccount depositAccount;
    private CreateDepositStepAdapter stepAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deposit);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        createDepositPresenter.attachView(this);

        customerIdentifier = getIntent().getExtras().getString(ConstantKeys.CUSTOMER_IDENTIFIER);
        depositAction = (DepositAction) getIntent().getSerializableExtra(
                ConstantKeys.DEPOSIT_ACTION);
        depositAccount = getIntent().getExtras().getParcelable(ConstantKeys.DEPOSIT_ACCOUNT);

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(CURRENT_STEP_POSITION);
        }

        stepAdapter = new CreateDepositStepAdapter(
                getSupportFragmentManager(), this, depositAction, depositAccount);
        stepperLayout.setAdapter(stepAdapter, currentPosition);
        stepperLayout.setListener(this);
        stepperLayout.setOffscreenPageLimit(stepAdapter.getCount());

        switch (depositAction) {
            case CREATE:
                setToolbarTitle(getString(R.string.create_new_deposit));
                break;
            case EDIT:
                setToolbarTitle(getString(R.string.update_deposit));
                break;
        }

        showBackButton();
    }

    @Override
    public void onBackPressed() {
        if (stepperLayout.getCurrentStepPosition() != 0) {
            stepperLayout.setCurrentStepPosition(
                    stepperLayout.getCurrentStepPosition() - 1);
        } else {
            new MaterialDialog.Builder()
                    .init(this)
                    .setTitle(getString(R.string.dialog_title_confirm_exit))
                    .setMessage(getString(
                            R.string.dialog_message_confirmation_exit_create_edit_activity))
                    .setPositiveButton(getString(R.string.dialog_action_exit),
                            (dialog, which) -> {
                                super.onBackPressed();
                            })
                    .setNegativeButton(getString(R.string.dialog_action_cancel))
                    .createMaterialDialog()
                    .show();
        }
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
        switch (depositAction) {
            case CREATE:
                createDepositPresenter.createDepositAccount(depositAccount);
                break;
            case EDIT:
                createDepositPresenter.updateDepositAccount(depositAccount.getAccountIdentifier(),
                        depositAccount);
                break;
        }
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
    public void setProductInstance(DepositAccount depositAccount, String productName) {
        depositAccount.setCustomerIdentifier(customerIdentifier);
        this.depositAccount = depositAccount;
        ((DepositOverViewContract) stepAdapter.findStep(1))
                .setProductInstance(depositAccount, productName, depositAction);
    }

    @Override
    public void depositCreatedSuccessfully() {
        finish();
    }

    @Override
    public void depositUpdatedSuccessfully() {
        finish();
    }

    @Override
    public void showProgressbar() {
        switch (depositAction) {
            case CREATE:
                stepperLayout.showProgress(getString(R.string.creating_deposit_account));
                break;
            case EDIT:
                stepperLayout.showProgress(getString(R.string.updating_deposit_account));
                break;
        }
    }

    @Override
    public void hideProgressbar() {
        stepperLayout.hideProgress();
    }

    @Override
    public void showNoInternetConnection() {
        stepperLayout.setNextButtonEnabled(true);
        stepperLayout.setBackButtonEnabled(true);
        Toaster.show(findViewById(android.R.id.content),
                getString(R.string.no_internet_connection));
    }

    @Override
    public void showError(String message) {
        stepperLayout.setNextButtonEnabled(true);
        stepperLayout.setBackButtonEnabled(true);
        Toaster.show(findViewById(android.R.id.content), message);
    }
}
