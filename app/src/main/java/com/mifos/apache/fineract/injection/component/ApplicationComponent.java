package com.mifos.apache.fineract.injection.component;

import android.app.Application;
import android.content.Context;

import com.mifos.apache.fineract.data.datamanager.DataManagerAuth;
import com.mifos.apache.fineract.data.local.PreferencesHelper;
import com.mifos.apache.fineract.injection.ApplicationContext;
import com.mifos.apache.fineract.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();
    Application application();
    DataManagerAuth dataManager();
    PreferencesHelper preferencesHelper();
}
