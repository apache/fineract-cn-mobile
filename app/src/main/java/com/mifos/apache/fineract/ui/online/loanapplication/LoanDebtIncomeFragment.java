package com.mifos.apache.fineract.ui.online.loanapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.mifos.apache.fineract.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

/**
 * @author Rajan Maurya
 *         On 24/07/17.
 */
public class LoanDebtIncomeFragment extends BaseFragmentDebtIncome implements Step {

    private OnNavigationBarListener.LoanDebtIncomeData onNavigationBarListener;

    public static LoanDebtIncomeFragment newInstance() {
        LoanDebtIncomeFragment fragment = new LoanDebtIncomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
