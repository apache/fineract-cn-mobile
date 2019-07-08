package org.apache.fineract.ui.online.groups.grouplist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.apache.fineract.data.datamanager.api.DataManagerAnonymous
import org.apache.fineract.data.datamanager.api.DataManagerGroups
import org.apache.fineract.data.models.Group
import org.apache.fineract.data.models.customer.Country

/*
 * Created by saksham on 15/June/2019
*/

class GroupsViewModel constructor(val dataManagerGroups: DataManagerGroups, val dataManagerAnonymous: DataManagerAnonymous) : ViewModel() {

    lateinit var groupsList: MutableLiveData<ArrayList<Group>>


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

    fun getCountries(): MutableLiveData<List<Country>> {
        val countries = MutableLiveData<List<Country>>()
        dataManagerAnonymous.countries.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    countries.value = it
                }
        return countries
    }

    fun createGroup(): MutableLiveData<Observable<ResponseBody>> {
        return dataManagerGroups.createGroup()
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

}