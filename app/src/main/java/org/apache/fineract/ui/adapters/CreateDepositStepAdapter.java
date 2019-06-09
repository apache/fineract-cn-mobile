package org.apache.fineract.ui.adapters;

import android.content.Context;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import org.apache.fineract.R;
import org.apache.fineract.data.models.deposit.DepositAccount;
import org.apache.fineract.ui.online.depositaccounts.createdepositaccount.DepositAction;
import org.apache.fineract.ui.online.depositaccounts.createdepositaccount
        .FormDepositOverviewFragment;
import org.apache.fineract.ui.online.depositaccounts.createdepositaccount
        .formdepositassignproduct.FormDepositAssignProductFragment;

/**
 * @author Rajan Maurya
 *         On 13/08/17.
 */
public class CreateDepositStepAdapter  extends AbstractFragmentStepAdapter {

    private String[] createDepositSteps;
    private DepositAction depositAction;
    private DepositAccount depositAccounts;

    public CreateDepositStepAdapter(@NonNull FragmentManager fm, @NonNull Context context,
            DepositAction action, DepositAccount depositAccounts) {
        super(fm, context);
        createDepositSteps = context.getResources().getStringArray(R.array.create_deposit_steps);
        this.depositAction = action;
        this.depositAccounts = depositAccounts;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0L) int position) {
        return new StepViewModel.Builder(context)
                .setTitle(createDepositSteps[position])
                .create();
    }

    @Override
    public Step createStep(@IntRange(from = 0L) int position) {
        switch (position) {
            case 0:
                return FormDepositAssignProductFragment.newInstance(depositAction, depositAccounts);
            case 1:
                return FormDepositOverviewFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return createDepositSteps.length;
    }
}
