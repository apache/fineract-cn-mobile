package org.apache.fineract.data.remote;

import android.content.Context;

import org.apache.fineract.data.services.AccountingService;
import org.apache.fineract.data.services.AnonymousService;
import org.apache.fineract.data.services.AuthService;
import org.apache.fineract.data.services.CustomerService;
import org.apache.fineract.data.services.DepositService;
import org.apache.fineract.data.services.IndividualLendingService;
import org.apache.fineract.data.services.LoanService;
import org.apache.fineract.data.services.PayrollService;
import org.apache.fineract.data.services.RolesService;
import org.apache.fineract.data.services.TellersService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * @author Rajan Maurya
 * @since 16/3/2017
 */
public class BaseApiManager {

    public static Retrofit retrofit;
    public static Retrofit anonyMousRetrofit;
    private static AuthService authApi;
    private static CustomerService customerApi;
    private static DepositService depositApi;
    private static LoanService loanApi;
    private static IndividualLendingService individualLendingService;
    private static AnonymousService anonymousService;
    private static RolesService rolesService;
    private static AccountingService accountingService;
    private static TellersService tellerService;
    private static PayrollService payrollService;

    public BaseApiManager(Context context) {
        createService(context);
        createAnonymousService();
    }

    private static void init() {
        authApi = createApi(AuthService.class);
        customerApi = createApi(CustomerService.class);
        depositApi = createApi(DepositService.class);
        loanApi = createApi(LoanService.class);
        individualLendingService = createApi(IndividualLendingService.class);
        rolesService = createApi(RolesService.class);
        accountingService = createApi(AccountingService.class);
        tellerService = createApi(TellersService.class);
        payrollService = createApi(PayrollService.class);
    }

    private static void initAnonymous() {
        anonymousService = anonyMousRetrofit.create(AnonymousService.class);
    }

    private static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    private static void createService(Context context) {

        retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.getDefaultBaseUrl())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new FineractOkHttpClient(context).getFineractOkHttpClient())
                .build();
        init();
    }

    private static void createAnonymousService() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        anonyMousRetrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.getDefaultBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        initAnonymous();
    }

    public AuthService getAuthApi() {
        return authApi;
    }

    public CustomerService getCustomerApi() {
        return customerApi;
    }

    public DepositService getDepositApi() {
        return depositApi;
    }

    public LoanService getLoanApi() {
        return loanApi;
    }

    public IndividualLendingService getIndividualLendingService() {
        return individualLendingService;
    }

    public AnonymousService getAnonymousService() {
        return anonymousService;
    }

    public RolesService getRolesAndPermissionsService() {
        return rolesService;
    }

    public  AccountingService getAccountingService() {
        return accountingService;
    }

    public TellersService getTellerService() {
        return tellerService;
    }

    public PayrollService getPayrollService() {
        return payrollService;
    }
}
