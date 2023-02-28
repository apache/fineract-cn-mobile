package org.apache.fineract.data.services

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import org.apache.fineract.data.models.geolocation.UserLocation

/**
 * Created by Ahmad Jawid Muhammadi on 15/7/20.
 */

interface GeolocationService {

    fun getVisitedCustomerLocationListAsync(): Deferred<List<UserLocation>>

    fun saveLastKnownLocation(userLocation: UserLocation)

    fun saveLocationPathAsync(userLocation: UserLocation): Deferred<ResponseBody>
}