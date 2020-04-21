package org.apache.fineract.ui.online.geo_location.visit_customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import org.apache.fineract.data.datamanager.api.DataManagerCustomer
import org.apache.fineract.data.datamanager.api.DataManagerGeolocation
import org.apache.fineract.data.models.customer.Customer
import org.apache.fineract.data.models.customer.CustomerPage
import org.apache.fineract.data.models.geolocation.UserLocation

/**
 * Created by Ahmad Jawid Muhammadi on 20/7/20.
 */

class VisitCustomerViewModel constructor(
        private val dataManagerGeolocation: DataManagerGeolocation,
        private val dataManagerCustomer: DataManagerCustomer
) : ViewModel() {

    private val job = Job()
    private val uiScope = CoroutineScope(job + Dispatchers.Default)
    private val compositeDisposable = CompositeDisposable()

    private var _customerList = MutableLiveData<List<Customer>>()
    val customerList: LiveData<List<Customer>> = _customerList

    fun saveLastKnownLocation(userLocation: UserLocation) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataManagerGeolocation.saveLastKnownLocation(userLocation)
            }
        }
    }

    fun saveLocationPath(userLocation: UserLocation) {
        uiScope.launch {
            try {
                dataManagerGeolocation.saveLocationPathAsync(userLocation).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchCustomers(pageIndex: Int, size: Int) {
        compositeDisposable.add(dataManagerCustomer.fetchCustomers(pageIndex, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<CustomerPage?>() {
                    override fun onNext(customerPage: CustomerPage) {
                        _customerList.value = customerPage.customers
                    }

                    override fun onError(throwable: Throwable) {}

                    override fun onComplete() {}
                })
        )
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}