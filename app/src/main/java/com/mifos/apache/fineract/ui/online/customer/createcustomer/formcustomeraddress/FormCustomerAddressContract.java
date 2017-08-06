package com.mifos.apache.fineract.ui.online.customer.createcustomer.formcustomeraddress;

import com.mifos.apache.fineract.data.models.customer.Country;
import com.mifos.apache.fineract.ui.base.MvpView;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 26/07/17.
 */

public interface FormCustomerAddressContract {

    interface View extends MvpView {

        void showCounties(List<String> countries);

        void showError(String message);
    }

    interface Presenter {

        void fetchCountries();

        List<String> filterCountriesName(List<Country> countries);

        String getCountryCode(String countryName);

        boolean isCountryNameValid(String s);
    }
}
