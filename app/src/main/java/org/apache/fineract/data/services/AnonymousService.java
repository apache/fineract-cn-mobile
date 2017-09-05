package org.apache.fineract.data.services;

import org.apache.fineract.data.models.customer.Country;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author Rajan Maurya
 *         On 26/07/17.
 */
public interface AnonymousService {

    @GET("http://restcountries.eu/rest/v2/all?fields=name;alpha2Code;translations")
    Observable<List<Country>> getCountries();
}
