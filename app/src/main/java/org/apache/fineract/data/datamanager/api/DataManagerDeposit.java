package org.apache.fineract.data.datamanager.api;

import org.apache.fineract.FakeRemoteDataSource;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.data.models.deposit.DepositAccount;
import org.apache.fineract.data.models.deposit.ProductDefinition;
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
 *         On 07/07/17.
 */
@Singleton
public class DataManagerDeposit extends FineractBaseDataManager {

    private final BaseApiManager baseApiManager;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManagerDeposit(BaseApiManager baseApiManager, PreferencesHelper preferencesHelper,
            DataManagerAuth dataManagerAuth) {
        super(dataManagerAuth, preferencesHelper);
        this.baseApiManager = baseApiManager;
        this.preferencesHelper = preferencesHelper;
    }

    public Observable<List<DepositAccount>> getCustomerDepositAccounts(
            String customerIdentifier) {
        return authenticatedObservableApi(baseApiManager.getDepositApi()
                .fetchCustomersDeposits(customerIdentifier))
                .onErrorResumeNext(
                        new Function<Throwable, ObservableSource<List<DepositAccount>>>() {
                            @Override
                            public ObservableSource<List<DepositAccount>> apply(
                                    Throwable throwable)
                                    throws Exception {
                                return Observable.just(FakeRemoteDataSource
                                        .getCustomerDepositAccounts());
                            }
                        });
    }

    public Observable<DepositAccount> getCustomerDepositAccountDetails(
            String accountIdentifier) {
        return authenticatedObservableApi(baseApiManager.getDepositApi()
                .fetchCustomerDepositDetails(accountIdentifier))
                .onErrorResumeNext(
                        new Function<Throwable, ObservableSource<DepositAccount>>() {
                            @Override
                            public ObservableSource<DepositAccount> apply(
                                    Throwable throwable) throws Exception {
                                return Observable.just(FakeRemoteDataSource
                                        .getCustomerDepositAccounts().get(0));
                            }
                        });
    }

    public Observable<List<ProductDefinition>> fetchProductDefinitions() {
        return authenticatedObservableApi(baseApiManager.getDepositApi().fetchProductDefinitions());
    }

    public Completable createDepositAccount(DepositAccount depositAccount) {
        return authenticatedCompletableApi(baseApiManager.getDepositApi()
                .createDepositAccount(depositAccount));
    }

    public Completable updateDepositAccount(String accountIdentifier,
            DepositAccount depositAccount) {
        return authenticatedCompletableApi(baseApiManager.getDepositApi()
                .updateDepositAccount(accountIdentifier, depositAccount));
    }
}
