package com.mifos.apache.fineract.injection.component;

import com.mifos.apache.fineract.injection.ConfigPersistent;
import com.mifos.apache.fineract.injection.module.ActivityModule;

import dagger.Component;

/**
 * A dagger component that will live during the lifecycle of an Activity but it won't
 * be destroy during configuration changes.
 * Check {@link com.mifos.apache.fineract.ui.base.MifosBaseActivity} to see how this components
 * survives configuration changes.
 * Use the {@link ConfigPersistent} scope to annotate dependencies that need to survive
 * configuration changes (for example Presenters).
 */
@ConfigPersistent
@Component(dependencies = ApplicationComponent.class)
public interface ConfigPersistentComponent {

    ActivityComponent activityComponent(ActivityModule activityModule);

}