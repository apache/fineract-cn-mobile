package com.mifos.apache.fineract.data.datamanager;

import com.mifos.apache.fineract.data.local.PreferencesHelper;
import com.mifos.apache.fineract.data.models.customer.Command;
import com.mifos.apache.fineract.data.models.customer.Customer;
import com.mifos.apache.fineract.data.models.customer.CustomerPage;
import com.mifos.apache.fineract.data.models.customer.identification.Identification;
import com.mifos.apache.fineract.data.models.customer.identification.ScanCard;
import com.mifos.apache.fineract.data.remote.BaseApiManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;

/**
 * @author Rajan Maurya
 *         On 20/06/17.
 */
@Singleton
public class DataManagerCustomer {

    private final BaseApiManager baseApiManager;
    private final DataManagerAuth mDataManagerAuth;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManagerCustomer(BaseApiManager baseApiManager, PreferencesHelper preferencesHelper,
            DataManagerAuth dataManagerAuth) {
        this.baseApiManager = baseApiManager;
        this.preferencesHelper = preferencesHelper;
        mDataManagerAuth = dataManagerAuth;
    }

    public Observable<CustomerPage> fetchCustomers(Integer pageIndex, Integer size) {
        return baseApiManager.getCustomerApi().fetchCustomers(pageIndex, size);
    }

    public Observable<Customer> fetchCustomer(String identifier) {
        return baseApiManager.getCustomerApi().fetchCustomer(identifier)
                .onErrorResumeNext(mDataManagerAuth
                        .refreshTokenAndRetry(
                                baseApiManager.getCustomerApi().fetchCustomer(identifier)));
    }

    public Completable updateCustomer(String customerIdentifier, Customer customer) {
        return baseApiManager.getCustomerApi().updateCustomer(customerIdentifier, customer);
    }

    public Observable<CustomerPage> searchCustomer(Integer pageIndex, Integer size, String term) {
        return baseApiManager.getCustomerApi().searchCustomer(pageIndex, size, term);
    }

    public Completable createCustomer(Customer customer) {
        return baseApiManager.getCustomerApi().createCustomer(customer);
    }

    public Completable customerCommand(String identifier, Command command) {
        return baseApiManager.getCustomerApi().customerCommand(identifier, command);
    }

    public Observable<List<Command>> fetchCustomerCommands(String customerIdentifier) {
        return baseApiManager.getCustomerApi().fetchCustomerCommands(customerIdentifier);
    }

    public Observable<List<Identification>> fetchIdentifications(String customerIdentifier) {
        return baseApiManager.getCustomerApi().fetchIdentification(customerIdentifier);
    }

    public Completable createIdentificationCard(String identifier, Identification identification) {
        return baseApiManager.getCustomerApi().createIdentificationCard(identifier, identification);
    }

    public Completable updateIdentificationCard(String customerIdentifier,
            String identificationNumber, Identification identification) {
        return baseApiManager.getCustomerApi().updateIdentificationCard(customerIdentifier,
                identificationNumber, identification);
    }

    public Observable<List<ScanCard>> fetchIdentificationScanCards(String customerIdentifier,
            String identificationNumber) {
        return baseApiManager.getCustomerApi().fetchIdentificationScanCards(customerIdentifier,
                identificationNumber);
    }

    public Completable uploadIdentificationCardScan(String customerIdentifier,
            String identificationNumber, String scanIdentifier, String description,
            MultipartBody.Part file) {
        return baseApiManager.getCustomerApi().uploadIdentificationCardScan(customerIdentifier,
                identificationNumber, scanIdentifier, description, file);
    }

    public Completable deleteIdentificationCardScan(String customerIdentifier,
            String identificationNumber, String scanIdentifier) {
        return baseApiManager.getCustomerApi().deleteIdentificationCardScan(
                customerIdentifier, identificationNumber, scanIdentifier);
    }

    public Completable deleteIdentificationCard(
            String customerIdentifier, String identificationnumber) {
        return baseApiManager.getCustomerApi()
                .deleteIdentificationCard(customerIdentifier, identificationnumber);
    }

    public Completable uploadCustomerPortrait(String customerIdentifier, MultipartBody.Part file) {
        return baseApiManager.getCustomerApi().uploadCustomerPortrait(customerIdentifier, file);
    }

    public Completable deleteCustomerPortrait(String customerIdentifier) {
        return baseApiManager.getCustomerApi().deleteCustomerPortrait(customerIdentifier);
    }
}
