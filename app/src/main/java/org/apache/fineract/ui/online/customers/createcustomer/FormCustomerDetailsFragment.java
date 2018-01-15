package org.apache.fineract.ui.online.customers.createcustomer;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.data.models.customer.DateOfBirth;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.utils.ConstantKeys;
import org.apache.fineract.utils.ValidationUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 25/07/17.
 */
public class FormCustomerDetailsFragment extends FineractBaseFragment implements Step, TextWatcher {

    public static final String DATE_FORMAT = "dd MMM yyyy";

    @BindView(R.id.til_account)
    TextInputLayout tilAccount;

    @BindView(R.id.et_account)
    EditText etAccount;

    @BindView(R.id.til_first_name)
    TextInputLayout tilFirstName;

    @BindView(R.id.et_first_name)
    EditText etFirstName;

    @BindView(R.id.til_middle_name)
    TextInputLayout tilMiddleName;

    @BindView(R.id.et_middle_name)
    EditText etMiddleName;

    @BindView(R.id.til_last_name)
    TextInputLayout tilLastName;

    @BindView(R.id.et_last_name)
    EditText etLastName;

    @BindView(R.id.til_date_of_birth)
    TextInputLayout tilDateOfBirth;

    @BindView(R.id.et_date_of_birth)
    EditText etDateOfBirth;

    @BindView(R.id.cb_is_member)
    CheckBox cbIsmember;

    View rootView;

    private Calendar calendar = Calendar.getInstance();
    private DateOfBirth dateOfBirth;
    private Customer customer;
    private CustomerAction customerAction;

    private OnNavigationBarListener.CustomerDetails onNavigationBarListener;
    private DatePickerDialog.OnDateSetListener date;

    public static FormCustomerDetailsFragment newInstance(Customer customer,
            CustomerAction action) {
        FormCustomerDetailsFragment fragment = new FormCustomerDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ConstantKeys.CUSTOMER, customer);
        args.putSerializable(ConstantKeys.CUSTOMER_ACTION, action);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateOfBirth = new DateOfBirth();
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
        rootView = inflater.inflate(R.layout.fragment_form_customer_details, container, false);
        ButterKnife.bind(this, rootView);

        showUserInterface();

        if (customerAction == CustomerAction.EDIT) {
            showPreviousCustomerDetails();
        }

        return rootView;
    }

    public void showUserInterface() {
        etAccount.addTextChangedListener(this);
        etFirstName.addTextChangedListener(this);
        etMiddleName.addTextChangedListener(this);
        etLastName.addTextChangedListener(this);
        etDateOfBirth.setText(DATE_FORMAT);
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateOfBirth.setDay(dayOfMonth);
                dateOfBirth.setMonth(monthOfYear + 1);
                dateOfBirth.setYear(year);
                setDateOfBirth();
            }
        };
    }

    public void showPreviousCustomerDetails() {
        etAccount.setText(customer.getIdentifier());
        etAccount.setEnabled(false);
        etFirstName.setText(customer.getGivenName());
        if (customer.getMiddleName() != null) {
            etMiddleName.setText(customer.getMiddleName());
        }
        etLastName.setText(customer.getSurname());
        cbIsmember.setChecked(customer.getMember());
        dateOfBirth = customer.getDateOfBirth();

        calendar.set(Calendar.YEAR, dateOfBirth.getYear());
        calendar.set(Calendar.MONTH, dateOfBirth.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, dateOfBirth.getDay());
        setDateOfBirth();
    }

    @OnClick(R.id.et_date_of_birth)
    void onClickDateOfBirth() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void setDateOfBirth() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        etDateOfBirth.setText(sdf.format(calendar.getTime()));
        validateDateOfBirth();
    }

    @Override
    public VerificationError verifyStep() {
        if (!(validateAccount() && isUrlSafe()) || !validateFirstName() || !validateLastName()
                || !validateDateOfBirth()) {
            isUrlSafe();
            return new VerificationError(null);
        } else {
            onNavigationBarListener.setCustomerDetails(etAccount.getText().toString().trim(),
                    etFirstName.getText().toString().trim(),
                    etLastName.getText().toString().trim(),
                    etMiddleName.getText().toString().trim(), dateOfBirth,
                    cbIsmember.isEnabled());
        }
        return null;
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
    public void afterTextChanged(Editable s) {
        if ((etAccount.getText().hashCode() == s.hashCode())) {
            if (validateAccount()) {
                isUrlSafe();
            }
        } else if (etFirstName.getText().hashCode() == s.hashCode()) {
            validateFirstName();
        } else if (etLastName.getText().hashCode() == s.hashCode()) {
            validateLastName();
        } else if (etDateOfBirth.getText().hashCode() == s.hashCode()) {
            validateDateOfBirth();
        }
    }

    public boolean validateAccount() {
        String account = etAccount.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            showTextInputLayoutError(tilAccount, getString(R.string.unique_required));
            return false;
        } else if (account.length() < 3) {
            showTextInputLayoutError(tilAccount,
                    getString(R.string.must_be_at_least_three_characters, 3));
            return false;
        } else if (account.length() > 32) {
            showTextInputLayoutError(tilAccount,
                    getString(R.string.only_thirty_two_character_allowed));
            return false;
        }
        showTextInputLayoutError(tilAccount, null);
        return true;
    }

    public boolean validateFirstName() {
        if (TextUtils.isEmpty(etFirstName.getText().toString().trim())) {
            showTextInputLayoutError(tilFirstName, getString(R.string.required));
            return false;
        }
        showTextInputLayoutError(tilFirstName, null);
        return true;
    }

    public boolean validateLastName() {
        if (TextUtils.isEmpty(etLastName.getText().toString().trim())) {
            showTextInputLayoutError(tilLastName, getString(R.string.required));
            return false;
        }
        showTextInputLayoutError(tilLastName, null);
        return true;
    }

    public boolean validateDateOfBirth() {
        if ((dateOfBirth.getDay() == null) || (dateOfBirth.getYear() == null) || (
                dateOfBirth.getMonth() == null)) {
            showTextInputLayoutError(tilDateOfBirth, getString(R.string.required));
            return false;
        }
        showTextInputLayoutError(tilDateOfBirth, null);
        return true;
    }

    public boolean isUrlSafe() {
        if (!ValidationUtil.isUrlSafe(etAccount.getText().toString().trim())) {
            showTextInputLayoutError(tilAccount, getString(
                    R.string.only_alphabetic_decimal_digits_characters_allowed));
            return false;
        }
        return true;
    }

    public void showTextInputLayoutError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(errorMessage);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = context instanceof Activity ? (Activity) context : null;
        try {
            onNavigationBarListener = (OnNavigationBarListener.CustomerDetails) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNavigationBarListener.CustomerDetails");
        }
    }
}
