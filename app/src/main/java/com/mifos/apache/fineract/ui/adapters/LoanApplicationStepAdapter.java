package com.mifos.apache.fineract.ui.adapters;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.ui.online.loans.loanapplication.LoanDebtIncomeFragment;
import com.mifos.apache.fineract.ui.online.loans.loanapplication.loancosigner.LoanCoSignerFragment;
import com.mifos.apache.fineract.ui.online.loans.loanapplication.loandetails.LoanDetailsFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * @author Rajan Maurya
 *         On 17/07/17.
 */
public class LoanApplicationStepAdapter extends AbstractFragmentStepAdapter {

    private String[] loanApplicationSteps;

    public LoanApplicationStepAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
        loanApplicationSteps = context.getResources().getStringArray(
                R.array.loan_application_steps);
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0L) int position) {
        return new StepViewModel.Builder(context)
                .setTitle(loanApplicationSteps[position])
                .create();
    }

    @Override
    public Step createStep(@IntRange(from = 0L) int position) {
        switch (position) {
            case 0:
                return LoanDetailsFragment.newInstance();
            case 1:
                return LoanDebtIncomeFragment.newInstance();
            case 2:
                return LoanCoSignerFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return loanApplicationSteps.length;
    }
}
