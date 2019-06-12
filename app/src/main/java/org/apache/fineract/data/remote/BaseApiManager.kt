package org.apache.fineract.data.remote

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.fineract.data.services.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Created by Ahmad Jawid Muhammadi on 1/6/20
 */

class BaseApiManager(context: Context) {

    companion object {
        lateinit var retrofit: Retrofit
    }

    lateinit var anonyMousRetrofit: Retrofit
    lateinit var authApi: AuthService
    lateinit var customerApi: CustomerService
    lateinit var depositApi: DepositService
    lateinit var loanApi: LoanService
    lateinit var individualLendingService: IndividualLendingService
    lateinit var anonymousService: AnonymousService
    lateinit var rolesService: RolesService
    lateinit var accountingService: AccountingService
    lateinit var tellerService: TellersService
    lateinit var productService: ProductService
    lateinit var payrollService: PayrollService

    init {
        createService(context)
        createAnonymousService()
    }

    private fun init() {
        authApi = createApi(AuthService::class.java)
        customerApi = createApi(CustomerService::class.java)
        depositApi = createApi(DepositService::class.java)
        loanApi = createApi(LoanService::class.java)
        individualLendingService = createApi(IndividualLendingService::class.java)
        rolesService = createApi(RolesService::class.java)
        accountingService = createApi(AccountingService::class.java)
        tellerService = createApi(TellersService::class.java)
        productService = createApi(ProductService::class.java)
        payrollService = createApi(PayrollService::class.java)
    }

    private fun initAnonymous() {
        anonymousService = anonyMousRetrofit!!.create(AnonymousService::class.java)
    }

    private fun <T> createApi(clazz: Class<T>): T {
        return retrofit!!.create(clazz)
    }

    private fun createService(context: Context) {
        retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl.getDefaultBaseUrl())
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(FineractOkHttpClient.getFineractOkHttpClient(context))
                .build()
        init()
    }

    private fun createAnonymousService() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
        anonyMousRetrofit = Retrofit.Builder()
                .baseUrl(BaseUrl.getDefaultBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        initAnonymous()
    }
}