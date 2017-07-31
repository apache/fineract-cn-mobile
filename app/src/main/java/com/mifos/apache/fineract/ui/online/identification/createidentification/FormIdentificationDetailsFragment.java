package com.mifos.apache.fineract.ui.online.identification.createidentification;

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
import android.widget.DatePicker;
import android.widget.EditText;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.customer.identification.ExpirationDate;
import com.mifos.apache.fineract.data.models.customer.identification.Identification;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 31/07/17.
 */
public class FormIdentificationDetailsFragment extends MifosBaseFragment implements Step,
        TextWatcher {

    public static final String DATE_FORMAT = "dd MMM yyyy";

    @BindView(R.id.til_number)
    TextInputLayout tilNumber;

    @BindView(R.id.et_number)
    EditText etNumber;

    @BindView(R.id.til_type)
    TextInputLayout tilType;

    @BindView(R.id.et_type)
    EditText etType;

    @BindView(R.id.til_expiration_date)
    TextInputLayout tilExpirationDate;

    @BindView(R.id.et_expiration_date)
    EditText etExpirationDate;

    @BindView(R.id.til_issuer)
    TextInputLayout tilIssuer;

    @BindView(R.id.et_issuer)
    EditText etIssuer;

    View rootView;

    private Calendar calendar = Calendar.getInstance();
    private ExpirationDate expirationDate;

    private DatePickerDialog.OnDateSetListener date;
    private OnNavigationBarListener.IdentificationCard onNavigationBarListener;

    public static FormIdentificationDetailsFragment newInstance() {
        FormIdentificationDetailsFragment fragment = new FormIdentificationDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expirationDate = new ExpirationDate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_form_identification_details,
                container, false);
        ButterKnife.bind(this, rootView);

        showUserInterface();

        return rootView;
    }

    public void showUserInterface() {
        etNumber.addTextChangedListener(this);
        etExpirationDate.addTextChangedListener(this);
        etIssuer.addTextChangedListener(this);
        etExpirationDate.setText(DATE_FORMAT);
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                expirationDate.setDay(dayOfMonth);
                expirationDate.setMonth(monthOfYear + 1);
                expirationDate.setYear(year);
                setDateOfBirth();
            }
        };
    }

    @OnClick(R.id.et_expiration_date)
    void onClickDateOfBirth() {
        new DatePickerDialog(getActivity(), date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setDateOfBirth() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        etExpirationDate.setText(sdf.format(calendar.getTime()));
        validateExpirationDate();
    }

    @Override
    public VerificationError verifyStep() {
        if (!validateNumber() || !validateType() || !validateExpirationDate()
                || !validateIssuer()) {
            return new VerificationError(null);
        } else {
            Identification identification = new Identification();
            identification.setNumber(etNumber.getText().toString().trim());
            identification.setType(etType.getText().toString().trim());
            identification.setExpirationDate(expirationDate);
            identification.setIssuer(etIssuer.getText().toString().trim());
            onNavigationBarListener.setIdentificationDetails(identification);
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
        if ((etNumber.getText().hashCode() == s.hashCode())) {
            validateNumber();
        } else if (etType.getText().hashCode() == s.hashCode()) {
            validateType();
        } else if (etExpirationDate.getText().hashCode() == s.hashCode()) {
            validateExpirationDate();
        } else if (etIssuer.getText().hashCode() == s.hashCode()) {
            validateIssuer();
        }
    }

    public boolean validateNumber() {
        String number = etNumber.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            showTextInputLayoutError(tilNumber, getString(R.string.unique_required));
            return false;
        }
        showTextInputLayoutError(tilNumber, null);
        return true;
    }

    public boolean validateType() {
        String type = etType.getText().toString().trim();
        if (TextUtils.isEmpty(type)) {
            showTextInputLayoutError(tilType, getString(R.string.required));
            return false;
        }
        showTextInputLayoutError(tilType, null);
        return true;
    }

    public boolean validateIssuer() {
        String issuer = etIssuer.getText().toString().trim();
        if (TextUtils.isEmpty(issuer)) {
            showTextInputLayoutError(tilIssuer, getString(R.string.required));
            return false;
        }
        showTextInputLayoutError(tilIssuer, null);
        return true;
    }

    public boolean validateExpirationDate() {
        if ((expirationDate.getDay() == null) || (expirationDate.getYear() == null) || (
                expirationDate.getMonth() == null)) {
            showTextInputLayoutError(tilExpirationDate, getString(R.string.required));
            return false;
        }
        showTextInputLayoutError(tilExpirationDate, null);
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
            onNavigationBarListener = (OnNavigationBarListener.IdentificationCard) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNavigationBarListener.IdentificationCard");
        }
    }
}
