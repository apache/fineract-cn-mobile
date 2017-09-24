package org.apache.fineract.ui.online.customers.createcustomer.customeractivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.Address;
import org.apache.fineract.data.models.customer.ContactDetail;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.data.models.customer.DateOfBirth;
import org.apache.fineract.ui.adapters.CreateCustomerStepAdapter;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.Toaster;
import org.apache.fineract.ui.online.customers.createcustomer.CustomerAction;
import org.apache.fineract.ui.online.customers.createcustomer.OnNavigationBarListener;
import org.apache.fineract.ui.online.customers.customerdetails.CustomerDetailsActivity;
import org.apache.fineract.utils.ConstantKeys;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 25/07/17.
 */
public class CreateCustomerActivity extends FineractBaseActivity
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
    private CustomerAction customerAction;
    private String customerIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_create_customer);
        ButterKnife.bind(this);
        createCustomerPresenter.attachView(this);

        customerAction = (CustomerAction) getIntent().getSerializableExtra(
                ConstantKeys.CUSTOMER_ACTION);
        customer = getIntent().getExtras().getParcelable(ConstantKeys.CUSTOMER);
        customerIdentifier = getIntent().getExtras().getString(ConstantKeys.CUSTOMER_IDENTIFIER);

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(CURRENT_STEP_POSITION);
        }

        CreateCustomerStepAdapter stepAdapter = new CreateCustomerStepAdapter(
                getSupportFragmentManager(), this, customer, customerAction);
        stepperLayout.setAdapter(stepAdapter, currentPosition);
        stepperLayout.setListener(this);
        stepperLayout.setOffscreenPageLimit(stepAdapter.getCount());

        switch (customerAction) {
            case CREATE:
                customer = new Customer();
                setToolbarTitle(getString(R.string.create_customer));
                break;
            case EDIT:
                setToolbarTitle(getString(R.string.edit_customer));
                break;
        }

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
        switch (customerAction) {
            case CREATE:
                createCustomerPresenter.createCustomer(customer);
                break;
            case EDIT:
                createCustomerPresenter.updateCustomer(customerIdentifier, customer);
                break;
        }
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
        Toast.makeText(this, getString(R.string.customer_created_successfully,
                customer.getGivenName()), Toast.LENGTH_LONG).show();
        Intent customerDetailsIntent = new Intent(this, CustomerDetailsActivity.class);
        customerDetailsIntent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER, customer.getIdentifier());
        startActivity(customerDetailsIntent);
        finish();
    }

    @Override
    public void customerUpdatedSuccessfully() {
        Toast.makeText(this, getString(R.string.customer_updated_successfully,
                customer.getGivenName()), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void showProgressbar() {
        switch (customerAction) {
            case CREATE:
                stepperLayout.showProgress(getString(R.string.creating_customer_please_wait));
                break;
            case EDIT:
                stepperLayout.showProgress(getString(R.string.updating_customer_please_wait));
                break;
        }
    }

    @Override
    public void hideProgressbar() {
        stepperLayout.hideProgress();
    }

    @Override
    public void showNoInternetConnection() {
        stepperLayout.setNextButtonEnabled(true);
        Toaster.show(findViewById(android.R.id.content),
                getString(R.string.no_internet_connection));
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
