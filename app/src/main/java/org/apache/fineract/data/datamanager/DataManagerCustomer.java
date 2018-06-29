package org.apache.fineract.data.datamanager;

import org.apache.fineract.FakeRemoteDataSource;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.data.models.customer.Command;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.data.models.customer.CustomerPage;
import org.apache.fineract.data.models.customer.identification.Identification;
import org.apache.fineract.data.models.customer.identification.ScanCard;
import org.apache.fineract.data.remote.BaseApiManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.MultipartBody;

/**
 * @author Rajan Maurya
 *         On 20/06/17.
 */
@Singleton
public class DataManagerCustomer extends FineractBaseDataManager {

    private final BaseApiManager baseApiManager;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManagerCustomer(BaseApiManager baseApiManager, PreferencesHelper preferencesHelper,
            DataManagerAuth dataManagerAuth) {
        super(dataManagerAuth, preferencesHelper);
        this.baseApiManager = baseApiManager;
        this.preferencesHelper = preferencesHelper;
    }

    public Observable<CustomerPage> fetchCustomers(Integer pageIndex, Integer size) {
        return authenticatedObservableApi(baseApiManager.getCustomerApi()
                .fetchCustomers(pageIndex, size))
                .onErrorResumeNext(
                        new Function<Throwable, ObservableSource<CustomerPage>>() {
                            @Override
                            public ObservableSource<CustomerPage> apply(
                                    Throwable throwable) throws Exception {
                                return Observable.just(FakeRemoteDataSource.getCustomerPage());
                            }
                        });
    }

    public Observable<Customer> fetchCustomer(String identifier) {
        return authenticatedObservableApi(baseApiManager.getCustomerApi()
                .fetchCustomer(identifier))
                .onErrorResumeNext(
                        new Function<Throwable, ObservableSource<Customer>>() {
                            @Override
                            public ObservableSource<Customer> apply(
                                    Throwable throwable) throws Exception {
                                return Observable.just(FakeRemoteDataSource.getCustomer());
                            }
                        });
    }

    public Completable updateCustomer(String customerIdentifier, Customer customer) {
        return authenticatedCompletableApi(baseApiManager.getCustomerApi()
                .updateCustomer(customerIdentifier, customer));
    }

    public Observable<CustomerPage> searchCustomer(Integer pageIndex, Integer size, String term) {
        return authenticatedObservableApi(baseApiManager.getCustomerApi()
                .searchCustomer(pageIndex, size, term));
    }

    public Completable createCustomer(Customer customer) {
        return authenticatedCompletableApi(
                baseApiManager.getCustomerApi().createCustomer(customer));
    }

    public Completable customerCommand(String identifier, Command command) {
        return authenticatedCompletableApi(baseApiManager.getCustomerApi()
                .customerCommand(identifier, command));
    }

    public Observable<List<Command>> fetchCustomerCommands(String customerIdentifier) {
        return authenticatedObservableApi(baseApiManager.getCustomerApi()
                .fetchCustomerCommands(customerIdentifier))
                .onErrorResumeNext(
                        new Function<Throwable, ObservableSource<List<Command>>>() {
                            @Override
                            public ObservableSource<List<Command>> apply(
                                    Throwable throwable) throws Exception {
                                return Observable.just(FakeRemoteDataSource.getCustomerCommands());
                            }
                        });
    }

    public Observable<List<Identification>> fetchIdentifications(String customerIdentifier) {
        return authenticatedObservableApi(baseApiManager.getCustomerApi()
                .fetchIdentification(customerIdentifier))
                .onErrorResumeNext(
                        new Function<Throwable, ObservableSource<List<Identification>>> () {
                            @Override
                            public ObservableSource<List<Identification>> apply(
                                    Throwable throwable)
                                    throws Exception {
                                return Observable.just(FakeRemoteDataSource.getIdentifications());
                            }
                        });
    }

    public Observable<Identification> searchIdentifications(String identifier, String number) {
        return authenticatedObservableApi(baseApiManager.getCustomerApi()
                    .searchIdentification(identifier, number))
                    .onErrorResumeNext(
                            new Function<Throwable, ObservableSource<Identification>>() {
                                @Override
                                public ObservableSource<Identification> apply(
                                        Throwable throwable) throws Exception {
                                    return Observable.just(FakeRemoteDataSource
                                            .getIdentifications().get(0));
                                }
                            });
    }

    public Completable createIdentificationCard(String identifier, Identification identification) {
        return authenticatedCompletableApi(baseApiManager.getCustomerApi()
                .createIdentificationCard(identifier, identification));
    }

    public Completable updateIdentificationCard(String customerIdentifier,
            String identificationNumber, Identification identification) {
        return authenticatedCompletableApi(baseApiManager.getCustomerApi().updateIdentificationCard(
                customerIdentifier, identificationNumber, identification));
    }

    public Observable<List<ScanCard>> fetchIdentificationScanCards(String customerIdentifier,
            String identificationNumber) {
        return authenticatedObservableApi(baseApiManager.getCustomerApi()
                .fetchIdentificationScanCards(customerIdentifier, identificationNumber))
                .onErrorResumeNext(
                        new Function<Throwable, ObservableSource<List<ScanCard>>>() {
                            @Override
                            public ObservableSource<List<ScanCard>> apply(
                                    Throwable throwable) throws Exception {
                                return Observable.just(FakeRemoteDataSource.getScanCards());
                            }
                        });
    }

    public Completable uploadIdentificationCardScan(String customerIdentifier,
            String identificationNumber, String scanIdentifier, String description,
            MultipartBody.Part file) {
        return authenticatedCompletableApi(baseApiManager.getCustomerApi()
                .uploadIdentificationCardScan(customerIdentifier, identificationNumber,
                        scanIdentifier, description, file));
    }

    public Completable deleteIdentificationCardScan(String customerIdentifier,
            String identificationNumber, String scanIdentifier) {
        return authenticatedCompletableApi(baseApiManager.getCustomerApi()
                .deleteIdentificationCardScan(customerIdentifier, identificationNumber,
                        scanIdentifier));
    }

    public Completable deleteIdentificationCard(
            String customerIdentifier, String identificationnumber) {
        return authenticatedCompletableApi(baseApiManager.getCustomerApi()
                .deleteIdentificationCard(customerIdentifier, identificationnumber));
    }

    public Completable uploadCustomerPortrait(String customerIdentifier, MultipartBody.Part file) {
        return authenticatedCompletableApi(baseApiManager.getCustomerApi()
                .uploadCustomerPortrait(customerIdentifier, file));
    }

    public Completable deleteCustomerPortrait(String customerIdentifier) {
        return authenticatedCompletableApi(baseApiManager.getCustomerApi()
                .deleteCustomerPortrait(customerIdentifier));
    }
}
