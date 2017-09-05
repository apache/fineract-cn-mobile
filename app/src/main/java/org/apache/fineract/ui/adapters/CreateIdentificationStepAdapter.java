package org.apache.fineract.ui.adapters;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.identification.Identification;
import org.apache.fineract.ui.online.identification.createidentification.Action;
import org.apache.fineract.ui.online.identification.createidentification
        .FormIdentificationDetailsFragment;
import org.apache.fineract.ui.online.identification.createidentification
        .FormOverViewIdentificationFragment;

/**
 * @author Rajan Maurya
 *         On 31/07/17.
 */
public class CreateIdentificationStepAdapter extends AbstractFragmentStepAdapter {

    private String[] createCustomerSteps;
    private Action action;
    private Identification identification;

    public CreateIdentificationStepAdapter(@NonNull FragmentManager fm, @NonNull Context context,
            Action action, Identification identification) {
        super(fm, context);
        createCustomerSteps =
                context.getResources().getStringArray(R.array.create_identification_steps);
        this.action = action;
        this.identification = identification;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0L) int position) {
        return new StepViewModel.Builder(context)
                .setTitle(createCustomerSteps[position])
                .create();
    }

    @Override
    public Step createStep(@IntRange(from = 0L) int position) {
        switch (position) {
            case 0:
                return FormIdentificationDetailsFragment.newInstance(action, identification);
            case 1:
                return FormOverViewIdentificationFragment.newInstance();

        }
        return null;
    }

    @Override
    public int getCount() {
        return createCustomerSteps.length;
    }
}
