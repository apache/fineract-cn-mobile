package org.apache.fineract;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.evernote.android.job.JobManager;
import com.mifos.mobile.passcode.utils.ForegroundChecker;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.apache.fineract.injection.component.ApplicationComponent;
import org.apache.fineract.injection.component.DaggerApplicationComponent;
import org.apache.fineract.injection.module.ApplicationModule;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

/**
 * @author Rajan Maurya
 * On 16/03/17.
 */

public class FineractApplication extends Application {

    ApplicationComponent applicationComponent;

    private static FineractApplication instance;

    @Inject
    JobManager jobManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Fabric.with(this, new Crashlytics());
        FlowManager.init(this);
        ForegroundChecker.init(this);
    }

    public static Context getContext() {
        return instance;
    }

    public static FineractApplication get(Context context) {
        return (FineractApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
            applicationComponent.inject(this);
        }
        return applicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }
}
