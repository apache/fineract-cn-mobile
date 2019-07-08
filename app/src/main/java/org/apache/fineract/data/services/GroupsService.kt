package org.apache.fineract.data.services

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.apache.fineract.data.models.Group
import retrofit2.http.GET
import retrofit2.http.POST

/*
 * Created by saksham on 15/June/2019
*/

interface GroupsService {

    @GET("/groups")
    fun getGroups(): Observable<List<Group>>

    @POST("/groups")
    fun createGroup(): Observable<ResponseBody>


}