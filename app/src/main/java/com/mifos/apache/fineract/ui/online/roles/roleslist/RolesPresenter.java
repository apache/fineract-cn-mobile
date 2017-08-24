package com.mifos.apache.fineract.ui.online.roles.roleslist;

import android.content.Context;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.datamanager.DataManagerRoles;
import com.mifos.apache.fineract.data.models.rolesandpermission.Role;
import com.mifos.apache.fineract.injection.ApplicationContext;
import com.mifos.apache.fineract.injection.ConfigPersistent;
import com.mifos.apache.fineract.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Rajan Maurya
 *         On 24/08/17.
 */
@ConfigPersistent
public class RolesPresenter extends BasePresenter<RolesContract.View>
        implements RolesContract.Presenter {

    private final DataManagerRoles dataManagerRoles;
    private CompositeDisposable compositeDisposable;

    @Inject
    protected RolesPresenter(@ApplicationContext Context context,
            DataManagerRoles dataManagerRoles) {
        super(context);
        this.dataManagerRoles = dataManagerRoles;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void fetchRoles() {
        checkViewAttached();
        getMvpView().showProgressbar();
        compositeDisposable.add(dataManagerRoles.getRoles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Role>>() {
                    @Override
                    public void onNext(List<Role> roles) {
                        getMvpView().hideProgressbar();
                        if (!roles.isEmpty()) {
                            getMvpView().showRoles(roles);
                        } else {
                            getMvpView().showEmptyRoles();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressbar();
                        getMvpView().showError(context.getString(R.string.error_fetching_roles));
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }
}
