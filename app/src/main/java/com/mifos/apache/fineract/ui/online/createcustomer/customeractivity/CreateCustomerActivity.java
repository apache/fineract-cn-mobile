package com.mifos.apache.fineract.ui.online.createcustomer.customeractivity;

import android.os.Bundle;
import android.view.View;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.customer.Address;
import com.mifos.apache.fineract.data.models.customer.ContactDetail;
import com.mifos.apache.fineract.data.models.customer.Customer;
import com.mifos.apache.fineract.data.models.customer.DateOfBirth;
import com.mifos.apache.fineract.ui.adapters.CreateCustomerStepAdapter;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.Toaster;
import com.mifos.apache.fineract.ui.online.createcustomer.OnNavigationBarListener;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 25/07/17.
 */
public class CreateCustomerActivity extends MifosBaseActivity
        implements StepperLayout.StepperListener, OnNavigationBarListener.CustomerDetails,
        OnNavigationBarListener.CustomerAddress, CreateCustomerContract.View,
        OnNavigationBarListener.CustomerContactDetails {

    private static final String CURRENT_STEP_POSITION = "position";
    private static final String LOG_TAG = CreateCustomerActivity.class.getSimpleName();

    @BindView(R.id.stepperLayout)
    StepperLayout stepperLayout;

    @Inject
    CreateCustomerPresenter createCustomerPresenter;

    private int currentPosition = 0;

    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_create_customer);
        ButterKnife.bind(this);
        createCustomerPresenter.attachView(this);
        customer = new Customer();

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(CURRENT_STEP_POSITION);
        }
        CreateCustomerStepAdapter stepAdapter = new CreateCustomerStepAdapter(
                getSupportFragmentManager(), this);
        stepperLayout.setAdapter(stepAdapter, currentPosition);
        stepperLayout.setListener(this);
        stepperLayout.setOffscreenPageLimit(stepAdapter.getCount());
        setToolbarTitle(getString(R.string.create_customer));
        showBackButton();
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
        stepperLayout.setNextButtonEnabled(false);
        customer.setType(Customer.Type.PERSON.name());
        createCustomerPresenter.createCustomer(customer);
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
    public void setCustomerDetails(String identifier, String firstName, String surname,
            String middleName, DateOfBirth dateOfBirth, boolean isMember) {
        customer.setIdentifier(identifier);
        customer.setGivenName(firstName);
        customer.setSurname(surname);
        customer.setMiddleName(middleName);
        customer.setDateOfBirth(dateOfBirth);
        customer.setMember(isMember);
    }

    @Override
    public void setAddress(Address address) {
        customer.setAddress(address);
    }

    @Override
    public void setContactDetails(List<ContactDetail> contactDetails) {
        customer.setContactDetails(contactDetails);
    }

    @Override
    public void customerCreatedSuccessfully() {
        finish();
    }

    @Override
    public void showProgressbar() {
        stepperLayout.showProgress(getString(R.string.creating_customer_please_wait));
    }

    @Override
    public void hideProgressbar() {
        stepperLayout.hideProgress();
    }

    @Override
    public void showError(String message) {
        stepperLayout.setNextButtonEnabled(true);
        Toaster.show(findViewById(android.R.id.content), message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stepperLayout.hideProgress();
        createCustomerPresenter.detachView();
    }
}
