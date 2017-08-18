package com.mifos.apache.fineract.injection.component;

import com.mifos.apache.fineract.injection.PerActivity;
import com.mifos.apache.fineract.injection.module.ActivityModule;
import com.mifos.apache.fineract.ui.online.customers.createcustomer.customeractivity.CreateCustomerActivity;
import com.mifos.apache.fineract.ui.online.customers.createcustomer.formcustomeraddress.FormCustomerAddressFragment;

import com.mifos.apache.fineract.ui.online.customers.customeractivities.CustomerActivitiesFragment;
import com.mifos.apache.fineract.ui.online.customers.customerlist.CustomersFragment;
import com.mifos.apache.fineract.ui.online.customers.customerprofile
        .editcustomerprofilebottomsheet.EditCustomerProfileBottomSheet;
import com.mifos.apache.fineract.ui.online.depositaccounts.createdepositaccount.FormDepositOverviewFragment;

import com.mifos.apache.fineract.ui.online.depositaccounts.createdepositaccount.createdepositactivity
        .CreateDepositActivity;
import com.mifos.apache.fineract.ui.online.depositaccounts.createdepositaccount.formdepositassignproduct
        .FormDepositAssignProductFragment;
import com.mifos.apache.fineract.ui.online.depositaccounts.depositaccountslist.DepositAccountsFragment;
import com.mifos.apache.fineract.ui.online.customers.customerdetails.CustomerDetailsFragment;
import com.mifos.apache.fineract.ui.online.loans.loanaccountlist.LoanAccountsFragment;
import com.mifos.apache.fineract.ui.online.debtincomereport.DebtIncomeReportFragment;
import com.mifos.apache.fineract.ui.online.depositaccounts.depositaccountdetails.DepositAccountDetailsFragment;
import com.mifos.apache.fineract.ui.online.identification.createidentification.identificationactivity.CreateIdentificationActivity;
import com.mifos.apache.fineract.ui.online.identification.identificationdetails.IdentificationDetailsFragment;
import com.mifos.apache.fineract.ui.online.identification.identificationlist.IdentificationsFragment;
import com.mifos.apache.fineract.ui.online.identification.uploadidentificationscan.UploadIdentificationCardBottomSheet;
import com.mifos.apache.fineract.ui.online.launcher.LauncherActivity;
import com.mifos.apache.fineract.ui.online.loans.loanapplication.BaseFragmentDebtIncome;
import com.mifos.apache.fineract.ui.online.loans.loanapplication.loanactivity.LoanApplicationActivity;
import com.mifos.apache.fineract.ui.online.loans.loanapplication.loancosigner.LoanCoSignerFragment;
import com.mifos.apache.fineract.ui.online.loans.loanapplication.loandetails.LoanDetailsFragment;
import com.mifos.apache.fineract.ui.online.loans.loandetails.CustomerLoanDetailsFragment;
import com.mifos.apache.fineract.ui.online.login.LoginActivity;
import com.mifos.apache.fineract.ui.online.plannedpayment.PlannedPaymentFragment;
import com.mifos.apache.fineract.ui.online.tasks.TasksBottomSheetFragment;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(LoginActivity loginActivity);

    void inject(LauncherActivity launcherActivity);

    void inject(CustomersFragment customersFragment);

    void inject(CustomerDetailsFragment customerDetailsFragment);

    void inject(DepositAccountsFragment customerDepositFragment);

    void inject(LoanAccountsFragment customerLoansFragment);

    void inject(CustomerLoanDetailsFragment customerLoanDetailsFragment);

    void inject(DepositAccountDetailsFragment customerDepositDetailsFragment);

    void inject(PlannedPaymentFragment plannedPaymentFragment);

    void inject(LoanApplicationActivity loanApplicationActivity);

    void inject(LoanDetailsFragment loanDetailsFragment);

    void inject(BaseFragmentDebtIncome loanDebtIncomeFragment);

    void inject(LoanCoSignerFragment loanCoSignerFragment);

    void inject(CreateCustomerActivity createCustomerActivity);

    void inject(FormCustomerAddressFragment formCustomerAddressFragment);

    void inject(TasksBottomSheetFragment tasksBottomSheetFragment);

    void inject(DebtIncomeReportFragment debtIncomeReportFragment);

    void inject(IdentificationsFragment identificationsFragment);

    void inject(CreateIdentificationActivity createIdentificationActivity);

    void inject(IdentificationDetailsFragment identificationDetailsFragment);

    void inject(UploadIdentificationCardBottomSheet uploadIdentificationCardBottomSheet);

    void inject(EditCustomerProfileBottomSheet editCustomerProfileBottomSheet);

    void inject(FormDepositAssignProductFragment formDepositAssignProductFragment);

    void inject(FormDepositOverviewFragment formDepositOverviewFragment);

    void inject(CreateDepositActivity createDepositActivity);

    void inject(CustomerActivitiesFragment customerActivitiesFragment);
}
