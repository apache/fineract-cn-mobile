package org.apache.fineract.data.services

import io.reactivex.Observable
import org.apache.fineract.data.models.Group
import retrofit2.Call
import retrofit2.http.GET

/*
 * Created by saksham on 15/June/2019
*/

interface GroupsService {

    @GET("/groups")
    fun getGroups(): Observable<List<Group>>


}