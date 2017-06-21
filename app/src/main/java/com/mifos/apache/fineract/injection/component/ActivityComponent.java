package com.mifos.apache.fineract.injection.component;

import com.mifos.apache.fineract.injection.PerActivity;
import com.mifos.apache.fineract.injection.module.ActivityModule;
import com.mifos.apache.fineract.ui.LauncherActivity;
import com.mifos.apache.fineract.ui.customer.CustomersFragment;
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
}
