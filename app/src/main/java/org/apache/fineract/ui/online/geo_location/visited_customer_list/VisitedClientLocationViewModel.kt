package org.apache.fineract.ui.online.geo_location.visited_customer_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.apache.fineract.data.Status
import org.apache.fineract.data.datamanager.api.DataManagerGeolocation
import org.apache.fineract.data.models.geolocation.UserLocation

/**
 * Created by Ahmad Jawid Muhammadi on 15/7/20.
 */

class VisitedClientLocationViewModel(
        private val dataManagerGeolocation: DataManagerGeolocation)
    : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Default)
    private var _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private var _locationList = MutableLiveData<List<UserLocation>>()
    val locationList: LiveData<List<UserLocation>> = _locationList

    fun getVisitedClientLocationList() {
        uiScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _status.value = Status.LOADING
                    _locationList.value = dataManagerGeolocation.getVisitedCustomerLocationListAsync().await()
                    _status.value = Status.DONE
                } catch (e: Exception) {
                    _status.value = Status.ERROR
                    _locationList.value = org.apache.fineract.FakeRemoteDataSource.getVisitedClientLocation()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}