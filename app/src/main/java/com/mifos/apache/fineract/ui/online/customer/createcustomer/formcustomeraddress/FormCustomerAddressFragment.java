package com.mifos.apache.fineract.ui.online.customer.createcustomer.formcustomeraddress;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.customer.Address;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.mifos.apache.fineract.ui.base.Toaster;
import com.mifos.apache.fineract.ui.online.customer.createcustomer.OnNavigationBarListener;
import com.mifos.apache.fineract.utils.ConstantKeys;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 25/07/17.
 */
public class FormCustomerAddressFragment extends MifosBaseFragment implements Step, TextWatcher,
        FormCustomerAddressContract.View, View.OnFocusChangeListener {

    @BindView(R.id.til_street)
    TextInputLayout tilStreet;

    @BindView(R.id.et_street)
    EditText etStreet;

    @BindView(R.id.til_city)
    TextInputLayout tilCity;

    @BindView(R.id.et_city)
    EditText etCity;

    @BindView(R.id.til_postal_code)
    TextInputLayout tilPostalCode;

    @BindView(R.id.et_postal_code)
    EditText etPostalCode;

    @BindView(R.id.til_country)
    TextInputLayout tilCountry;

    @BindView(R.id.et_country)
    AutoCompleteTextView etCountry;

    @BindView(R.id.til_region)
    TextInputLayout tilRegion;

    @BindView(R.id.et_region)
    EditText etRegion;

    View rootView;

    @Inject
    FormCustomerAddressPresenter formCustomerAddressPresenter;

    private String [] countries;

    private OnNavigationBarListener.CustomerAddress onNavigationBarListener;

    public static FormCustomerAddressFragment newInstance() {
        FormCustomerAddressFragment fragment = new FormCustomerAddressFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_form_customer_address, container, false);
        ButterKnife.bind(this, rootView);
        formCustomerAddressPresenter.attachView(this);

        showUserInterface();

        formCustomerAddressPresenter.fetchCountries();

        return rootView;
    }

    public void showUserInterface() {
        etStreet.addTextChangedListener(this);
        etCity.addTextChangedListener(this);
        etCountry.addTextChangedListener(this);
        etCountry.setOnFocusChangeListener(this);
    }

    @Override
    public VerificationError verifyStep() {
        if (!validateStreet() || !validateCity() || !validateCountry()) {
            return new VerificationError(null);
        } else {
            Address address = new Address();
            address.setStreet(etStreet.getText().toString().trim());
            address.setCity(etCity.getText().toString().trim());
            address.setCountry(etCountry.getText().toString().trim());
            address.setPostalCode(etPostalCode.getText().toString().trim());
            address.setRegion(etRegion.getText().toString().trim());
            address.setCountryCode(formCustomerAddressPresenter
                    .getCountryCode(etCountry.getText().toString().trim()));
            onNavigationBarListener.setAddress(address);
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
        if ((etCountry.getText().hashCode() == s.hashCode())) {
            etCountry.post(new Runnable() {
                @Override
                public void run() {
                    etCountry.showDropDown();
                    validateCountry();
                }
            });
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if ((etStreet.getText().hashCode() == s.hashCode())) {
            validateStreet();
        } else if (etCity.getText().hashCode() == s.hashCode()) {
            validateCity();
        } else if (etCountry.getText().hashCode() == s.hashCode()) {
            validateCountry();
        }
    }

    public boolean validateStreet() {
        if (TextUtils.isEmpty(etStreet.getText().toString().trim())) {
            showTextInputLayoutError(tilStreet, getString(R.string.required));
            return false;
        }
        showTextInputLayoutError(tilStreet, null);
        return true;
    }

    public boolean validateCity() {
        if (TextUtils.isEmpty(etCity.getText().toString().trim())) {
            showTextInputLayoutError(tilCity, getString(R.string.required));
            return false;
        }
        showTextInputLayoutError(tilCity, null);
        return true;
    }

    public boolean validateCountry() {
        if (TextUtils.isEmpty(etCountry.getText().toString().trim())) {
            showTextInputLayoutError(tilCountry, getString(R.string.required));
            return false;
        } else if (!formCustomerAddressPresenter.isCountryNameValid(
                etCountry.getText().toString().trim())) {
            showTextInputLayoutError(tilCountry, getString(R.string.invalid_country));
            return false;
        }
        showTextInputLayoutError(tilCountry, null);
        return true;
    }

    public void showTextInputLayoutError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(errorMessage);
    }

    @Override
    public void showCounties(List<String> countries) {
        this.countries = countries.toArray(new String[0]);
        ArrayAdapter<String> customerAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, this.countries);
        etCountry.setAdapter(customerAdapter);
    }

    @Override
    public void showError(String message) {
        Toaster.show(rootView, message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = context instanceof Activity ? (Activity) context : null;
        try {
            onNavigationBarListener = (OnNavigationBarListener.CustomerAddress) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNavigationBarListener.CustomerAddress");
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            hideKeyboard(etCountry, getActivity());
            etCountry.post(new Runnable() {
                @Override
                public void run() {
                    etCountry.showDropDown();
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArray(ConstantKeys.COUNTRIES, countries);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            countries = savedInstanceState.getStringArray(ConstantKeys.COUNTRIES);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        formCustomerAddressPresenter.detachView();
    }
}
