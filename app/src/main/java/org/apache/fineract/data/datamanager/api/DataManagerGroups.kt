package org.apache.fineract.data.datamanager.api

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import org.apache.fineract.FakeRemoteDataSource
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.data.models.Group
import org.apache.fineract.data.models.customer.Command
import org.apache.fineract.data.remote.BaseApiManager
import javax.inject.Inject


/*
 * Created by saksham on 15/June/2019
*/

class DataManagerGroups @Inject constructor(private val baseManagerApi: BaseApiManager,
                                            val dataManagerAuth: DataManagerAuth,
                                            val preferencesHelper: PreferencesHelper)
    : FineractBaseDataManager(dataManagerAuth, preferencesHelper) {

    fun getGroups(): MutableLiveData<ArrayList<Group>> {
        val groups = MutableLiveData<ArrayList<Group>>()

        groups.value = ArrayList(baseManagerApi.groupsService.getGroups()
                .onErrorResumeNext(Function<Throwable, ObservableSource<List<Group>>> {
                    Observable.just(FakeRemoteDataSource.getGroups())
                }).blockingFirst())
        return groups
    }

    fun createGroup(group: Group): Deferred<ResponseBody> = baseManagerApi.groupsService.createGroup(group)

    fun updateGroup(identifier: String, group: Group): Deferred<ResponseBody> {
        return baseManagerApi.groupsService.updateGroup(identifier, group)
    }

    fun changeGroupStatus(identifier: String, command: Command): Deferred<ResponseBody> {
        return baseManagerApi.groupsService.changeGroupStatus(identifier, command)
    }
}