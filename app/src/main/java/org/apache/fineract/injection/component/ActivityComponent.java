package org.apache.fineract.injection.component;

import org.apache.fineract.injection.PerActivity;
import org.apache.fineract.injection.module.ActivityModule;
import org.apache.fineract.ui.offline.CustomerPayloadFragment;
import org.apache.fineract.ui.online.DashboardActivity;
import org.apache.fineract.ui.online.accounting.accounts.AccountsFragment;
import org.apache.fineract.ui.online.accounting.ledgers.LedgerFragment;
import org.apache.fineract.ui.online.customers.createcustomer.customeractivity
        .CreateCustomerActivity;
import org.apache.fineract.ui.online.customers.createcustomer.formcustomeraddress
        .FormCustomerAddressFragment;
import org.apache.fineract.ui.online.customers.customeractivities.CustomerActivitiesFragment;
import org.apache.fineract.ui.online.customers.customerdetails.CustomerDetailsFragment;
import org.apache.fineract.ui.online.customers.customerlist.CustomersFragment;
import org.apache.fineract.ui.online.customers.customerpayroll.PayrollFragment;
import org.apache.fineract.ui.online.customers.customerpayroll.editcustomerpayroll
        .EditPayrollActivity;
import org.apache.fineract.ui.online.customers.customerpayroll.editcustomerpayroll
        .EditPayrollAllocationFragment;
import org.apache.fineract.ui.online.customers.customerprofile.editcustomerprofilebottomsheet
        .EditCustomerProfileBottomSheet;
import org.apache.fineract.ui.online.customers.customertasks.CustomerTasksBottomSheetFragment;
import org.apache.fineract.ui.online.depositaccounts.createdepositaccount
        .FormDepositOverviewFragment;
import org.apache.fineract.ui.online.depositaccounts.createdepositaccount.createdepositactivity
        .CreateDepositActivity;
import org.apache.fineract.ui.online.depositaccounts.createdepositaccount
        .formdepositassignproduct.FormDepositAssignProductFragment;
import org.apache.fineract.ui.online.depositaccounts.depositaccountdetails
        .DepositAccountDetailsFragment;
import org.apache.fineract.ui.online.depositaccounts.depositaccountslist.DepositAccountsFragment;
import org.apache.fineract.ui.online.groups.creategroup.AddGroupLeaderStepFragment;
import org.apache.fineract.ui.online.groups.creategroup.AddGroupMemberStepFragment;
import org.apache.fineract.ui.online.groups.creategroup.CreateGroupActivity;
import org.apache.fineract.ui.online.groups.creategroup.GroupAddressStepFragment;
import org.apache.fineract.ui.online.groups.creategroup.GroupReviewStepFragment;
import org.apache.fineract.ui.online.groups.groupdetails.GroupDetailsFragment;
import org.apache.fineract.ui.online.groups.grouplist.GroupListFragment;
import org.apache.fineract.ui.online.groups.grouptasks.GroupTasksBottomSheetFragment;
import org.apache.fineract.ui.online.identification.createidentification.identificationactivity
        .CreateIdentificationActivity;
import org.apache.fineract.ui.online.identification.identificationdetails
        .IdentificationDetailsFragment;
import org.apache.fineract.ui.online.identification.identificationlist.IdentificationsFragment;
import org.apache.fineract.ui.online.identification.uploadidentificationscan
        .UploadIdentificationCardBottomSheet;
import org.apache.fineract.ui.online.launcher.LauncherActivity;
import org.apache.fineract.ui.online.loanaccounts.debtincomereport.DebtIncomeReportFragment;
import org.apache.fineract.ui.online.loanaccounts.loanaccountlist.LoanAccountsFragment;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.BaseFragmentDebtIncome;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.loanactivity
        .LoanApplicationActivity;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.loancosigner.LoanCoSignerFragment;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.loandetails.LoanDetailsFragment;
import org.apache.fineract.ui.online.loanaccounts.loandetails.CustomerLoanDetailsFragment;
import org.apache.fineract.ui.online.loanaccounts.plannedpayment.PlannedPaymentFragment;
import org.apache.fineract.ui.online.login.LoginActivity;
import org.apache.fineract.ui.online.review.AddLoanReviewFragment;
import org.apache.fineract.ui.online.roles.roleslist.RolesFragment;
import org.apache.fineract.ui.online.teller.TellerFragment;
import org.apache.fineract.ui.product.productlist.ProductFragment;
import org.apache.fineract.ui.product.productdetails.ProductDetailsActivity;
import org.apache.fineract.ui.product.productdetails.ProductDetailsFragment;

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

    void inject(CustomerTasksBottomSheetFragment tasksBottomSheetFragment);

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

    void inject(DashboardActivity dashboardActivity);

    void inject(RolesFragment rolesFragment);

    void inject(CustomerPayloadFragment customerPayloadFragment);

    void inject(LedgerFragment ledgerFragment);

    void inject(AccountsFragment accountsFragment);

    void inject(TellerFragment tellerFragment);

    void inject(AddLoanReviewFragment addLoanReviewFragment);

    void inject(ProductFragment productFragment);

    void inject(PayrollFragment payrollFragment);

    void inject(EditPayrollAllocationFragment editPayrollAllocationFragment);

    void inject(EditPayrollActivity editPayrollActivity);

    void inject(GroupListFragment groupListFragment);

    void inject(GroupAddressStepFragment groupAddressStepFragment);

    void inject(CreateGroupActivity createGroupActivity);

    void inject(AddGroupMemberStepFragment addGroupMemberStepFragment);

    void inject(AddGroupLeaderStepFragment addGroupLeaderStepFragment);

    void inject(GroupReviewStepFragment groupReviewStepFragment);

    void inject(GroupDetailsFragment groupDetailsFragment);

    void inject(GroupTasksBottomSheetFragment groupTasksBottomSheetFragment);

    void inject(ProductDetailsActivity productDetailsActivity);

    void inject(ProductDetailsFragment productDetailsFragment);
}

