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

/**
 * @author Rajan Maurya
 *         On 20/06/17.
 */
@Singleton
public class DataManagerCustomer {

    private final BaseApiManager baseApiManager;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManagerCustomer(BaseApiManager baseApiManager, PreferencesHelper preferencesHelper) {
        this.baseApiManager = baseApiManager;
        this.preferencesHelper = preferencesHelper;
    }

    public Observable<CustomerPage> fetchCustomers(Integer pageIndex, Integer size) {
        return baseApiManager.getCustomerApi().fetchCustomers(pageIndex, size);
    }

    public Observable<Customer> fetchCustomer(String identifier) {
        return baseApiManager.getCustomerApi().fetchCustomer(identifier);
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

    public Observable<List<Identification>> fetchIdentifications(String customerIdentifier) {
        return baseApiManager.getCustomerApi().fetchIdentification(customerIdentifier);
    }

    public Completable createIdentificationCard(String identifier, Identification identification) {
        return baseApiManager.getCustomerApi().createIdentificationCard(identifier, identification);
    }

    public Observable<List<ScanCard>> fetchIdentificationScanCards(String customerIdentifier,
            String identificationNumber) {
        return baseApiManager.getCustomerApi().fetchIdentificationScanCards(customerIdentifier,
                identificationNumber);
    }
}
