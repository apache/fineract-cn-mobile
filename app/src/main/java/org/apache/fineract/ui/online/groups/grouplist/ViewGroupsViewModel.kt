package org.apache.fineract.ui.online.groups.grouplist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.functions.Predicate
import org.apache.fineract.data.datamanager.api.DataManagerGroups
import org.apache.fineract.data.models.Group

/*
 * Created by saksham on 15/June/2019
*/

class ViewGroupsViewModel constructor(val dataManagerGroups: DataManagerGroups) : ViewModel() {

    lateinit var groupsList: MutableLiveData<ArrayList<Group>>


    fun getGroups(): MutableLiveData<ArrayList<Group>> {
        groupsList = dataManagerGroups.getGroups()
        return groupsList
    }

    fun searchGroup(groups: ArrayList<Group>, query: String, searchedGroup: (ArrayList<Group>) -> Unit) {
        searchedGroup(ArrayList(Observable.fromIterable(groups).filter(object : Predicate<Group> {
            override fun test(group: Group): Boolean {
                return group.identifier.toLowerCase().contains(query.toLowerCase()).toString().toBoolean()
            }
        }).toList().blockingGet()))
    }

}