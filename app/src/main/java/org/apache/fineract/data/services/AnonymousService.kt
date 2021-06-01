package org.apache.fineract.data.services

import io.reactivex.Observable
import org.apache.fineract.data.models.customer.Country
import retrofit2.http.GET

/**
 * Created by Ahmad Jawid Muhammadi on 2/6/20
 */
interface AnonymousService {

    @GET("http://restcountries.eu/rest/v2/all?fields=name;alpha2Code;translations")
    fun getCountries(): Observable<List<Country?>?>?
}