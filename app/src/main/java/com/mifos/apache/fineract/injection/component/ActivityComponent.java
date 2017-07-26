package com.mifos.apache.fineract.injection.component;

import com.mifos.apache.fineract.injection.PerActivity;
import com.mifos.apache.fineract.injection.module.ActivityModule;
import com.mifos.apache.fineract.ui.online.createcustomer.customeractivity.CreateCustomerActivity;
import com.mifos.apache.fineract.ui.online.createcustomer.formcustomeraddress
        .FormCustomerAddressFragment;
import com.mifos.apache.fineract.ui.online.customer.CustomersFragment;
import com.mifos.apache.fineract.ui.online.customerdeposit.CustomerDepositFragment;
import com.mifos.apache.fineract.ui.online.customerdetails.CustomerDetailsFragment;
import com.mifos.apache.fineract.ui.online.customerloans.CustomerLoansFragment;
import com.mifos.apache.fineract.ui.online.depositdetails.CustomerDepositDetailsFragment;
import com.mifos.apache.fineract.ui.online.launcher.LauncherActivity;
import com.mifos.apache.fineract.ui.online.loanapplication.BaseFragmentDebtIncome;
import com.mifos.apache.fineract.ui.online.loanapplication.loanactivity.LoanApplicationActivity;
import com.mifos.apache.fineract.ui.online.loanapplication.loancosigner.LoanCoSignerFragment;
import com.mifos.apache.fineract.ui.online.loanapplication.loandetails.LoanDetailsFragment;
import com.mifos.apache.fineract.ui.online.loandetails.CustomerLoanDetailsFragment;
import com.mifos.apache.fineract.ui.online.login.LoginActivity;
import com.mifos.apache.fineract.ui.online.plannedpayment.PlannedPaymentFragment;

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

    void inject(CustomerDepositFragment customerDepositFragment);

    void inject(CustomerLoansFragment customerLoansFragment);

    void inject(CustomerLoanDetailsFragment customerLoanDetailsFragment);

    void inject(CustomerDepositDetailsFragment customerDepositDetailsFragment);

    void inject(PlannedPaymentFragment plannedPaymentFragment);

    void inject(LoanApplicationActivity loanApplicationActivity);

    void inject(LoanDetailsFragment loanDetailsFragment);

    void inject(BaseFragmentDebtIncome loanDebtIncomeFragment);

    void inject(LoanCoSignerFragment loanCoSignerFragment);

    void inject(CreateCustomerActivity createCustomerActivity);

    void inject(FormCustomerAddressFragment formCustomerAddressFragment);
}
