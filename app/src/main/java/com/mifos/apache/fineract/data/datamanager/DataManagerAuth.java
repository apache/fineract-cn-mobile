package com.mifos.apache.fineract.data.datamanager;

import com.mifos.apache.fineract.data.BaseApiManager;
import com.mifos.apache.fineract.data.local.PreferencesHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Rajan Maurya On 16/03/17.
 */
@Singleton
public class DataManagerAuth {

    private final BaseApiManager baseApiManager;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManagerAuth(BaseApiManager baseApiManager, PreferencesHelper preferencesHelper) {
        this.baseApiManager  = baseApiManager;
        this.preferencesHelper = preferencesHelper;
    }
}
