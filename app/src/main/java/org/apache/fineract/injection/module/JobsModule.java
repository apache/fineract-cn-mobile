package org.apache.fineract.injection.module;

import android.app.Application;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;

import org.apache.fineract.data.datamanager.api.DataManagerCustomer;
import org.apache.fineract.data.local.database.helpers.DatabaseHelperCustomer;
import org.apache.fineract.jobs.CreationUpdationCustomerJob;
import org.apache.fineract.jobs.StartSyncJob;
import org.apache.fineract.jobs.FineractJobCreator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

@Module
public class JobsModule {

    @Provides
    @Singleton
    public JobManager provideJobManager(Application application, FineractJobCreator
            fineractJobCreator) {
        JobManager.create(application).addJobCreator(fineractJobCreator);
        return JobManager.instance();
    }

    @Provides
    @IntoMap
    @StringKey(CreationUpdationCustomerJob.TAG)
    public Job provideSyncCustomerCreationUpdationJob(DataManagerCustomer dataManagerCustomer,
                                      DatabaseHelperCustomer databaseHelper) {
        return new CreationUpdationCustomerJob(dataManagerCustomer, databaseHelper);
    }

    @Provides
    @IntoMap
    @StringKey(StartSyncJob.TAG)
    public Job provideSyncCustomerJob(DataManagerCustomer dataManagerCustomer,
                                      DatabaseHelperCustomer databaseHelper) {
        return new StartSyncJob(dataManagerCustomer, databaseHelper);
    }
}
