package com.mifos.apache.fineract.injection.component;

import com.mifos.apache.fineract.injection.PerActivity;
import com.mifos.apache.fineract.injection.module.ActivityModule;
import com.mifos.apache.fineract.ui.launcher.LauncherActivity;
import com.mifos.apache.fineract.ui.customer.CustomersFragment;
import com.mifos.apache.fineract.ui.customerdeposit.CustomerDepositFragment;
import com.mifos.apache.fineract.ui.customerdetails.CustomerDetailsFragment;
import com.mifos.apache.fineract.ui.customerloans.CustomerLoansFragment;
import com.mifos.apache.fineract.ui.login.LoginActivity;

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
}
