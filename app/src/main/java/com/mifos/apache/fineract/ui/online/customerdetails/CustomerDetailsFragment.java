package com.mifos.apache.fineract.ui.online.customerdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.customer.Address;
import com.mifos.apache.fineract.data.models.customer.ContactDetail;
import com.mifos.apache.fineract.data.models.customer.Customer;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.MifosBaseFragment;
import com.mifos.apache.fineract.ui.base.Toaster;
import com.mifos.apache.fineract.ui.online.customerdeposit.CustomerDepositActivity;
import com.mifos.apache.fineract.ui.online.customerloans.CustomerLoansActivity;
import com.mifos.apache.fineract.ui.online.identification.identificationlist.IdentificationsActivity;
import com.mifos.apache.fineract.ui.online.tasks.TasksBottomSheetFragment;
import com.mifos.apache.fineract.ui.views.HeaderView;
import com.mifos.apache.fineract.utils.ConstantKeys;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 26/06/17.
 */
public class CustomerDetailsFragment extends MifosBaseFragment
        implements AppBarLayout.OnOffsetChangedListener, CustomerDetailsContract.View {

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_header_view)
    HeaderView toolbarHeaderView;

    @BindView(R.id.float_header_view)
    HeaderView floatHeaderView;

    @BindView(R.id.tv_current_status)
    TextView tvCurrentStatus;

    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.tv_email)
    TextView tvEmail;

    @BindView(R.id.tv_phone_no)
    TextView tvPhoneNo;

    @BindView(R.id.tv_mobile_no)
    TextView tvMobileNo;

    @BindView(R.id.tv_birthday)
    TextView tvBirthDay;

    @BindView(R.id.tv_no_contact_details_available)
    TextView tvNoContactDetailsAvailable;

    @BindView(R.id.ncv_customer_details)
    NestedScrollView ncvCustomerDetails;

    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    @BindView(R.id.tv_error)
    TextView tvError;

    @Inject
    CustomerDetailsPresenter customerDetailsPresenter;

    private View rootView;
    private String customerIdentifier;
    private boolean isHideToolbarView = false;
    private Customer customer;

    public static CustomerDetailsFragment newInstance(String identifier) {
        CustomerDetailsFragment fragment = new CustomerDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ConstantKeys.CUSTOMER_IDENTIFIER, identifier);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customerIdentifier = getArguments().getString(ConstantKeys.CUSTOMER_IDENTIFIER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customer_details, container, false);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        customerDetailsPresenter.attachView(this);

        showUserInterface();

        customerDetailsPresenter.loanCustomerDetails(customerIdentifier);

        return rootView;
    }

    @OnClick(R.id.iv_retry)
    void onRetry() {
        ncvCustomerDetails.setVisibility(View.GONE);
        rlError.setVisibility(View.GONE);
        customerDetailsPresenter.loanCustomerDetails(customerIdentifier);
    }

    @OnClick(R.id.ll_deposit_accounts)
    void viewDepositAccounts() {
        Intent intent = new Intent(getActivity(), CustomerDepositActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        startActivity(intent);
    }

    @OnClick(R.id.ll_loan_accounts)
    void viewLoanAccounts() {
        Intent intent = new Intent(getActivity(), CustomerLoansActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        startActivity(intent);
    }

    @OnClick(R.id.ll_tasks)
    void updateCustomerStatus() {
        TasksBottomSheetFragment tasksBottomSheet = new TasksBottomSheetFragment();
        tasksBottomSheet.setCustomerStatus(customer.getCurrentState());
        tasksBottomSheet.setCustomerIdentifier(customerIdentifier);
        tasksBottomSheet.show(getChildFragmentManager(), getString(R.string.tasks));
    }

    @OnClick(R.id.ll_identifier_cards)
    void showIdentificationCards() {
        Intent intent = new Intent(getActivity(), IdentificationsActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        startActivity(intent);
    }

    @Override
    public void showUserInterface() {
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(" ");
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public void showCustomerDetails(Customer customer) {
        this.customer = customer;
        ncvCustomerDetails.setVisibility(View.VISIBLE);

        tvCurrentStatus.setText(customer.getCurrentState().name());
        switch (customer.getCurrentState()) {
            case ACTIVE:
                tvCurrentStatus.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_check_circle_black_24dp, 0, 0, 0);
                break;
            case PENDING:
                tvCurrentStatus.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_hourglass_empty_black_24dp, 0, 0, 0);
                break;
            case LOCKED:
                break;
            case CLOSED:
                break;
        }

        Address address = customer.getAddress();
        tvAddress.setText(address.getRegion() + " " + address.getCity()
                + " " + address.getPostalCode() + " " + address.getCountry());

        if (customer.getContactDetails().size() == 0) {
            tvNoContactDetailsAvailable.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.GONE);
            tvPhoneNo.setVisibility(View.GONE);
            tvMobileNo.setVisibility(View.GONE);
        } else {
            for (ContactDetail contactDetail : customer.getContactDetails()) {
                showContactDetails(contactDetail);
            }
        }

        tvBirthDay.setText(customer.getDateOfBirth().getYear() + "-" +
                customer.getDateOfBirth().getMonth() + "-" + customer.getDateOfBirth().getDay());

        String title = customer.getGivenName() + " " + customer.getSurname();
        String subtitle;
        if (customer.getAssignedOffice() == null) {
            subtitle = getString(R.string.assigned_employee) + " " +
                    getString(R.string.not_assigned);
        } else {
            subtitle = getString(R.string.assigned_employee) + " " + customer.getAssignedEmployee();
        }
        showToolbarTitleSubtitle(title, subtitle);
    }

    @Override
    public void showContactDetails(ContactDetail contactDetail) {
        switch (contactDetail.getType()) {
            case EMAIL:
                tvEmail.setText(contactDetail.getValue());
                tvEmail.setVisibility(View.VISIBLE);
                break;
            case MOBILE:
                tvMobileNo.setText(contactDetail.getValue());
                tvMobileNo.setVisibility(View.VISIBLE);
                break;
            case PHONE:
                tvPhoneNo.setText(contactDetail.getValue());
                tvPhoneNo.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void showToolbarTitleSubtitle(String title, String subtitle) {
        toolbarHeaderView.bindTo(title, subtitle);
        floatHeaderView.bindTo(title, subtitle);
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
    public void showError(String errorMessage) {
        rlError.setVisibility(View.VISIBLE);
        tvError.setText(errorMessage);
        Toaster.show(rootView, errorMessage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideMifosProgressBar();
        customerDetailsPresenter.detachView();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }
}
