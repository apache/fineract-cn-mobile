package org.apache.fineract.ui.online.customers.customerdetails;

import android.content.Context;

import org.apache.fineract.couchbase.SynchronizationManager;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.ConfigPersistent;
import org.apache.fineract.ui.base.BasePresenter;
import org.apache.fineract.utils.GsonUtilsKt;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * @author Rajan Maurya
 * On 26/06/17.
 */
@ConfigPersistent
public class CustomerDetailsPresenter extends BasePresenter<CustomerDetailsContract.View>
        implements CustomerDetailsContract.Presenter {

    private SynchronizationManager synchronizationManager;

    @Inject
    public CustomerDetailsPresenter(@ApplicationContext Context context,
                                    SynchronizationManager synchronizationManager) {
        super(context);
        this.synchronizationManager = synchronizationManager;
    }

    @Override
    public void attachView(CustomerDetailsContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void loanCustomerDetails(String identifier) {
        checkViewAttached();
        getMvpView().showProgressbar();
        HashMap<String, Object> item = synchronizationManager.getDocumentById(identifier);
        Customer customer = GsonUtilsKt.convertToData(item, Customer.class);
        getMvpView().showCustomerDetails(customer);
        getMvpView().hideProgressbar();
    }
}
