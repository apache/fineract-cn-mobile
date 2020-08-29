package org.apache.fineract.ui.online.groups.grouplist

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
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.data.models.Group
import org.apache.fineract.data.models.customer.Command
import org.apache.fineract.data.models.customer.Country
import org.apache.fineract.utils.DateUtils
import org.apache.fineract.utils.serializeToMap
import org.apache.fineract.utils.toDataClass

/*
 * Created by saksham on 15/June/2019
*/

class GroupViewModel constructor(private val synchronizationManager: SynchronizationManager,
                                 private val dataManagerAnonymous: DataManagerAnonymous,
                                 private val preferencesHelper: PreferencesHelper) : ViewModel() {

    var groupsList = MutableLiveData<ArrayList<Group>>()
    private var viewModelJob = Job()

    // Create a Coroutine scope using a job to be able to cancel when needed
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.IO)
    private var _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    fun getGroups(): MutableLiveData<ArrayList<Group>>? {
        val expression = Expression.property("documentType")
                .equalTo(Expression.string(DocumentType.GROUP.value))
                .and(Expression.property("status").notEqualTo(Expression.string("null")))
        val hashMapList = synchronizationManager.getDocuments(expression)
        if (hashMapList?.isEmpty() == null) {
            return null
        }
        val list = arrayListOf<Group>()
        for (map in hashMapList) {
            list.add(map.toDataClass())
        }
        groupsList.value = list
        return groupsList
    }

    fun searchGroup(groups: ArrayList<Group>, query: String, searchedGroup: (ArrayList<Group>) -> Unit) {
        searchedGroup(ArrayList(Observable.fromIterable(groups).filter { group -> group.identifier?.toLowerCase()?.contains(query.toLowerCase()).toString().toBoolean() }.toList().blockingGet()))
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
                    group.createdBy = preferencesHelper.userName
                    group.createdOn = DateUtils.getCurrentDate()
                    group.lastModifiedBy = preferencesHelper.userName
                    group.lastModifiedOn = DateUtils.getCurrentDate()
                    synchronizationManager.saveDocument(group.identifier!!, group.serializeToMap())
                    _status.value = Status.DONE
                } catch (e: Exception) {
                    _status.value = Status.ERROR
                }
            }
        }
    }

    fun updateGroup(group: Group) {
        uiScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _status.value = Status.LOADING
                    synchronizationManager.updateDocument(group.identifier!!, group.serializeToMap())
                    _status.value = Status.DONE
                } catch (e: Exception) {
                    _status.value = Status.ERROR
                }
            }
        }
    }

    fun changeGroupStatus(identifier: String, group: Group, command: Command) {
        uiScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _status.value = Status.LOADING
                    when (command.action) {
                        Command.Action.ACTIVATE -> group.status = Group.Status.ACTIVE
                        Command.Action.REOPEN -> group.status = Group.Status.PENDING
                        Command.Action.CLOSE -> group.status = Group.Status.CLOSED
                        Command.Action.LOCK -> group.status = Group.Status.ACTIVE
                        else -> group.status = Group.Status.PENDING
                    }
                    synchronizationManager.updateDocument(identifier, group.serializeToMap())
                    //   dataManagerGroups.changeGroupStatus(identifier, command).await()
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