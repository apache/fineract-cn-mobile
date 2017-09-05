package org.apache.fineract.injection.component;

import android.app.Application;
import android.content.Context;

import org.apache.fineract.FineractApplication;
import org.apache.fineract.data.datamanager.DataManagerAnonymous;
import org.apache.fineract.data.datamanager.DataManagerAuth;
import org.apache.fineract.data.datamanager.DataManagerCustomer;
import org.apache.fineract.data.datamanager.DataManagerDeposit;
import org.apache.fineract.data.datamanager.DataManagerIndividualLending;
import org.apache.fineract.data.datamanager.DataManagerLoans;
import org.apache.fineract.data.datamanager.DataManagerRoles;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.data.remote.BaseApiManager;
import org.apache.fineract.data.remote.FineractInterceptor;
import org.apache.fineract.data.remote.ReceivedCookiesInterceptor;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();
    Application application();
    DataManagerAuth dataManager();
    DataManagerCustomer dataManagerCustomer();
    DataManagerDeposit dataManagerDeposit();
    DataManagerLoans dataManagerLoans();
    DataManagerIndividualLending dataManagerIndividualLending();
    DataManagerAnonymous dataManagerAnonymous();
    DataManagerRoles dataManagerRolesAndPermissions();
    BaseApiManager baseApiManager();
    PreferencesHelper preferencesHelper();

    void inject(FineractInterceptor mifosInterceptor);
    void inject(FineractApplication mifosApplication);

    void inject(ReceivedCookiesInterceptor receivedCookiesInterceptor);

}
