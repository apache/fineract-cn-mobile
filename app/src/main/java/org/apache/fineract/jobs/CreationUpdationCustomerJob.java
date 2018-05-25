package org.apache.fineract.jobs;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import org.apache.fineract.data.datamanager.api.DataManagerCustomer;
import org.apache.fineract.data.datamanager.contracts.ManagerCustomer;
import org.apache.fineract.data.local.database.helpers.DatabaseHelperCustomer;
import org.apache.fineract.data.models.customer.Customer;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class CreationUpdationCustomerJob extends Job {

    private ManagerCustomer dataManagerCustomer;
    private DatabaseHelperCustomer databaseHelper;
    public static final String TAG = "CUSTOMERS_CREATION_UPDATION";

    @Inject
    public CreationUpdationCustomerJob(DataManagerCustomer dataManagerCustomer,
                                       DatabaseHelperCustomer databaseHelper) {
        this.dataManagerCustomer = dataManagerCustomer;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        syncCustomer();
        return Result.SUCCESS;
    }

    @SuppressLint("CheckResult")
    private void syncCustomer() {
        databaseHelper.fetchCustomerPayload()
                .flatMap(new Function<List<Customer>, ObservableSource<Customer>>() {
                    @Override
                    public ObservableSource<Customer> apply(List<Customer> customers) throws Exception {
                        return Observable.fromIterable(customers);
                    }
                })
                .flatMapCompletable(new Function<Customer, CompletableSource>() {
                    @Override
                    public CompletableSource apply(Customer customer) throws Exception {
                        return dataManagerCustomer.createCustomer(customer);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        // getting the updates once the data is synced to the server.
                        StartSyncJob.scheduleItNow();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(StartSyncJob.class.getSimpleName(), e.toString());
                    }
                });

    }

    public static void schedulePeriodic() {
        new JobRequest.Builder(CreationUpdationCustomerJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15),
                        TimeUnit.MINUTES.toMillis(10))
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }
}
