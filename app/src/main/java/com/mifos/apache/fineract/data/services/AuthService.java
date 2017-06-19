package com.mifos.apache.fineract.data.services;


import com.mifos.apache.fineract.data.remote.EndPoints;
import com.mifos.apache.fineract.data.models.User;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Rajan Maurya
 *         On 16/03/17.
 */
public interface AuthService {

    @POST(EndPoints.API_IDENTITY_PATH + "/token?grant_type=password")
    Observable<User> login(
            @Query("username") String username,
            @Query("password") String password);
}
