package org.apache.fineract.ui.online.loanaccounts.loanapplication.loandetails;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.apache.fineract.R;
import org.apache.fineract.data.models.loan.LoanAccount;
import org.apache.fineract.data.models.loan.PaymentCycle;
import org.apache.fineract.data.models.loan.TermRange;
import org.apache.fineract.data.models.product.Product;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.ui.base.Toaster;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.OnNavigationBarListener;
import org.apache.fineract.utils.Utils;
import org.apache.fineract.utils.ValidateIdentifierUtil;
import org.apache.fineract.utils.ValidationUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 17/07/17.
 */
public class LoanDetailsFragment extends FineractBaseFragment implements Step,
        LoanDetailsContract.View, AdapterView.OnItemSelectedListener, TextWatcher {

    public static final String LOG_TAG = LoanDetailsFragment.class.getSimpleName();

    @BindView(R.id.ncv_loan_details)
    NestedScrollView ncvLoanDetails;

    @BindView(R.id.sp_products)
    AppCompatSpinner spProducts;

    @BindView(R.id.til_short_name)
    TextInputLayout tilShortName;

    @BindView(R.id.et_short_name)
    EditText etShortName;

    @BindView(R.id.et_principal_amount)
    EditText etPrincipalAmount;

    @BindView(R.id.til_principal_amount)
    TextInputLayout tilPrincipalAmount;

    @BindView(R.id.et_term)
    EditText etTerm;

    @BindView(R.id.til_term)
    TextInputLayout tilTerm;

    @BindView(R.id.sp_term_unit_type)
    AppCompatSpinner spTermUnitType;

    @BindView(R.id.et_repay)
    EditText etRepay;

    @BindView(R.id.til_repay)
    TextInputLayout tilRepay;

    @BindView(R.id.sp_repay_unit_type)
    AppCompatSpinner spRepayUnitType;

    @BindView(R.id.ll_repay_unit_week)
    LinearLayout llRepayUnitWeek;

    @BindView(R.id.ll_repay_unit_year)
    LinearLayout llRepayUnitYear;

    @BindView(R.id.sp_repay_unit_week)
    AppCompatSpinner spRepayUnitTypeWeek;

    @BindView(R.id.ll_repay_unit_month)
    LinearLayout llRepayUnitMonth;

    @BindView(R.id.rb_repay_on_day)
    RadioButton rbRepayOnDay;

    @BindView(R.id.rb_repay_on_specific_week_day)
    RadioButton rbRepayOnSpecificDay;

    @BindView(R.id.sp_repay_month_day_in_number)
    AppCompatSpinner spRepayMonthDayInNumber;

    @BindView(R.id.sp_repay_time_slots)
    AppCompatSpinner spRepayTimeSlots;

    @BindView(R.id.sp_repay_week_days)
    AppCompatSpinner spRepayWeekDays;

    @BindView(R.id.sp_repay_year_month)
    AppCompatSpinner spRepayYearMonth;

    @BindView(R.id.layout_error)
    View layoutError;

    View rootView;

    @Inject
    LoanDetailsPresenter loanDetailsPresenter;

    private OnNavigationBarListener.LoanDetailsData onNavigationBarListener;

    private List<String> products;
    private List<String> repayUnitType;
    private List<String> ternUnitType;
    private List<String> timeSlots;
    private List<String> weeks;
    private List<String> months;
    private List<String> monthsNumber;
    private Product product;

    private ArrayAdapter<String> productsAdapter;
    private ArrayAdapter<String> termUnitTypeAdapter;

    public static LoanDetailsFragment newInstance() {
        LoanDetailsFragment fragment = new LoanDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((FineractBaseActivity) getActivity()).getActivityComponent().inject(this);
        setRetainInstance(true);
        products = new ArrayList<>();
        ternUnitType = new ArrayList<>();
        repayUnitType = new ArrayList<>(Arrays.asList(
                getActivity().getResources().getStringArray(R.array.repay_unit_type)));
        timeSlots = new ArrayList<>(Arrays.asList(
                getActivity().getResources().getStringArray(R.array.time_slots)));
        weeks = new ArrayList<>(Arrays.asList(
                getActivity().getResources().getStringArray(R.array.week_names)));
        months = new ArrayList<>(Arrays.asList(
                getActivity().getResources().getStringArray(R.array.month_names)));
        monthsNumber = new ArrayList<>(Arrays.asList(
                getActivity().getResources().getStringArray(R.array.repay_on_months)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_loan_details, container, false);
        ButterKnife.bind(this, rootView);
        initializeFineractUIErrorHandler(getActivity(), rootView);
        loanDetailsPresenter.attachView(this);

        showUserInterface();

        loanDetailsPresenter.fetchProducts();

        return rootView;
    }

    @OnClick(R.id.btn_try_again)
    void reloadProducts() {
        layoutError.setVisibility(View.GONE);
        ncvLoanDetails.setVisibility(View.GONE);
        loanDetailsPresenter.fetchProducts();
    }

    @Override
    public VerificationError verifyStep() {
        if (!validateShortName() || !validateTerm() || !validateRepay()
                || !validatePrincipalAmount()) {
            return new VerificationError(null);
        } else {
            PaymentCycle paymentCycle = new PaymentCycle();
            paymentCycle.setPeriod(Integer.parseInt(etRepay.getText().toString().trim()));
            paymentCycle.setTemporalUnit(
                    spRepayUnitType.getSelectedItem().toString().toUpperCase());
            switch (spRepayUnitType.getSelectedItemPosition()) {
                case 0:
                    paymentCycle.setAlignmentDay(spRepayUnitTypeWeek.getSelectedItemPosition());
                    break;
                case 1:
                    if (rbRepayOnDay.isChecked()) {
                        paymentCycle.setAlignmentDay(
                                spRepayMonthDayInNumber.getSelectedItemPosition());
                    } else if (rbRepayOnSpecificDay.isChecked()) {
                        paymentCycle.setAlignmentDay(spRepayWeekDays.getSelectedItemPosition());
                        paymentCycle.setAlignmentMonth(null);
                        paymentCycle.setAlignmentWeek(spRepayTimeSlots.getSelectedItemPosition());
                    }
                    break;
                case 2:
                    if (rbRepayOnDay.isChecked()) {
                        paymentCycle.setAlignmentDay(
                                spRepayMonthDayInNumber.getSelectedItemPosition());
                    } else if (rbRepayOnSpecificDay.isChecked()) {
                        paymentCycle.setAlignmentDay(spRepayWeekDays.getSelectedItemPosition());
                        paymentCycle.setAlignmentMonth(null);
                        paymentCycle.setAlignmentWeek(spRepayTimeSlots.getSelectedItemPosition());
                    }
                    paymentCycle.setAlignmentMonth(spRepayYearMonth.getSelectedItemPosition());
                    break;
            }
            onNavigationBarListener.setLoanDetails(LoanAccount.State.CREATED,
                    etShortName.getText().toString().trim(), product.getIdentifier(),
                    Double.parseDouble(etPrincipalAmount.getText().toString().trim()),
                    paymentCycle, new TermRange(product.getTermRange().getTemporalUnit(),
                            Double.parseDouble(etTerm.getText().toString().trim()))
                    , spProducts.getSelectedItem().toString());


            return null;
        }
    }

    @Override
    public void onSelected() {
        // when every condition will fine in verifyStep();
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        // Make changes on UI, If error occurred
    }

    @OnClick(R.id.rb_repay_on_day)
    void repayOnDay() {
        spRepayMonthDayInNumber.setEnabled(true);
        spRepayTimeSlots.setEnabled(false);
        spRepayWeekDays.setEnabled(false);
    }

    @OnClick(R.id.rb_repay_on_specific_week_day)
    void repayOnSpecificDay() {
        spRepayMonthDayInNumber.setEnabled(false);
        spRepayTimeSlots.setEnabled(true);
        spRepayWeekDays.setEnabled(true);
    }

    @Override
    public void showUserInterface() {

        productsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, products);
        productsAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spProducts.setAdapter(productsAdapter);
        spProducts.setOnItemSelectedListener(this);

        ArrayAdapter<String> repayUnitAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, repayUnitType);
        repayUnitAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spRepayUnitType.setAdapter(repayUnitAdapter);
        spRepayUnitType.setOnItemSelectedListener(this);

        ArrayAdapter<String> repayUnitTypeWeekAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, weeks);
        repayUnitTypeWeekAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spRepayUnitTypeWeek.setAdapter(repayUnitTypeWeekAdapter);
        spRepayUnitTypeWeek.setOnItemSelectedListener(this);

        ArrayAdapter<String> repayUnitTypeMonthTimeSlotsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_gallery_item, timeSlots);
        repayUnitTypeMonthTimeSlotsAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spRepayTimeSlots.setAdapter(repayUnitTypeMonthTimeSlotsAdapter);
        spRepayTimeSlots.setOnItemSelectedListener(this);

        ArrayAdapter<String> repayUnitTypeMonthWeeksAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_gallery_item, weeks);
        repayUnitTypeMonthWeeksAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spRepayWeekDays.setAdapter(repayUnitTypeMonthWeeksAdapter);
        spRepayWeekDays.setOnItemSelectedListener(this);

        ArrayAdapter<String> repayUnitTypeYearMonthsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, months);
        repayUnitTypeYearMonthsAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spRepayYearMonth.setAdapter(repayUnitTypeYearMonthsAdapter);
        spRepayYearMonth.setOnItemSelectedListener(this);

        ArrayAdapter<String> repayMonthDayInNumberAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_gallery_item, monthsNumber);
        repayMonthDayInNumberAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spRepayMonthDayInNumber.setAdapter(repayMonthDayInNumberAdapter);
        spRepayMonthDayInNumber.setOnItemSelectedListener(this);

        termUnitTypeAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_gallery_item, ternUnitType);
        termUnitTypeAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spTermUnitType.setAdapter(termUnitTypeAdapter);
        spTermUnitType.setOnItemSelectedListener(this);

        etPrincipalAmount.addTextChangedListener(this);
        etTerm.addTextChangedListener(this);
        etRepay.addTextChangedListener(this);
        etShortName.addTextChangedListener(this);

        spRepayMonthDayInNumber.setEnabled(true);
        spRepayTimeSlots.setEnabled(false);
        spRepayWeekDays.setEnabled(false);
        rbRepayOnDay.setChecked(true);
    }

    @Override
    public void showProducts(List<String> products) {
        ncvLoanDetails.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
        this.products.addAll(products);
        productsAdapter.notifyDataSetChanged();
    }

    @Override
    public void setComponentsValidations(Product product) {
        this.product = product;
        etPrincipalAmount.setText(String.valueOf(product.getBalanceRange().getMinimum()));
        ternUnitType.clear();
        ternUnitType.addAll(loanDetailsPresenter.getCurrentTermUnitType(
                repayUnitType, product.getTermRange().getTemporalUnit()));
        termUnitTypeAdapter.notifyDataSetChanged();
        spTermUnitType.setEnabled(false);
        etTerm.setText("1");
        etRepay.setText("1");
    }

    @Override
    public void showEmptyProducts() {
        ncvLoanDetails.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
        Toaster.show(rootView, getString(R.string.empty_create_loan_products), Toaster.INDEFINITE);
    }

    @Override
    public void showProgressbar() {
        showMifosProgressBar();
    }

    @Override
    public void hideProgressbar() {
        hideMifosProgressBar();
    }

    @Override
    public void showNoInternetConnection() {
        ncvLoanDetails.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        showFineractNoInternetUI();
    }

    @Override
    public void showError(String message) {
        ncvLoanDetails.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        showFineractErrorUI(getString(R.string.products));
    }

    @Override
    public boolean validateShortName() {
        return ValidateIdentifierUtil.isValid(getActivity(),
                etShortName.getText().toString().trim(), tilShortName);
    }

    @Override
    public boolean validateTerm() {
        try {
            Double minimum = 1.0;
            Double maximum = product.getTermRange().getMaximum();
            Double value = Double.parseDouble(etTerm.getText().toString());

            if (etTerm.getText().toString().equals("")) {
                ValidationUtil.showTextInputLayoutError(tilTerm, getString(R.string.required));
                return false;
            } else if (!(minimum <= value)) {
                ValidationUtil.showTextInputLayoutError(tilTerm,
                        getString(R.string.value_must_greater_or_equal_to,
                                Utils.getPrecision(minimum)));
                return false;
            } else if (!(value <= maximum)) {
                ValidationUtil.showTextInputLayoutError(tilTerm,
                        getString(R.string.value_must_less_than_or_equal_to,
                                Utils.getPrecision(maximum)));
                return false;
            }
        } catch (NumberFormatException e) {
            ValidationUtil.showTextInputLayoutError(tilTerm,
                    getString(R.string.required));
            return false;
        }
        ValidationUtil.hideTextInputLayoutError(tilTerm);
        return true;
    }

    @Override
    public boolean validatePrincipalAmount() {
        try {
            Double minimum = product.getBalanceRange().getMinimum();
            Double maximum = product.getBalanceRange().getMaximum();
            Double value = Double.parseDouble(etPrincipalAmount.getText().toString());

            if (etPrincipalAmount.getText().toString().equals("")) {
                ValidationUtil.isEmpty(getActivity(),
                        etPrincipalAmount.getText().toString().trim(), tilPrincipalAmount);
                return false;
            } else if (!(minimum < value)) {
                ValidationUtil.showTextInputLayoutError(tilPrincipalAmount,
                        getString(R.string.value_must_greater_or_equal_to,
                                Utils.getPrecision(minimum)));
                return false;
            } else if (!(value <= maximum)) {
                ValidationUtil.showTextInputLayoutError(tilPrincipalAmount,
                        getString(R.string.value_must_less_than_or_equal_to,
                                Utils.getPrecision(maximum)));
                return false;
            }
        } catch (NumberFormatException e) {
            ValidationUtil.showTextInputLayoutError(tilPrincipalAmount,
                    getString(R.string.required));
            return false;
        }
        ValidationUtil.hideTextInputLayoutError(tilPrincipalAmount);
        return true;
    }

    @Override
    public boolean validateRepay() {
        return ValidationUtil.isEmpty(getActivity(),
                etRepay.getText().toString().trim(), tilRepay);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_products:
                loanDetailsPresenter.setProductPositionAndValidateViews(position);
                break;
            case R.id.sp_repay_unit_type:
                switch (position) {
                    case 0:
                        llRepayUnitWeek.setVisibility(View.VISIBLE);
                        llRepayUnitMonth.setVisibility(View.GONE);
                        llRepayUnitYear.setVisibility(View.GONE);
                        break;
                    case 1:
                        llRepayUnitWeek.setVisibility(View.GONE);
                        llRepayUnitMonth.setVisibility(View.VISIBLE);
                        llRepayUnitYear.setVisibility(View.GONE);
                        break;
                    case 2:
                        llRepayUnitWeek.setVisibility(View.GONE);
                        llRepayUnitMonth.setVisibility(View.VISIBLE);
                        llRepayUnitYear.setVisibility(View.VISIBLE);
                        break;
                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideMifosProgressBar();
        loanDetailsPresenter.detachView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = context instanceof Activity ? (Activity) context : null;
        try {
            onNavigationBarListener = (OnNavigationBarListener.LoanDetailsData) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNavigationBarListener");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etPrincipalAmount.getText().hashCode() == s.hashCode()) {
            validatePrincipalAmount();
        } else if (etTerm.getText().hashCode() == s.hashCode()) {
            validateTerm();
        } else if (etRepay.getText().hashCode() == s.hashCode()) {
            validateRepay();
        } else if (etShortName.getText().hashCode() == s.hashCode()) {
            validateShortName();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("products", (ArrayList<String>) products);
        super.onSaveInstanceState(outState);
    }
}
