package org.apache.fineract.data.datamanager.database;

import org.apache.fineract.data.datamanager.contracts.ManagerCustomer;
import org.apache.fineract.data.local.database.helpers.DatabaseHelperCustomer;
import org.apache.fineract.data.models.customer.Command;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.data.models.customer.CustomerPage;
import org.apache.fineract.data.models.customer.identification.Identification;
import org.apache.fineract.data.models.customer.identification.ScanCard;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;

@Singleton
public class DbManagerCustomer implements ManagerCustomer {

    private DatabaseHelperCustomer databaseHelperCustomer;

    @Inject
    public DbManagerCustomer(DatabaseHelperCustomer databaseHelperCustomer) {
        this.databaseHelperCustomer = databaseHelperCustomer;
    }

    @Override
    public Observable<CustomerPage> fetchCustomers(Integer pageIndex, Integer size) {
        return databaseHelperCustomer.readAllCustomers();
    }

    @Override
    public Observable<Customer> fetchCustomer(final String identifier) {
        return databaseHelperCustomer.fetchCustomer(identifier);
    }

    @Override
    public Completable updateCustomer(String customerIdentifier, Customer customer) {
        return null;
    }

    @Override
    public Observable<CustomerPage> searchCustomer(Integer pageIndex, Integer size, String term) {
        return null;
    }

    @Override
    public Completable createCustomer(Customer customer) {
        return databaseHelperCustomer.saveCustomerPayload(customer);
    }

    @Override
    public Completable customerCommand(String identifier, Command command) {
        return null;
    }

    @Override
    public Observable<List<Command>> fetchCustomerCommands(String customerIdentifier) {
        return null;
    }

    @Override
    public Observable<List<Identification>> fetchIdentifications(String customerIdentifier) {
        return null;
    }

    @Override
    public Completable createIdentificationCard(String identifier, Identification identification) {
        return null;
    }

    @Override
    public Completable updateIdentificationCard(String customerIdentifier,
            String identificationNumber, Identification identification) {
        return null;
    }

    @Override
    public Observable<List<ScanCard>> fetchIdentificationScanCards(String customerIdentifier,
            String identificationNumber) {
        return null;
    }

    @Override
    public Completable uploadIdentificationCardScan(String customerIdentifier, String
            identificationNumber, String scanIdentifier, String description, MultipartBody.Part
            file) {
        return null;
    }

    @Override
    public Completable deleteIdentificationCardScan(String customerIdentifier, String
            identificationNumber, String scanIdentifier) {
        return null;
    }

    @Override
    public Completable deleteIdentificationCard(String customerIdentifier, String
            identificationnumber) {
        return null;
    }

    @Override
    public Completable uploadCustomerPortrait(String customerIdentifier, MultipartBody.Part file) {
        return null;
    }

    @Override
    public Completable deleteCustomerPortrait(String customerIdentifier) {
        return null;
    }
}
