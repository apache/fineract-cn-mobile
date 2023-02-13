package org.apache.fineract.data.datamanager.api;

import org.apache.fineract.FakeRemoteDataSource;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.data.models.rolesandpermission.Role;
import org.apache.fineract.data.remote.BaseApiManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author Rajan Maurya
 * On 24/08/17.
 */
@Singleton
public class DataManagerRoles extends FineractBaseDataManager {

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
                baseApiManager.getRolesAndPermissionsService().getRoles())
                .onErrorResumeNext(
                        new Function<Throwable, ObservableSource<List<Role>>>() {
                            @Override
                            public ObservableSource<List<Role>> apply(Throwable throwable)
                                    throws Exception {
                                return Observable.just(FakeRemoteDataSource.getRoles());
                            }
                        });
    }

    public Completable createRole(Role role) {
        return authenticatedCompletableApi(
                baseApiManager.getRolesAndPermissionsService()
                        .createRole(role));
    }

    public Completable deleteRole(String identifier) {
        return authenticatedCompletableApi(
                baseApiManager.getRolesAndPermissionsService()
                        .deleteRole(identifier));
    }

    public Completable updateRole(String identifier, Role role) {
        return authenticatedCompletableApi(
                baseApiManager.getRolesAndPermissionsService()
                        .updateRole(
                                identifier, role));
    }
}
