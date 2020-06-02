package org.apache.fineract.data.services

import io.reactivex.Observable
import org.apache.fineract.data.models.Authentication
import org.apache.fineract.data.remote.EndPoints
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Ahmad Jawid Muhammadi on 2/6/20
 */
interface AuthService {

    @POST(EndPoints.API_IDENTITY_PATH + "/token?grant_type=password")
    fun login(
            @Query("username") username: String?,
            @Query("password") password: String?): Observable<Authentication?>?

    @POST(EndPoints.API_IDENTITY_PATH + "/token?grant_type=refresh_token")
    fun refreshToken(): Observable<Authentication?>?
}