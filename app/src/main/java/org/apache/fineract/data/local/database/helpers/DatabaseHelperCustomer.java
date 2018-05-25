package org.apache.fineract.data.local.database.helpers;

import com.google.gson.Gson;

import org.apache.fineract.data.local.database.syncmodels.customer.CustomerPayload;
import org.apache.fineract.data.local.database.syncmodels.customer.CustomerPayload_Table;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.data.models.customer.CustomerPage;
import org.apache.fineract.data.models.customer.Customer_Table;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

@Singleton
public class DatabaseHelperCustomer {

    @Inject
    public DatabaseHelperCustomer() {
    }

    public void saveCustomers(final CustomerPage customerPage) {
        for (Customer customer : customerPage.getCustomers()) {
            customer.save();
        }
    }

    public Observable<CustomerPage> readAllCustomers() {
        return Observable.defer(new Callable<ObservableSource<? extends CustomerPage>>() {
            @Override
            public ObservableSource<? extends CustomerPage> call() throws Exception {
                CustomerPage customerPage = new CustomerPage();
                List<Customer> customerList = select()
                        .from(Customer.class)
                        .queryList();
                customerPage.setCustomers(customerList);
                customerPage.setTotalPages(customerPage.getCustomers().size());
                return Observable.just(customerPage);
            }
        });
    }

    public Observable<Customer> fetchCustomer(final String identifier) {
        return Observable.defer(new Callable<ObservableSource<? extends Customer>>() {
            @Override
            public ObservableSource<? extends Customer> call() throws Exception {

                Customer customer = select()
                        .from(Customer.class)
                        .where(Customer_Table.identifier.eq(identifier))
                        .querySingle();
                if (customer == null)
                    customer = new Customer();
                    //else it will throw exception and will not go in the flatMap
                return Observable.just(customer);
            }
        });

    }

    public void deleteCustomerPayload(final Customer customer) {
        CustomerPayload payload = select()
                .from(CustomerPayload.class)
                .where(CustomerPayload_Table.customerPayload.eq(new Gson().toJson(customer)))
                .querySingle();
        if (payload != null) {
            payload.delete();
        }
    }

    public Completable saveCustomerPayload(final Customer customer) {
        return Observable.defer(new Callable<ObservableSource<?>>() {
            @Override
            public ObservableSource<?> call() throws Exception {
                CustomerPayload customerPayload = new CustomerPayload(new Gson().toJson(customer));
                customerPayload.save();
                return Observable.empty();
            }
        }).ignoreElements();
    }

    public Observable<List<Customer>> fetchCustomerPayload() {
        return Observable.defer(new Callable<ObservableSource<? extends List<Customer>>>() {
            @Override
            public ObservableSource<? extends List<Customer>> call() throws Exception {
                List<CustomerPayload> customerPayloads = select()
                        .from(CustomerPayload.class)
                        .queryList();

                List<Customer> customerList = new ArrayList<>();
                Gson gson = new Gson();
                for (CustomerPayload payload: customerPayloads) {
                    customerList.add(gson.fromJson(payload.getCustomerPayload(), Customer.class));
                }
                return Observable.just(customerList);
            }
        });
    }
}
