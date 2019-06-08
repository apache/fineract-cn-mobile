package org.apache.fineract.ui.adapters;

import android.content.Context;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import org.apache.fineract.R;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.LoanDebtIncomeFragment;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.loancosigner.LoanCoSignerFragment;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.loandetails.LoanDetailsFragment;
import org.apache.fineract.ui.online.review.AddLoanReviewFragment;

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
            case 3:
                return AddLoanReviewFragment.Companion.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return loanApplicationSteps.length;
    }
}
