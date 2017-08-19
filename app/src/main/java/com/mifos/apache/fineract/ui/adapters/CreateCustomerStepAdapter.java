package com.mifos.apache.fineract.ui.adapters;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.customer.Customer;
import com.mifos.apache.fineract.ui.online.customers.createcustomer.CustomerAction;
import com.mifos.apache.fineract.ui.online.customers.createcustomer.formcustomeraddress.FormCustomerAddressFragment;
import com.mifos.apache.fineract.ui.online.customers.createcustomer.FormCustomerContactFragment;
import com.mifos.apache.fineract.ui.online.customers.createcustomer.FormCustomerDetailsFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * @author Rajan Maurya
 *         On 25/07/17.
 */
public class CreateCustomerStepAdapter extends AbstractFragmentStepAdapter {

    private String[] createCustomerSteps;
    private Customer customer;
    private CustomerAction customerAction;

    public CreateCustomerStepAdapter(@NonNull FragmentManager fm, @NonNull Context context,
            Customer customer, CustomerAction customerAction) {
        super(fm, context);
        this.customer = customer;
        this.customerAction = customerAction;
        createCustomerSteps = context.getResources().getStringArray(R.array.create_customer_steps);
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
                return FormCustomerDetailsFragment.newInstance(customer, customerAction);
            case 1:
                return FormCustomerAddressFragment.newInstance(customer, customerAction);
            case 2:
                return FormCustomerContactFragment.newInstance(customer, customerAction);

        }
        return null;
    }

    @Override
    public int getCount() {
        return createCustomerSteps.length;
    }
}
