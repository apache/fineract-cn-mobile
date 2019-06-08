package org.apache.fineract.jobs;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import org.apache.fineract.data.datamanager.api.DataManagerCustomer;
import org.apache.fineract.data.datamanager.contracts.ManagerCustomer;
import org.apache.fineract.data.local.database.helpers.DatabaseHelperCustomer;
import org.apache.fineract.data.models.customer.CustomerPage;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class StartSyncJob extends Job {

    public static final String TAG = "CUSTOMERS_SYNC";
    public static final String STATUS = "status";
    private ManagerCustomer dataManagerCustomer;
    private DatabaseHelperCustomer dbHelper;

    @Inject
    public StartSyncJob(DataManagerCustomer dataManagerCustomer, DatabaseHelperCustomer
            dbHelper) {
        this.dataManagerCustomer = dataManagerCustomer;
        this.dbHelper = dbHelper;
    }

    @SuppressLint("CheckResult")
    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        updateStatus(JobStatus.STARTED);
        dataManagerCustomer.fetchCustomers(0, 50)
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<CustomerPage>() {
                    @Override
                    public void onNext(CustomerPage customerPage) {
                        dbHelper.saveCustomers(customerPage);
                        updateStatus(JobStatus.ENDED);
                        //starting other jobs once it finishes the StartSyncJob
                        StartSyncJob.schedulePeriodic();
                        CreationUpdationCustomerJob.schedulePeriodic();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return Result.SUCCESS;
    }

    private void updateStatus(JobStatus status) {
        Intent intent = new Intent("org.apache.fineract.JobsReceiver");
        intent.putExtra(STATUS, status);
        getContext().sendBroadcast(intent);
    }

    public static void schedulePeriodic() {
        new JobRequest.Builder(StartSyncJob.TAG)
                .setPeriodic(TimeUnit.DAYS.toMillis(1),
                        TimeUnit.MINUTES.toMillis(10))
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }

    public static void scheduleItNow() {
        new JobRequest.Builder(StartSyncJob.TAG)
                .startNow()
                .build()
                .schedule();
    }

    public enum JobStatus {
        STARTED,
        ENDED
    }
}
