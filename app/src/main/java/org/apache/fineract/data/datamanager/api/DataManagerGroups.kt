package org.apache.fineract.data.datamanager.api

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.data.models.Group
import org.apache.fineract.data.remote.BaseApiManager
import org.apache.fineract.FakeRemoteDataSource
import org.apache.fineract.data.datamanager.api.DataManagerAuth
import org.apache.fineract.data.datamanager.api.FineractBaseDataManager
import javax.inject.Inject
import javax.inject.Singleton


/*
 * Created by saksham on 15/June/2019
*/

class DataManagerGroups @Inject constructor(val baseManagerApi: BaseApiManager,
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
}