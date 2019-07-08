package org.apache.fineract.data.datamanager.api

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.data.models.Group
import org.apache.fineract.data.remote.BaseApiManager
import org.apache.fineract.FakeRemoteDataSource
import javax.inject.Inject


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

    fun createGroup(): MutableLiveData<Observable<ResponseBody>> {
        val response = MutableLiveData<Observable<ResponseBody>>()

        response.value = baseManagerApi.groupsService.createGroup()
                .onErrorResumeNext(Function<Throwable, ObservableSource<ResponseBody>> {
                    Observable.just(ResponseBody.create(MediaType.parse("text/plain"), "Successfully created Group"))
                })

        return response
    }
}