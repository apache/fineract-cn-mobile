package org.apache.fineract.data.datamanager.api;

import org.apache.fineract.FakeRemoteDataSource;
import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.data.models.loan.LoanAccount;
import org.apache.fineract.data.models.loan.LoanAccountPage;
import org.apache.fineract.data.models.product.ProductPage;
import org.apache.fineract.data.remote.BaseApiManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author Rajan Maurya
 *         On 07/07/17.
 */
@Singleton
public class DataManagerLoans extends FineractBaseDataManager {

    private final BaseApiManager baseApiManager;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManagerLoans(BaseApiManager baseApiManager, PreferencesHelper preferencesHelper,
            DataManagerAuth dataManagerAuth) {
        super(dataManagerAuth, preferencesHelper);
        this.baseApiManager = baseApiManager;
        this.preferencesHelper = preferencesHelper;
    }

    public Observable<LoanAccountPage> fetchCustomerLoanAccounts(
            String customeridentifier, Integer pageIndex, Integer size) {
        return authenticatedObservableApi(baseApiManager.getLoanApi()
                .fetchCustomerLoanAccounts(customeridentifier, pageIndex, size))
                .onErrorResumeNext(
                        new Function<Throwable, ObservableSource<LoanAccountPage>>() {
                            @Override
                            public ObservableSource<LoanAccountPage> apply(
                                    Throwable throwable) throws Exception {
                                return Observable.just(FakeRemoteDataSource.getloanAccountPage());
                            }
                        });
    }

    public Observable<LoanAccount> fetchCustomerLoanDetails(
            String productIdentifier, String caseIdentifier) {
        return authenticatedObservableApi(baseApiManager.getLoanApi()
                .fetchCustomerLoanDetails(productIdentifier, caseIdentifier))
                .onErrorResumeNext(
                        new Function<Throwable, ObservableSource<LoanAccount>>() {
                            @Override
                            public ObservableSource<LoanAccount> apply(
                                    Throwable throwable) throws Exception {
                                return Observable.just(FakeRemoteDataSource.getloanAccount());
                            }
                        });
    }

    public Observable<ProductPage> getProducts(Integer pageIndex, Integer size) {
        return authenticatedObservableApi(baseApiManager.getLoanApi()
                .getProducts(pageIndex, size, false))
                .onErrorResumeNext(
                        new Function<Throwable, ObservableSource<? extends ProductPage>>() {
                            @Override
                            public ObservableSource<? extends ProductPage> apply(
                                    Throwable throwable) throws Exception {
                                return Observable.just(FakeRemoteDataSource.getProductPage());
                            }
                        });
    }

    public Completable createLoan(String productIdentifier, LoanAccount loanAccount) {
        return authenticatedCompletableApi(baseApiManager.getLoanApi()
                .createLoan(productIdentifier, loanAccount));
    }
}
