package org.apache.fineract.ui.online.geo_location.visit_customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.apache.fineract.data.datamanager.api.DataManagerCustomer
import org.apache.fineract.data.datamanager.api.DataManagerGeolocation
import javax.inject.Inject

/**
 * Created by Ahmad Jawid Muhammadi on 20/7/20.
 */

class VisitCustomerViewModelFactory @Inject constructor(
        private val dataManagerGeolocation: DataManagerGeolocation,
        private val dataManagerCustomer: DataManagerCustomer
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VisitCustomerViewModel(dataManagerGeolocation, dataManagerCustomer) as T
    }
}