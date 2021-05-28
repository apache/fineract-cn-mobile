package org.apache.fineract.data.services

import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import org.apache.fineract.data.models.Group
import org.apache.fineract.data.models.customer.Command
import retrofit2.http.*

/*
 * Created by saksham on 15/June/2019
*/

interface GroupsService {

    @GET("/groups")
    fun getGroups(): Observable<List<Group>>

    @POST("/groups")
    fun createGroup(@Body group: Group): Deferred<ResponseBody>

    @PUT("/groups/{identifier}")
    fun updateGroup(@Path("identifier") identifier: String,
                    @Body group: Group): Deferred<ResponseBody>

    @PUT("/groups/{identifier}/commands")
    fun changeGroupStatus(@Path("identifier") identifier: String,
                          @Body command: Command): Deferred<ResponseBody>
}