package org.apache.fineract.injection.component;

import android.app.Application;
import android.content.Context;

import com.evernote.android.job.JobManager;

import org.apache.fineract.FineractApplication;
import org.apache.fineract.data.datamanager.api.DataManagerAnonymous;
import org.apache.fineract.data.datamanager.api.DataManagerAuth;
import org.apache.fineract.data.datamanager.api.DataManagerCustomer;
import org.apache.fineract.data.datamanager.api.DataManagerDeposit;
import org.apache.fineract.data.datamanager.api.DataManagerIndividualLending;
import org.apache.fineract.data.datamanager.api.DataManagerLoans;
import org.apache.fineract.data.datamanager.api.DataManagerProduct;
import org.apache.fineract.data.datamanager.api.DataManagerRoles;
import org.apache.fineract.data.datamanager.database.DbManagerCustomer;
import org.apache.fineract.data.datamanager.DataManagerAccounting;
import org.apache.fineract.data.datamanager.DataManagerTeller;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.data.local.database.helpers.DatabaseHelperCustomer;
import org.apache.fineract.data.remote.BaseApiManager;
import org.apache.fineract.data.remote.FineractInterceptor;
import org.apache.fineract.data.remote.ReceivedCookiesInterceptor;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.module.ApplicationModule;
import org.apache.fineract.injection.module.JobsModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, JobsModule.class})
public interface ApplicationComponent {

    @ApplicationContext
    Context context();
    Application application();
    JobManager jobManager();
    DataManagerAuth dataManager();
    DataManagerCustomer dataManagerCustomer();
    DataManagerProduct dataManagerProduct();
    DbManagerCustomer bbManagerCustomer();
    DatabaseHelperCustomer customerDatabaseHelper();
    DataManagerDeposit dataManagerDeposit();
    DataManagerLoans dataManagerLoans();
    DataManagerIndividualLending dataManagerIndividualLending();
    DataManagerAnonymous dataManagerAnonymous();
    DataManagerRoles dataManagerRolesAndPermissions();
    DataManagerAccounting dataManagerAccounting();
    DataManagerTeller dataManagerTeller();
    BaseApiManager baseApiManager();
    PreferencesHelper preferencesHelper();

    void inject(FineractInterceptor mifosInterceptor);
    void inject(FineractApplication mifosApplication);

    void inject(ReceivedCookiesInterceptor receivedCookiesInterceptor);
    void inject(JobManager jobManager);
}
