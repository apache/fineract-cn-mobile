package org.apache.fineract.ui.adapters;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import org.apache.fineract.R;
import org.apache.fineract.data.models.loan.LoanAccount;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.LoanApplicationAction;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.LoanDebtIncomeFragment;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.loancosigner.LoanCoSignerFragment;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.loandetails.LoanDetailsFragment;
import org.apache.fineract.ui.online.review.AddLoanReviewFragment;

/**
 * @author Rajan Maurya
 * On 17/07/17.
 */
public class LoanApplicationStepAdapter extends AbstractFragmentStepAdapter {

    private String[] loanApplicationSteps;
    private LoanAccount loanAccount;
    private LoanApplicationAction loanApplicationAction;

    public LoanApplicationStepAdapter(@NonNull FragmentManager fm,
                                      @NonNull Context context,
                                      @NonNull LoanAccount loanAccount,
                                      @NonNull LoanApplicationAction loanApplicationAction) {
        super(fm, context);
        loanApplicationSteps = context.getResources().getStringArray(
                R.array.loan_application_steps);
        this.loanAccount = loanAccount;
        this.loanApplicationAction = loanApplicationAction;
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
                return LoanDetailsFragment.newInstance(loanAccount, loanApplicationAction);
            case 1:
                return LoanDebtIncomeFragment.newInstance(loanAccount, loanApplicationAction);
            case 2:
                return LoanCoSignerFragment.newInstance(loanAccount, loanApplicationAction);
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
