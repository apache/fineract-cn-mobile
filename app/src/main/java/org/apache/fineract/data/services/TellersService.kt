package org.apache.fineract.data.services

import io.reactivex.Completable
import io.reactivex.Observable
import org.apache.fineract.data.models.teller.Teller
import org.apache.fineract.data.remote.EndPoints
import retrofit2.http.*

interface TellersService {

    @GET(EndPoints.API_TELLER_PATH + "/offices/{officeIdentifier}/teller")
    fun getTellerList(@Path("officeIdentifier") officeIdentifier: String): Observable<List<Teller>>

    @GET(EndPoints.API_TELLER_PATH + "/offices/{officeIdentifier}/teller/{tellerCode}")
    fun searchTeller(@Path("officeIdentifier") officeIdentifier: String,
                     @Path("tellerCode") tellerCode: String): Observable<Teller>

    @POST(EndPoints.API_TELLER_PATH + "/offices/{officeIdentifier}/teller")
    fun createTeller(@Path("officeIdentifier") officeIdentifier: String,
                    @Body teller: Teller): Completable

    @PUT(EndPoints.API_TELLER_PATH + "/offices/{officeIdentifier}/teller")
    fun updateTeller(@Path("officeIdentifier") officeIdentifier: String,
                    @Body teller: Teller): Completable
}