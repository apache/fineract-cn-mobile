package org.apache.fineract.ui.online.geo_location.visited_customer_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.apache.fineract.data.datamanager.api.DataManagerGeolocation
import javax.inject.Inject

/**
 * Created by Ahmad Jawid Muhammadi on 15/7/20.
 */

class VisitedClientLocationViewModelFactory @Inject constructor
(private val geolocation: DataManagerGeolocation) :
        ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VisitedClientLocationViewModel(geolocation) as T
    }
}