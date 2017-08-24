package com.mifos.apache.fineract.data.datamanager;

import com.mifos.apache.fineract.data.local.PreferencesHelper;
import com.mifos.apache.fineract.data.models.rolesandpermission.Role;
import com.mifos.apache.fineract.data.remote.BaseApiManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * @author Rajan Maurya
 *         On 24/08/17.
 */
@Singleton
public class DataManagerRoles extends MifosBaseDataManager {

    private final BaseApiManager baseApiManager;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManagerRoles(BaseApiManager baseApiManager,
            DataManagerAuth dataManagerAuth, PreferencesHelper preferencesHelper) {
        super(dataManagerAuth, preferencesHelper);
        this.baseApiManager = baseApiManager;
        this.preferencesHelper = preferencesHelper;
    }

    public Observable<List<Role>> getRoles() {
        return authenticatedObservableApi(
                baseApiManager.getRolesAndPermissionsService().getRoles());
    }
}
