package com.mifos.apache.fineract.data.datamanager;

import android.util.Base64;

import com.mifos.apache.fineract.data.BaseApiManager;
import com.mifos.apache.fineract.data.local.PreferencesHelper;
import com.mifos.apache.fineract.data.models.User;

import java.nio.charset.Charset;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * @author Rajan Maurya On 16/03/17.
 */
@Singleton
public class DataManagerAuth {

    private final BaseApiManager baseApiManager;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManagerAuth(BaseApiManager baseApiManager, PreferencesHelper preferencesHelper) {
        this.baseApiManager = baseApiManager;
        this.preferencesHelper = preferencesHelper;
    }

    public Observable<User> login(String username, String password) {
        return baseApiManager.getAuthApi().login(username,
                Base64.encodeToString(password.getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP));
    }
}
