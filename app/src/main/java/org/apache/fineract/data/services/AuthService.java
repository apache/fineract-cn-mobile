package org.apache.fineract.data.services;


import org.apache.fineract.data.models.Authentication;
import org.apache.fineract.data.remote.EndPoints;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Rajan Maurya
 *         On 16/03/17.
 */
public interface AuthService {

    @POST(EndPoints.API_IDENTITY_PATH + "/token?grant_type=password")
    Observable<Authentication> login(
            @Query("username") String username,
            @Query("password") String password);

    @POST(EndPoints.API_IDENTITY_PATH + "/token?grant_type=refresh_token")
    Observable<Authentication> refreshToken();

}
