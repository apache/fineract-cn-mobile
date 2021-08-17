package org.apache.fineract.ui.product.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.couchbase.lite.Expression
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import org.apache.fineract.couchbase.DocumentType
import org.apache.fineract.couchbase.SynchronizationManager
import org.apache.fineract.data.Status
import org.apache.fineract.data.datamanager.api.DataManagerAnonymous
import org.apache.fineract.data.datamanager.api.DataManagerProduct
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.data.models.customer.Country
import org.apache.fineract.data.models.product.Product
import org.apache.fineract.data.models.product.ProductPage
import org.apache.fineract.utils.DateUtils
import org.apache.fineract.utils.serializeToMap
import org.apache.fineract.utils.toDataClass

/*
 * Created by Varun Jain on 11/August/2021
*/


class ProductViewModel constructor(private val synchronizationManager: SynchronizationManager,
                                   private val dataManagerAnonymous: DataManagerAnonymous,
                                   private val preferencesHelper: PreferencesHelper,
                                   private val dataManagerProduct: DataManagerProduct
) : ViewModel() {

    var productList = MutableLiveData<ArrayList<Product>>()
    private var viewModelJob = Job()

    // Create a Coroutine scope using a job to be able to cancel when needed
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.IO)
    private var _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    @SuppressLint("CheckResult")
    fun getProducts(): MutableLiveData<ArrayList<Product>>? {
        val expression = Expression.property("documentType")
            .equalTo(Expression.string(DocumentType.PRODUCT.value))
            .and(Expression.property("identifier").notEqualTo(Expression.string("null")))
        val hashMapList = synchronizationManager.getDocuments(expression)
        if (hashMapList?.isEmpty() == null) {
            return null
        }
        val list = arrayListOf<Product>()
        for (map in hashMapList){
            list.add(map.toDataClass())
        }
        productList.value = list
        return productList
    }

    fun searchProducts(products: ArrayList<Product>, query: String, searchedProduct: (ArrayList<Product>) -> Unit) {
        searchedProduct(ArrayList(Observable.fromIterable(products).filter { product -> product.identifier?.toLowerCase()?.contains(query.toLowerCase()).toString().toBoolean() }.toList().blockingGet()))
    }

    @SuppressLint("CheckResult")
    fun createProduct(product: Product) {
        uiScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _status.value = Status.LOADING
                    product.createdBy = preferencesHelper.userName
                    product.createdOn = DateUtils.getCurrentDate()
                    product.lastModifiedBy = preferencesHelper.userName
                    product.lastModifiedOn = DateUtils.getCurrentDate()
                    synchronizationManager.saveDocument(product.identifier!!, product.serializeToMap())
                    _status.value = Status.DONE
                } catch (exception: Exception) {
                    _status.value = Status.ERROR
                }
            }
        }
    }

    fun updateProduct(product: Product) {
        uiScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _status.value = Status.LOADING
                    product.createdBy = preferencesHelper.userName
                    product.createdOn = DateUtils.getCurrentDate()
                    product.lastModifiedBy = preferencesHelper.userName
                    product.lastModifiedOn = DateUtils.getCurrentDate()
                    synchronizationManager.updateDocument(product.identifier!!, product.serializeToMap())
                    _status.value = Status.DONE
                } catch (exception: Exception) {
                    _status.value = Status.ERROR
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    fun getCountries(): MutableLiveData<List<Country>> {
        val countries = MutableLiveData<List<Country>>()
        dataManagerAnonymous.countries.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                countries.value = it
            }
        return countries
    }

    fun getCountryNames(countries: List<Country>): List<String> {
        return Observable.fromIterable(countries).map { country: Country -> country.name }.toList().blockingGet()
    }

    fun getCountryCode(countries: List<Country>, countryName: String): String? {
        for (country in countries) {
            if (country.name == countryName) {
                return country.alphaCode
            }
        }
        return null
    }

    fun isCountryValid(countries: List<Country>, countryName: String): Boolean {
        for (country in countries) {
            if (country.name == countryName) {
                return true
            }
        }
        return false
    }

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        synchronizationManager.closeDatabase()
    }

}