package org.apache.fineract;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import org.apache.fineract.injection.component.ApplicationComponent;
import org.apache.fineract.injection.component.DaggerApplicationComponent;
import org.apache.fineract.injection.module.ApplicationModule;

import io.fabric.sdk.android.Fabric;

/**
 * @author Rajan Maurya
 * On 16/03/17.
 */

public class FineractApplication extends Application {

    ApplicationComponent applicationComponent;

    private static FineractApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Fabric.with(this, new Crashlytics());
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
        }
        return applicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }
}
