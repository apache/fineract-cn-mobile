package org.apache.fineract.injection.module;

import android.app.Application;
import android.content.Context;

import org.apache.fineract.data.remote.BaseApiManager;
import org.apache.fineract.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    BaseApiManager provideBaseApiManager() {
        return new BaseApiManager(mApplication);
    }

}
