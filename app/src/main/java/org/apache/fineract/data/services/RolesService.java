package org.apache.fineract.data.services;

import org.apache.fineract.data.models.rolesandpermission.Role;
import org.apache.fineract.data.remote.EndPoints;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * @author Rajan Maurya
 * On 24/08/17.
 */
public interface RolesService {

    @GET(EndPoints.API_IDENTITY_PATH + "/roles")
    Observable<List<Role>> getRoles();

    @POST(EndPoints.API_IDENTITY_PATH + "/roles")
    Completable createRole(@Body Role role);

    @DELETE(EndPoints.API_IDENTITY_PATH + "/roles/{identifier}")
    Completable deleteRole(@Path("identifier") String identifier);

    @PUT(EndPoints.API_ACCOUNTING_PATH + "/roles/{identifier}")
    Completable updateRole(@Path("identifier") String identifier,
                           @Body Role role);
}
