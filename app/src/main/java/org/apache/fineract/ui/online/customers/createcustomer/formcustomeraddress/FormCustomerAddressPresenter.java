package org.apache.fineract.ui.online.customers.createcustomer.formcustomeraddress;

import android.content.Context;

import com.google.gson.Gson;

import org.apache.fineract.R;
import org.apache.fineract.data.datamanager.DataManagerAnonymous;
import org.apache.fineract.data.models.customer.Country;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.injection.ConfigPersistent;
import org.apache.fineract.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Rajan Maurya
 *         On 26/07/17.
 */
@ConfigPersistent
public class FormCustomerAddressPresenter extends BasePresenter<FormCustomerAddressContract.View>
        implements FormCustomerAddressContract.Presenter {

    private DataManagerAnonymous dataManagerAnonymous;
    private final CompositeDisposable compositeDisposable;

    private List<Country> countries;
    private Gson gson;

    @Inject
    public FormCustomerAddressPresenter(@ApplicationContext Context context,
            DataManagerAnonymous dataManager) {
        super(context);
        this.dataManagerAnonymous = dataManager;
        compositeDisposable = new CompositeDisposable();
        countries = new ArrayList<>();
        gson = new Gson();
    }

    @Override
    public void attachView(FormCustomerAddressContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    @Override
    public void fetchCountries() {
        checkViewAttached();
        compositeDisposable.add(dataManagerAnonymous.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Country>>() {

                    @Override
                    public void onNext(List<Country> countries) {
                        getMvpView().showCounties(filterCountriesName(countries));
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(context.getString(R.string.error_loading_countries));
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    @Override
    public List<String> filterCountriesName(final List<Country> countries) {
        this.countries = countries;
        return Observable.fromIterable(countries).map(new Function<Country, String>() {
            @Override
            public String apply(Country country) throws Exception {
                return country.getName();
            }
        }).toList().blockingGet();
    }

    @Override
    public String getCountryCode(final String countryName) {
        for (Country country : countries) {
            if (country.getName().equals(countryName)) {
                return country.getAlphaCode();
            }
        }
        return null;
    }

    @Override
    public boolean isCountryNameValid(String country) {
        for (Country country1 : countries) {
            if (country1.getName().equals(country)) {
                return true;
            }
        }
        return false;
    }
}
