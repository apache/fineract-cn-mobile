package org.apache.fineract.data.datamanager.contracts;

import org.apache.fineract.data.models.customer.Command;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.data.models.customer.CustomerPage;
import org.apache.fineract.data.models.customer.identification.Identification;
import org.apache.fineract.data.models.customer.identification.ScanCard;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;

public interface ManagerCustomer {

    Observable<CustomerPage> fetchCustomers(Integer pageIndex, Integer size);

    Observable<Customer> fetchCustomer(String identifier);

    Completable updateCustomer(String customerIdentifier, Customer customer);

    Observable<CustomerPage> searchCustomer(Integer pageIndex, Integer size, String term);

    Completable createCustomer(Customer customer);

    Completable customerCommand(String identifier, Command command);

    Observable<List<Command>> fetchCustomerCommands(String customerIdentifier);

    Observable<List<Identification>> fetchIdentifications(String customerIdentifier);

    Completable createIdentificationCard(String identifier, Identification identification);

    Completable updateIdentificationCard(String customerIdentifier, String identificationNumber,
                                         Identification identification);

    Observable<List<ScanCard>> fetchIdentificationScanCards(String customerIdentifier,
                                                                   String identificationNumber);

    Completable uploadIdentificationCardScan(String customerIdentifier, String identificationNumber,
                                             String scanIdentifier, String description,
                                             MultipartBody.Part file);

    Completable deleteIdentificationCardScan(String customerIdentifier, String identificationNumber,
                                             String scanIdentifier);

    Completable deleteIdentificationCard(String customerIdentifier, String identificationnumber);

    Completable uploadCustomerPortrait(String customerIdentifier, MultipartBody.Part file);

    Completable deleteCustomerPortrait(String customerIdentifier);
}
