package org.apache.fineract.ui.online.groups.grouplist

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import org.apache.fineract.data.Status
import org.apache.fineract.data.datamanager.api.DataManagerAnonymous
import org.apache.fineract.data.datamanager.api.DataManagerGroups
import org.apache.fineract.data.models.Group
import org.apache.fineract.data.models.customer.Command
import org.apache.fineract.data.models.customer.Country

/*
 * Created by saksham on 15/June/2019
*/

class GroupViewModel constructor(val dataManagerGroups: DataManagerGroups, val dataManagerAnonymous: DataManagerAnonymous) : ViewModel() {

    lateinit var groupsList: MutableLiveData<ArrayList<Group>>
    private var viewModelJob = Job()
    // Create a Coroutine scope using a job to be able to cancel when needed
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.IO)
    private var _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    fun getGroups(): MutableLiveData<ArrayList<Group>> {
        groupsList = dataManagerGroups.getGroups()
        return groupsList
    }

    fun searchGroup(groups: ArrayList<Group>, query: String, searchedGroup: (ArrayList<Group>) -> Unit) {
        searchedGroup(ArrayList(Observable.fromIterable(groups).filter(object : Predicate<Group> {
            override fun test(group: Group): Boolean {
                return group.identifier?.toLowerCase()?.contains(query.toLowerCase()).toString().toBoolean()
            }
        }).toList().blockingGet()))
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

    fun createGroup(group: Group) {
        uiScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _status.value = Status.LOADING
                    dataManagerGroups.createGroup(group).await()
                    _status.value = Status.DONE
                } catch (e: Exception) {
                    _status.value = Status.ERROR
                }
            }
        }
    }

    fun updateGroup(identifier: String, group: Group) {
        uiScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _status.value = Status.LOADING
                    dataManagerGroups.updateGroup(identifier, group).await()
                    _status.value = Status.DONE
                } catch (e: Exception) {
                    _status.value = Status.ERROR
                }
            }
        }
    }

    fun changeGroupStatus(identifier: String, command: Command) {
        uiScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _status.value = Status.LOADING
                    dataManagerGroups.changeGroupStatus(identifier, command).await()
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
            if (country.name.equals(countryName)) {
                return country.alphaCode
            }
        }
        return null
    }

    fun isCountryValid(countries: List<Country>, countryName: String): Boolean {
        for (country in countries) {
            if (country.name.equals(countryName)) {
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
    }

}