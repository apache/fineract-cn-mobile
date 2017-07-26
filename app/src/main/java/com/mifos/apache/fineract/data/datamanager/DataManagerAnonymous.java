package com.mifos.apache.fineract.data.datamanager;

import com.mifos.apache.fineract.data.local.PreferencesHelper;
import com.mifos.apache.fineract.data.models.customer.Country;
import com.mifos.apache.fineract.data.remote.BaseApiManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * @author Rajan Maurya
 *         On 26/07/17.
 */
@Singleton
public class DataManagerAnonymous {

    private final BaseApiManager baseApiManager;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManagerAnonymous(BaseApiManager baseApiManager,
            PreferencesHelper preferencesHelper) {
        this.baseApiManager = baseApiManager;
        this.preferencesHelper = preferencesHelper;
    }

    public Observable<List<Country>> getCountries() {
        return baseApiManager.getAnonymousService().getCountries();
    }
}
