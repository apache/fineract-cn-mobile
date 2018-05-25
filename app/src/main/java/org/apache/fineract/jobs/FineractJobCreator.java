package org.apache.fineract.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class FineractJobCreator implements JobCreator {

    private Map<String, Provider<Job>> jobProvider;

    @Inject
    public FineractJobCreator(Map<String, Provider<Job>> jobProvider) {
        this.jobProvider = jobProvider;
    }

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        if (jobProvider.containsKey(tag)) {
            return jobProvider.get(tag).get();
        }
        return null;
    }
}
