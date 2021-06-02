package org.apache.fineract.ui.online.loanaccounts.loanapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.apache.fineract.R;
import org.apache.fineract.data.models.loan.CreditWorthinessFactor;
import org.apache.fineract.data.models.loan.CreditWorthinessSnapshot;
import org.apache.fineract.data.models.loan.LoanAccount;
import org.apache.fineract.data.models.loan.LoanParameters;
import org.apache.fineract.utils.ConstantKeys;

import java.util.Objects;

/**
 * @author Rajan Maurya
 * On 24/07/17.
 */
public class LoanDebtIncomeFragment extends BaseFragmentDebtIncome implements Step {

    private OnNavigationBarListener.LoanDebtIncomeData onNavigationBarListener;
    private LoanApplicationAction loanApplicationAction;
    private LoanAccount loanAccount;
    private LoanParameters loanParameters;

    public static LoanDebtIncomeFragment newInstance(
            LoanAccount loanAccount,
            LoanApplicationAction loanApplicationAction) {
        LoanDebtIncomeFragment fragment = new LoanDebtIncomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ConstantKeys.LOAN_APPLICATION_ACTION, loanApplicationAction);
        args.putParcelable(ConstantKeys.LOAN_ACCOUNT, loanAccount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loanApplicationAction = (LoanApplicationAction) getArguments()
                .getSerializable(ConstantKeys.LOAN_APPLICATION_ACTION);

        if (loanApplicationAction == LoanApplicationAction.EDIT) {
            loanAccount = getArguments().getParcelable(ConstantKeys.LOAN_ACCOUNT);
            loanParameters = new Gson().fromJson(loanAccount.getParameters(), LoanParameters.class);
            for (CreditWorthinessSnapshot snapshot
                    : Objects.requireNonNull(loanParameters.getCreditWorthinessSnapshots())) {
                for (CreditWorthinessFactor debt : snapshot.getDebts()) {
                    addDebt(debt);
                }

                for (CreditWorthinessFactor income : snapshot.getIncomeSources()) {
                    addDebt(income);
                }
            }

        }

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_loan_debt_income_ratio;
    }

    @Override
    public VerificationError verifyStep() {
        onNavigationBarListener.setDebtIncome(getCreditWorthinessSnapshot());
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = context instanceof Activity ? (Activity) context : null;
        try {
            onNavigationBarListener = (OnNavigationBarListener.LoanDebtIncomeData) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNavigationBarListener");
        }
    }
}
