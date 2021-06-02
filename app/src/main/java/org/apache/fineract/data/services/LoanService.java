package org.apache.fineract.data.services;

import org.apache.fineract.data.models.loan.LoanAccount;
import org.apache.fineract.data.models.loan.LoanAccountPage;
import org.apache.fineract.data.models.product.ProductPage;
import org.apache.fineract.data.remote.EndPoints;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Rajan Maurya
 * On 07/07/17.
 */
public interface LoanService {

    @GET(EndPoints.API_PORTFOLIO_PATH + "/individuallending/customers/{customeridentifier}/cases")
    Observable<LoanAccountPage> fetchCustomerLoanAccounts(
            @Path("customeridentifier") String customerIdentifier,
            @Query("pageIndex") Integer pageIndex,
            @Query("size") Integer size);

    @GET(EndPoints.API_PORTFOLIO_PATH + "/products/{productidentifier}/cases/{caseidentifier}")
    Observable<LoanAccount> fetchCustomerLoanDetails(
            @Path("productidentifier") String productIdentifier,
            @Path("caseidentifier") String caseIdentifier);

    @GET(EndPoints.API_PORTFOLIO_PATH + "/products/")
    Observable<ProductPage> getProducts(
            @Query("pageIndex") Integer pageIndex,
            @Query("size") Integer size,
            @Query("includeDisabled") Boolean includeDisabled);

    @POST(EndPoints.API_PORTFOLIO_PATH + "/products/{productidentifier}/cases/")
    Completable createLoan(
            @Path("productidentifier") String productidentifier,
            @Body LoanAccount loanAccount);

    @PUT(EndPoints.API_PORTFOLIO_PATH + "/products/{productidentifier}/cases/{caseidentifier}")
    Completable updateLoan(
            @Path("productidentifier") String productIdentifier,
            @Path("caseidentifier") String caseIdentifier,
            @Body LoanAccount loanAccount);
}
