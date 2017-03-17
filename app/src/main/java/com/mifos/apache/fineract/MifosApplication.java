package com.mifos.apache.fineract;

import android.app.Application;
import android.content.Context;

import com.mifos.apache.fineract.injection.component.ApplicationComponent;
import com.mifos.apache.fineract.injection.component.DaggerApplicationComponent;
import com.mifos.apache.fineract.injection.module.ApplicationModule;

/**
 * @author Rajan Maurya
 * On 16/03/17.
 */

public class MifosApplication extends Application {

    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static MifosApplication get(Context context) {
        return (MifosApplication) context.getApplicationContext();
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
