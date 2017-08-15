package com.mifos.apache.fineract.ui.adapters;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.deposit.DepositAccount;
import com.mifos.apache.fineract.ui.online.depositaccounts.createdepositaccount.DepositAction;
import com.mifos.apache.fineract.ui.online.depositaccounts.createdepositaccount.formdepositassignproduct.FormDepositAssignProductFragment;
import com.mifos.apache.fineract.ui.online.depositaccounts.createdepositaccount.FormDepositOverviewFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

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
