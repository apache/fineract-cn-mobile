package org.apache.fineract.data.services

import io.reactivex.Observable
import org.apache.fineract.data.models.rolesandpermission.Role
import org.apache.fineract.data.remote.EndPoints
import retrofit2.http.GET

/**
 * Created by Ahmad Jawid Muhammadi on 2/6/20
 */
interface RolesService {
    @GET(EndPoints.API_IDENTITY_PATH + "/roles")
    fun getRoles(): Observable<List<Role?>?>?
}