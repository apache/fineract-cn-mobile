package org.apache.fineract.data.datamanager.api

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.data.models.geolocation.UserLocation
import org.apache.fineract.data.remote.BaseApiManager
import javax.inject.Inject

/**
 * Created by Ahmad Jawid Muhammadi on 15/7/20.
 */

class DataManagerGeolocation @Inject constructor(private val baseManagerApi: BaseApiManager,
                                                 val dataManagerAuth: DataManagerAuth,
                                                 val preferencesHelper: PreferencesHelper)
    : FineractBaseDataManager(dataManagerAuth, preferencesHelper) {


    fun getVisitedCustomerLocationListAsync(): Deferred<List<UserLocation>> {
        return baseManagerApi.geolocationService.getVisitedCustomerLocationListAsync()
    }

    fun saveLastKnownLocation(userLocation: UserLocation) {
        baseManagerApi.geolocationService.saveLastKnownLocation(userLocation)
    }

    fun saveLocationPathAsync(userLocation: UserLocation): Deferred<ResponseBody> {
        return baseManagerApi.geolocationService.saveLocationPathAsync(userLocation)
    }

}
