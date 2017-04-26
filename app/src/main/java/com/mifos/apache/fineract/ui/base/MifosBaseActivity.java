package com.mifos.apache.fineract.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mifos.apache.fineract.MifosApplication;
import com.mifos.apache.fineract.injection.component.ActivityComponent;
import com.mifos.apache.fineract.injection.component.ConfigPersistentComponent;
import com.mifos.apache.fineract.injection.component.DaggerConfigPersistentComponent;
import com.mifos.apache.fineract.injection.module.ActivityModule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public class MifosBaseActivity extends AppCompatActivity {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();

    private ActivityComponent activityComponent;
    private long activityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        activityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(activityId)) {
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(MifosApplication.get(this).getComponent())
                    .build();
            sComponentsMap.put(activityId, configPersistentComponent);
        } else {
            configPersistentComponent = sComponentsMap.get(activityId);
        }
        activityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, activityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            sComponentsMap.remove(activityId);
        }
        super.onDestroy();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

}
