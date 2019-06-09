package org.apache.fineract.ui.online.review;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.ContactDetail;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.ui.online.customers.createcustomer.FormCustomerDetailsFragment;
import org.apache.fineract.ui.online.customers.createcustomer.OnNavigationBarListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Mohak Puri
 *         On 26/05/18.
 */

public class CreateCustomerReviewFragment extends FineractBaseFragment implements Step {

    View rootView;
    Customer customer;
    OnNavigationBarListener.CustomerDetails onNavigationBarListener;

    @BindView(R.id.tv_account)
    TextView tvAccount;

    @BindView(R.id.tv_first_name)
    TextView tvFirstName;

    @BindView(R.id.tv_middle_name)
    TextView tvMiddleName;

    @BindView(R.id.tv_last_name)
    TextView tvLastName;

    @BindView(R.id.ll_middle_name)
    LinearLayout llMiddleName;

    @BindView(R.id.tv_dob)
    TextView tvDob;

    @BindView(R.id.cb_is_member)
    CheckBox cbIsAMember;

    @BindView(R.id.tv_street)
    TextView tvStreet;

    @BindView(R.id.tv_city)
    TextView tvCity;

    @BindView(R.id.ll_postal_code)
    LinearLayout llPostalCode;

    @BindView(R.id.tv_postal_code)
    TextView tvPostalCode;

    @BindView(R.id.tv_country)
    TextView tvCountry;

    @BindView(R.id.ll_region)
    LinearLayout llRegion;

    @BindView(R.id.tv_region)
    TextView tvRegion;

    @BindView(R.id.ll_email)
    LinearLayout llEmail;

    @BindView(R.id.tv_email)
    TextView tvEmail;

    @BindView(R.id.ll_phone)
    LinearLayout llPhone;

    @BindView(R.id.tv_phone)
    TextView tvPhone;

    @BindView(R.id.ll_mobile)
    LinearLayout llMobile;

    @BindView(R.id.tv_mobile)
    TextView tvMobile;

    @BindView(R.id.ll_step_three)
    LinearLayout ll_stepThree;

    @BindView(R.id.tv_reviewNameOne)
    TextView tvReviewNameOne;

    @BindView(R.id.tv_reviewNameTwo)
    TextView tvReviewNameTwo;

    @BindView(R.id.tv_reviewNameThree)
    TextView tvReviewNameThree;

    @BindArray(R.array.create_customer_steps)
    String[] createCustomerSteps;


    public static CreateCustomerReviewFragment newInstance() {
        CreateCustomerReviewFragment fragment = new CreateCustomerReviewFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnNavigationBarListener.CustomerDetails) {
            onNavigationBarListener = (OnNavigationBarListener.CustomerDetails) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNavigationBarListener.CustomerDetails");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_create_customer_review, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void showUserInterface() {

        if (customer == null) {
            return;
        }

        tvAccount.setText(customer.getIdentifier());
        tvFirstName.setText(customer.getGivenName());
        tvLastName.setText(customer.getSurname());

        tvReviewNameOne.setText(createCustomerSteps[0]);
        tvReviewNameTwo.setText(createCustomerSteps[1]);

        if (customer.getMiddleName().isEmpty()) {
            llMiddleName.setVisibility(View.GONE);
        } else {
            llMiddleName.setVisibility(View.VISIBLE);
            tvMiddleName.setText(customer.getMiddleName());
        }

        setDateOfBirth();
        cbIsAMember.setChecked(customer.getMember());
        tvStreet.setText(customer.getAddress().getStreet());
        tvCity.setText(customer.getAddress().getCity());

        if (customer.getAddress().getPostalCode().isEmpty()) {
            llPostalCode.setVisibility(View.GONE);
        } else {
            llPostalCode.setVisibility(View.VISIBLE);
            tvPostalCode.setText(customer.getAddress().getPostalCode());
        }

        tvCountry.setText(customer.getAddress().getCountry());

        if (customer.getAddress().getRegion().isEmpty()) {
            llRegion.setVisibility(View.GONE);
        } else {
            llRegion.setVisibility(View.VISIBLE);
            tvRegion.setText(customer.getAddress().getRegion());
        }

        llEmail.setVisibility(View.GONE);
        llPhone.setVisibility(View.GONE);
        llMobile.setVisibility(View.GONE);

        if (customer.getContactDetails().isEmpty()) {
            ll_stepThree.setVisibility(View.GONE);
        } else {
            ll_stepThree.setVisibility(View.VISIBLE);
            tvReviewNameThree.setText(createCustomerSteps[2]);
        }

        for (ContactDetail detail : customer.getContactDetails()) {

            if (detail.getType() == ContactDetail.Type.EMAIL) {
                llEmail.setVisibility(View.VISIBLE);
                tvEmail.setText(detail.getValue());
            }

            if (detail.getType() == ContactDetail.Type.PHONE) {
                llPhone.setVisibility(View.VISIBLE);
                tvPhone.setText(detail.getValue());
            }

            if (detail.getType() == ContactDetail.Type.MOBILE) {
                llMobile.setVisibility(View.VISIBLE);
                tvMobile.setText(detail.getValue());
            }
        }

    }

    private void setDateOfBirth() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, customer.getDateOfBirth().getYear());
        calendar.set(Calendar.MONTH, customer.getDateOfBirth().getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, customer.getDateOfBirth().getDay());
        SimpleDateFormat sdf = new SimpleDateFormat(FormCustomerDetailsFragment.DATE_FORMAT,
                Locale.ENGLISH);
        tvDob.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
        customer = onNavigationBarListener.getCustomer();
        showUserInterface();
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        onNavigationBarListener = null;
    }

}
