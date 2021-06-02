package org.apache.fineract.ui.online.customers.createcustomer.customeractivity;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;

import org.apache.fineract.couchbase.SynchronizationManager;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.ConfigPersistent;
import org.apache.fineract.ui.base.BasePresenter;
import org.apache.fineract.utils.DateUtils;
import org.apache.fineract.utils.GsonUtilsKt;

import java.util.Objects;

import javax.inject.Inject;

/**
 * @author Rajan Maurya
 * On 27/07/17.
 */
@ConfigPersistent
public class CreateCustomerPresenter extends BasePresenter<CreateCustomerContract.View>
        implements CreateCustomerContract.Presenter {

    private SynchronizationManager synchronizationManager;
    private PreferencesHelper preferencesHelper;

    @Inject
    public CreateCustomerPresenter(@ApplicationContext Context context,
                                   SynchronizationManager synchronizationManager,
                                   PreferencesHelper preferencesHelper) {
        super(context);
        this.synchronizationManager = synchronizationManager;
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public void createCustomer(Customer customer) {
        try {
            customer.setCreatedBy(preferencesHelper.getUserName());
            customer.setCreatedOn(DateUtils.getCurrentDate());
            customer.setLastModifiedBy(preferencesHelper.getUserName());
            customer.setLastModifiedOn(DateUtils.getCurrentDate());
            synchronizationManager.saveDocument(Objects.requireNonNull(customer.getIdentifier()),
                    GsonUtilsKt.serializeToMap(customer));
        } catch (CouchbaseLiteException e) {
            Log.e("CreateCustomerPresenter", e.toString());
        }
        getMvpView().customerCreatedSuccessfully();
    }

    @Override
    public void updateCustomer(String customerIdentifier, Customer customer) {
        customer.setLastModifiedBy(preferencesHelper.getUserName());
        customer.setLastModifiedOn(DateUtils.getCurrentDate());
        synchronizationManager.updateDocument(Objects.requireNonNull(customer.getIdentifier()),
                GsonUtilsKt.serializeToMap(customer));
        getMvpView().customerUpdatedSuccessfully();
    }
}
