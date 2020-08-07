package org.apache.fineract.ui.online.customers.customerlist;

import android.content.Context;

import com.couchbase.lite.Expression;

import org.apache.fineract.R;
import org.apache.fineract.couchbase.DocumentType;
import org.apache.fineract.couchbase.SynchronizationManager;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.ConfigPersistent;
import org.apache.fineract.ui.base.BasePresenter;
import org.apache.fineract.utils.GsonUtilsKt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Rajan Maurya
 * On 20/06/17.
 */
@ConfigPersistent
public class CustomersPresenter extends BasePresenter<CustomersContract.View>
        implements CustomersContract.Presenter {

    private int customerListSize = 50;
    private boolean loadmore = false;

    private SynchronizationManager synchronizationManager;

    @Inject
    public CustomersPresenter(@ApplicationContext Context context,
                              SynchronizationManager synchronizationManager) {
        super(context);
        this.synchronizationManager = synchronizationManager;
    }

    @Override
    public void attachView(CustomersContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void fetchCustomers(Integer pageIndex, Boolean loadmore) {
        this.loadmore = loadmore;
        fetchCustomers(pageIndex, customerListSize);
    }

    @Override
    public void fetchCustomers(Integer pageIndex, Integer size) {
        checkViewAttached();
        getMvpView().showProgressbar();
        ArrayList<Customer> customerList = new ArrayList<>();

        Expression expression = Expression.property("documentType")
                .equalTo(Expression.string(DocumentType.CUSTOMER.getValue()));

        List<HashMap<String, Object>> map = synchronizationManager.getDocuments(
                expression,
                size,
                pageIndex);

        for (HashMap<String, Object> item : map) {
            Customer customer = GsonUtilsKt.convertToData(item, Customer.class);
            customerList.add(customer);
        }

        if (!loadmore && customerList.size() == 0) {
            getMvpView().showEmptyCustomers(
                    context.getString(R.string.empty_customer_list));
        } else if (loadmore && customerList.size() == 0) {
            getMvpView().showMessage(
                    context.getString(R.string.no_more_customer_available));
        } else {
            showCustomers(customerList);
        }
        getMvpView().hideProgressbar();
    }


    @Override
    public void showCustomers(List<Customer> customers) {
        if (loadmore) {
            getMvpView().showMoreCustomers(customers);
        } else {
            getMvpView().showCustomers(customers);
        }
    }

    @Override
    public void searchCustomerOnline(String query) {
//        checkViewAttached();
//        getMvpView().showProgressbar();
//        compositeDisposable.add(
//                dataManagerCustomer.fetchCustomer(query)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(new DisposableObserver<Customer>() {
//                            @Override
//                            public void onNext(Customer value) {
//                                getMvpView().hideProgressbar();
//                                getMvpView().searchCustomerList(value);
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                showExceptionError(e,
//                                        context.getString(R.string.error_finding_customer));
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        }));
    }
}
