package org.apache.fineract.ui.online.customers.customertasks;

import android.content.Context;
import android.util.Log;

import org.apache.fineract.R;
import org.apache.fineract.couchbase.SynchronizationManager;
import org.apache.fineract.data.models.customer.Command;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.ConfigPersistent;
import org.apache.fineract.ui.base.BasePresenter;
import org.apache.fineract.utils.GsonUtilsKt;

import java.util.Objects;

import javax.inject.Inject;

/**
 * @author Rajan Maurya
 * On 28/07/17.
 */
@ConfigPersistent
public class CustomerTasksBottomSheetPresenter
        extends BasePresenter<CustomerTasksBottomSheetContract.View>
        implements CustomerTasksBottomSheetContract.Presenter {

    private SynchronizationManager synchronizationManager;

    @Inject
    public CustomerTasksBottomSheetPresenter(@ApplicationContext Context context,
                                             SynchronizationManager synchronizationManager) {
        super(context);

        this.synchronizationManager = synchronizationManager;
    }

    @Override
    public void attachView(CustomerTasksBottomSheetContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        synchronizationManager.closeDatabase();
    }

    @Override
    public void changeCustomerStatus(String identifier, Customer customer, Command command) {
        checkViewAttached();
        getMvpView().showProgressbar();
        try {
            switch (Objects.requireNonNull(command.getAction())) {
                case LOCK:
                    customer.setCurrentState(Customer.State.LOCKED);
                    break;
                case REOPEN:
                    customer.setCurrentState(Customer.State.PENDING);
                    break;
                case ACTIVATE:
                case UNLOCK:
                    customer.setCurrentState(Customer.State.ACTIVE);
                    break;
                case CLOSE:
                    customer.setCurrentState(Customer.State.CLOSED);
                    break;
            }
            synchronizationManager.updateDocument(identifier, GsonUtilsKt.serializeToMap(customer));
            getMvpView().hideProgressbar();
            getMvpView().statusChangedSuccessfully();
        } catch (Exception e) {
            getMvpView().hideProgressbar();
            showExceptionError(e,
                    context.getString(R.string.error_updating_status));
            Log.e("CustomerTasks", e.toString());
        }
    }
}
