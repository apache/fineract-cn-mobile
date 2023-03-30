package org.apache.fineract.ui.online.teller.tellerlist

import android.annotation.SuppressLint
import android.util.Log.e
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
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.data.models.Group
import org.apache.fineract.data.models.customer.Country
import org.apache.fineract.data.models.teller.Teller
import org.apache.fineract.data.models.teller.TellerCommand
import org.apache.fineract.utils.DateUtils
import org.apache.fineract.utils.serializeToMap
import org.apache.fineract.utils.toDataClass
import java.lang.Exception

/*
 * Created by Varun Jain on 14.06.2021
*/

/**
 * Adding the SynchronizationManager as a constructor parameter so as to implement Couchbase afterwards
 */
class TellerViewModel(private val synchronizationManager: SynchronizationManager,
                      private val dataManagerAnonymous: DataManagerAnonymous,
                      private val preferencesHelper: PreferencesHelper
) : ViewModel() {

    var tellersList = MutableLiveData<ArrayList<Teller>>()
    private var viewModelJob = Job()

    // Create a Coroutine scope using a job to be able to cancel when needed
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.IO)
    private var _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    @SuppressLint("CheckResult")
    fun getTellers(): MutableLiveData<ArrayList<Teller>>? {
        val expression = Expression.property("documentType")
            .equalTo(Expression.string(DocumentType.TELLER.value))
            .and(Expression.property("state").notEqualTo(Expression.string("null")))
        val hashMapList = synchronizationManager.getDocuments(expression)
        if (hashMapList?.isEmpty() == null) {
            return null
        }
        val list = arrayListOf<Teller>()
        for (map in hashMapList) {
            list.add(map.toDataClass())
        }
        tellersList.value = list
        return tellersList
    }

    fun searchTeller(tellers: ArrayList<Teller>, query: String, searchedTeller: (ArrayList<Teller>) -> Unit) {
        searchedTeller(ArrayList(Observable.fromIterable(tellers).filter { teller -> teller.tellerAccountIdentifier?.toLowerCase()?.contains(query.toLowerCase()).toString().toBoolean() }.toList().blockingGet()))
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

    @SuppressLint("CheckResult")
    fun createTeller(teller: Teller) {
        uiScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _status.value = Status.LOADING
                    teller.createdBy = preferencesHelper.userName
                    teller.createdOn = DateUtils.getCurrentDate()
                    teller.lastModifiedBy = preferencesHelper.userName
                    teller.lastModifiedOn = DateUtils.getCurrentDate()
                    teller.lastOpenedBy = preferencesHelper.userName
                    teller.lastOpenedOn = DateUtils.getCurrentDate()
                    synchronizationManager.saveDocument(teller.tellerAccountIdentifier!!, teller.serializeToMap())
                    _status.value = Status.DONE
                } catch (exception: Exception) {
                    _status.value = Status.ERROR
                }
            }
        }
    }

    fun updateTeller(teller: Teller) {
        uiScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _status.value = Status.LOADING
                    teller.createdBy = preferencesHelper.userName
                    teller.createdOn = DateUtils.getCurrentDate()
                    teller.lastModifiedBy = preferencesHelper.userName
                    teller.lastModifiedOn = DateUtils.getCurrentDate()
                    teller.lastOpenedBy = preferencesHelper.userName
                    teller.lastOpenedOn = DateUtils.getCurrentDate()
                    synchronizationManager.updateDocument(teller.tellerAccountIdentifier!!, teller.serializeToMap())
                    _status.value = Status.DONE
                } catch (exception: Exception) {
                    _status.value = Status.ERROR
                }
            }
        }
    }

    fun changeTellerStatus(teller: Teller, command: TellerCommand) {
        uiScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _status.value = Status.LOADING
                    when (command.action) {
                        TellerCommand.TellerAction.ACTIVATE -> teller.state = Teller.State.ACTIVE
                        TellerCommand.TellerAction.CLOSE -> teller.state = Teller.State.CLOSED
                        TellerCommand.TellerAction.REOPEN -> teller.state = Teller.State.OPEN
                        else -> teller.state = Teller.State.PAUSED
                    }
                    synchronizationManager.updateDocument(teller.tellerAccountIdentifier!!, teller.serializeToMap())
                    _status.value = Status.DONE
                } catch (e: Exception) {
                    _status.value = Status.ERROR
                }
            }
        }
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