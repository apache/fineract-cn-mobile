package org.apache.fineract.ui.online.customers.createcustomer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.ContactDetail;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.utils.ConstantKeys;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 25/07/17.
 */
public class FormCustomerContactFragment extends FineractBaseFragment implements Step, TextWatcher {

    @BindView(R.id.til_email)
    TextInputLayout tilEmail;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.et_phone_number)
    EditText etPhone;

    @BindView(R.id.et_mobile)
    EditText etMobile;

    View rootView;

    private Customer customer;
    private CustomerAction customerAction;

    private OnNavigationBarListener.CustomerContactDetails onNavigationBarListener;

    public static FormCustomerContactFragment newInstance(Customer customer,
            CustomerAction action) {
        FormCustomerContactFragment fragment = new FormCustomerContactFragment();
        Bundle args = new Bundle();
        args.putParcelable(ConstantKeys.CUSTOMER, customer);
        args.putSerializable(ConstantKeys.CUSTOMER_ACTION, action);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customer = getArguments().getParcelable(ConstantKeys.CUSTOMER);
            customerAction = (CustomerAction) getArguments().getSerializable(
                    ConstantKeys.CUSTOMER_ACTION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_form_customer_contact, container, false);
        ButterKnife.bind(this, rootView);
        etEmail.addTextChangedListener(this);

        if (customerAction == CustomerAction.EDIT) {
            showPreviousContactDetails();
        }

        return rootView;
    }

    public void showPreviousContactDetails() {
        if (customer.getContactDetails().size() != 0) {
            for (ContactDetail contactDetail : customer.getContactDetails()) {
                showContactDetails(contactDetail);
            }
        }
    }

    public void showContactDetails(ContactDetail contactDetail) {
        switch (contactDetail.getType()) {
            case EMAIL:
                etEmail.setText(contactDetail.getValue());
                break;
            case MOBILE:
                etMobile.setText(contactDetail.getValue());
                break;
            case PHONE:
                etPhone.setText(contactDetail.getValue());
                break;
        }
    }

    @Override
    public VerificationError verifyStep() {
        if (!validateEmail()) {
            return new VerificationError(null);
        } else {

            List<ContactDetail> contactDetails = new ArrayList<>();

            if (!TextUtils.isEmpty(etEmail.getText().toString().trim())) {
                ContactDetail emailContactDetail = new ContactDetail();
                emailContactDetail.setGroup(ContactDetail.Group.BUSINESS);
                emailContactDetail.setType(ContactDetail.Type.EMAIL);
                emailContactDetail.setPreferenceLevel(1);
                emailContactDetail.setValue(etEmail.getText().toString().trim());
                contactDetails.add(emailContactDetail);
            }

            if (!TextUtils.isEmpty(etPhone.getText().toString().trim())) {
                ContactDetail phoneContactDetail = new ContactDetail();
                phoneContactDetail.setGroup(ContactDetail.Group.BUSINESS);
                phoneContactDetail.setType(ContactDetail.Type.PHONE);
                phoneContactDetail.setPreferenceLevel(1);
                phoneContactDetail.setValue(etPhone.getText().toString().trim());
                contactDetails.add(phoneContactDetail);
            }

            if (!TextUtils.isEmpty(etMobile.getText().toString().trim())) {
                ContactDetail mobileContactDetail = new ContactDetail();
                mobileContactDetail.setGroup(ContactDetail.Group.BUSINESS);
                mobileContactDetail.setType(ContactDetail.Type.MOBILE);
                mobileContactDetail.setPreferenceLevel(1);
                mobileContactDetail.setValue(etMobile.getText().toString().trim());
                contactDetails.add(mobileContactDetail);
            }

            onNavigationBarListener.setContactDetails(contactDetails);
        }
        return null;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if ((etEmail.getText().hashCode() == s.hashCode())) {
            validateEmail();
        }
    }

    public boolean validateEmail() {
        String email = etEmail.getText().toString().trim();
        if (!TextUtils.isEmpty(email) && !isValidEmail(email)) {
            tilEmail.setErrorEnabled(true);
            tilEmail.setError(getString(R.string.invalid_email));
            return false;
        } else if (TextUtils.isEmpty(email)) {
            tilEmail.setErrorEnabled(true);
            tilEmail.setError(null);
            return true;
        }
        tilEmail.setErrorEnabled(true);
        tilEmail.setError(null);
        return true;
    }

    private static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onSelected() {
    }

    @Override
    public void onError(@NonNull VerificationError error) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = context instanceof Activity ? (Activity) context : null;
        try {
            onNavigationBarListener = (OnNavigationBarListener.CustomerContactDetails) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNavigationBarListener.CustomerContactDetails");
        }
    }
}
