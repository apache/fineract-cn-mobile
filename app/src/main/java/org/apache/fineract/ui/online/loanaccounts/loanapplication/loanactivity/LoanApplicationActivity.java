package org.apache.fineract.ui.online.loanaccounts.loanapplication.loanactivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.apache.fineract.R;
import org.apache.fineract.data.models.loan.CreditWorthinessSnapshot;
import org.apache.fineract.data.models.loan.LoanAccount;
import org.apache.fineract.data.models.loan.LoanParameters;
import org.apache.fineract.data.models.loan.PaymentCycle;
import org.apache.fineract.data.models.loan.TermRange;
import org.apache.fineract.ui.adapters.LoanApplicationStepAdapter;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.Toaster;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.LoanApplicationAction;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.OnNavigationBarListener;
import org.apache.fineract.utils.ConstantKeys;
import org.apache.fineract.utils.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 * On 17/07/17.
 */
public class LoanApplicationActivity extends FineractBaseActivity
        implements StepperLayout.StepperListener, OnNavigationBarListener.LoanDetailsData,
        OnNavigationBarListener.LoanDebtIncomeData, OnNavigationBarListener.LoanCoSignerData,
        LoanApplicationContract.View, OnNavigationBarListener.ReviewLoan {

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
    private String selectedProduct;
    private LoanApplicationAction loanApplicationAction;
    private String caseIdentifier;

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
        loanApplicationAction = (LoanApplicationAction) getIntent()
                .getSerializableExtra(ConstantKeys.LOAN_APPLICATION_ACTION);
        customerIdentifier = getIntent().getExtras().getString(ConstantKeys.CUSTOMER_IDENTIFIER);
        switch (loanApplicationAction) {
            case CREATE:
                setToolbarTitle(getString(R.string.create_new_loan));
                break;
            case EDIT:
                setToolbarTitle(getString(R.string.edit_loan));
                caseIdentifier = getIntent().getExtras().getString(ConstantKeys.CASE_IDENTIFIER);
                loanAccount = getIntent().getExtras().getParcelable(ConstantKeys.LOAN_ACCOUNT);
                if (loanAccount != null) {
                    loanParameters = loanAccount.getLoanParameters();
                }
        }

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(CURRENT_STEP_POSITION);
        }
        LoanApplicationStepAdapter stepAdapter = new LoanApplicationStepAdapter(
                getSupportFragmentManager(), this, loanAccount, loanApplicationAction);
        stepperLayout.setAdapter(stepAdapter, currentPosition);
        stepperLayout.setListener(this);
        stepperLayout.setOffscreenPageLimit(stepAdapter.getCount());

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
        loanParameters.setCreditWorthinessSnapshots(creditWorthinessSnapshots);
        switch (loanApplicationAction) {
            case CREATE:
                loanApplicationPresenter.createLoan(
                        loanAccount.getProductIdentifier(),
                        loanAccount);
            case EDIT:
                loanApplicationPresenter.updateLoan(
                        loanAccount.getProductIdentifier(),
                        loanAccount,
                        caseIdentifier);
        }

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
    public void applicationUpdatedSuccessfully() {
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
    public void showNoInternetConnection() {
        Toaster.show(findViewById(android.R.id.content),
                getString(R.string.no_internet_connection));
    }

    @Override
    public void showError(String message) {
        Toaster.show(findViewById(android.R.id.content), message);
    }

    @Override
    public void setLoanDetails(LoanAccount.State currentState,
                               String identifier,
                               String productIdentifier,
                               Double maximumBalance,
                               PaymentCycle paymentCycle,
                               TermRange termRange, String selectedProduct) {
        this.selectedProduct = selectedProduct;

        loanAccount.setCurrentState(currentState);
        loanAccount.setIdentifier(identifier);
        loanAccount.setProductIdentifier(productIdentifier);

        loanParameters.setCustomerIdentifier(customerIdentifier);
        loanParameters.setMaximumBalance(maximumBalance);
        loanParameters.setPaymentCycle(paymentCycle);
        loanParameters.setTermRange(termRange);
        loanAccount.setParameters(new Gson().toJson(loanParameters));
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
    public LoanAccount getLoanAccount() {
        return loanAccount;
    }

    @Override
    public String getSelectedProduct() {
        return selectedProduct;
    }

    @Override
    public List<CreditWorthinessSnapshot> getCreditWorthinessSnapshot() {
        return creditWorthinessSnapshots;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressbar();
        loanApplicationPresenter.detachView();
    }
}
