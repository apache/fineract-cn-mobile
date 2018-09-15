package org.apache.fineract.ui.online.customers.customerdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.Address;
import org.apache.fineract.data.models.customer.ContactDetail;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseFragment;
import org.apache.fineract.ui.online.customers.createcustomer.CustomerAction;
import org.apache.fineract.ui.online.customers.createcustomer.customeractivity.CreateCustomerActivity;
import org.apache.fineract.ui.online.customers.customeractivities.CustomerActivitiesActivity;
import org.apache.fineract.ui.online.customers.customerprofile.CustomerProfileActivity;
import org.apache.fineract.ui.online.customers.customertasks.CustomerTasksBottomSheetFragment;
import org.apache.fineract.ui.online.customers.customertasks.OnTasksChangeListener;
import org.apache.fineract.ui.online.depositaccounts.depositaccountslist.DepositAccountsActivity;
import org.apache.fineract.ui.online.identification.identificationlist.IdentificationsActivity;
import org.apache.fineract.ui.online.loanaccounts.loanaccountlist.LoanAccountsActivity;
import org.apache.fineract.ui.views.HeaderView;
import org.apache.fineract.utils.ConstantKeys;
import org.apache.fineract.utils.ImageLoaderUtils;
import org.apache.fineract.utils.StatusUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 26/06/17.
 */
public class CustomerDetailsFragment extends FineractBaseFragment
        implements AppBarLayout.OnOffsetChangedListener, CustomerDetailsContract.View,
        OnTasksChangeListener {

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

    @BindView(R.id.cl_customer_details)
    CoordinatorLayout clCustomerDetails;

    @BindView(R.id.layout_error)
    View layoutError;

    @BindView(R.id.rl_email)
    RelativeLayout rlEmail;

    @BindView(R.id.rl_phone_no)
    RelativeLayout rlPhoneNo;

    @BindView(R.id.rl_mobile_no)
    RelativeLayout rlMobileNo;

    @BindView(R.id.iv_current_status)
    ImageView ivCurrentStatus;

    @BindView(R.id.iv_customer_profile)
    ImageView ivCustomerProfile;

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
        ((FineractBaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        initializeFineractUIErrorHandler(getActivity(), rootView);
        customerDetailsPresenter.attachView(this);

        showUserInterface();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        clCustomerDetails.setVisibility(View.GONE);
        collapsingToolbarLayout.setTitle(" ");
        customerDetailsPresenter.loanCustomerDetails(customerIdentifier);
    }

    @OnClick(R.id.btn_try_again)
    void onRetry() {
        clCustomerDetails.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
        customerDetailsPresenter.loanCustomerDetails(customerIdentifier);
    }

    @OnClick(R.id.ll_deposit_accounts)
    void viewDepositAccounts() {
        Intent intent = new Intent(getActivity(), DepositAccountsActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        startActivity(intent);
    }

    @OnClick(R.id.ll_loan_accounts)
    void viewLoanAccounts() {
        Intent intent = new Intent(getActivity(), LoanAccountsActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        startActivity(intent);
    }

    @OnClick(R.id.ll_tasks)
    void updateCustomerStatus() {
        CustomerTasksBottomSheetFragment tasksBottomSheet = new CustomerTasksBottomSheetFragment();
        tasksBottomSheet.setCustomerStatus(customer.getCurrentState());
        tasksBottomSheet.setCustomerIdentifier(customerIdentifier);
        tasksBottomSheet.setCustomerTasksChangeListener(this);
        tasksBottomSheet.show(getChildFragmentManager(), getString(R.string.tasks));
    }

    @OnClick(R.id.ll_identifier_cards)
    void showIdentificationCards() {
        Intent intent = new Intent(getActivity(), IdentificationsActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        startActivity(intent);
    }

    @OnClick(R.id.iv_customer_profile)
    void showCustomerProfileImage() {
        Intent intent = new Intent(getActivity(), CustomerProfileActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        startActivity(intent);
    }

    @OnClick(R.id.ll_activities)
    void showCustomerActivities() {
        Intent intent = new Intent(getActivity(), CustomerActivitiesActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier);
        startActivity(intent);
    }

    @OnClick(R.id.fab_edit_customer)
    void editCustomer() {
        Intent intent = new Intent(getActivity(), CreateCustomerActivity.class);
        intent.putExtra(ConstantKeys.CUSTOMER_ACTION, CustomerAction.EDIT);
        intent.putExtra(ConstantKeys.CUSTOMER, customer);
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
        clCustomerDetails.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);

        loadCustomerPortrait();

        tvCurrentStatus.setText(customer.getCurrentState().name());
        StatusUtils.setCustomerStatusIcon(customer.getCurrentState(),
                ivCurrentStatus, getActivity());

        Address address = customer.getAddress();
        StringBuilder addressBuilder = new StringBuilder();
        addressBuilder
                .append(address.getStreet()).append(", ")
                .append(address.getCity()).append(", ");
        if (address.getPostalCode() != null) {
            addressBuilder.append(address.getPostalCode());
            addressBuilder.append(", ");
        }
        addressBuilder.append(address.getCountry());
        tvAddress.setText(addressBuilder);

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
        if (customer.getAssignedEmployee() == null) {
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
                rlEmail.setVisibility(View.VISIBLE);
                break;
            case MOBILE:
                tvMobileNo.setText(contactDetail.getValue());
                rlMobileNo.setVisibility(View.VISIBLE);
                break;
            case PHONE:
                tvPhoneNo.setText(contactDetail.getValue());
                rlPhoneNo.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void showToolbarTitleSubtitle(String title, String subtitle) {
        toolbarHeaderView.bindTo(title, subtitle);
        floatHeaderView.bindTo(title, subtitle);
    }

    @Override
    public void loadCustomerPortrait() {
        ImageLoaderUtils imageLoaderUtils = new ImageLoaderUtils(getActivity());
        imageLoaderUtils.loadImage(imageLoaderUtils.buildCustomerPortraitImageUrl(
                customer.getIdentifier()), ivCustomerProfile, R.drawable.mifos_logo_new);
    }

    @Override
    public Customer.State getCustomerStatus() {
        return customer.getCurrentState();
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
        clCustomerDetails.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        showFineractNoInternetUI();
    }

    @Override
    public void showError(String errorMessage) {
        clCustomerDetails.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        showFineractErrorUI(getString(R.string.customer_details));
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

    @Override
    public void changeCustomerStatus(Customer.State state) {
        customer.setCurrentState(state);
        tvCurrentStatus.setText(state.name());
        StatusUtils.setCustomerStatusIcon(state, ivCurrentStatus, getActivity());
    }
}
