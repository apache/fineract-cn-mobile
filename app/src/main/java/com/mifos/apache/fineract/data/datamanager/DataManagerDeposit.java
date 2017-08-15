package com.mifos.apache.fineract.data.datamanager;

import com.mifos.apache.fineract.data.local.PreferencesHelper;
import com.mifos.apache.fineract.data.models.deposit.DepositAccount;
import com.mifos.apache.fineract.data.models.deposit.ProductDefinition;
import com.mifos.apache.fineract.data.remote.BaseApiManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * @author Rajan Maurya
 *         On 07/07/17.
 */
@Singleton
public class DataManagerDeposit {

    private final BaseApiManager baseApiManager;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManagerDeposit(BaseApiManager baseApiManager, PreferencesHelper preferencesHelper) {
        this.baseApiManager = baseApiManager;
        this.preferencesHelper = preferencesHelper;
    }

    public Observable<List<DepositAccount>> getCustomerDepositAccounts(
            String customerIdentifier) {
        return baseApiManager.getDepositApi().fetchCustomersDeposits(customerIdentifier);
    }

    public Observable<DepositAccount> getCustomerDepositAccountDetails(
            String accountIdentifier) {
        return baseApiManager.getDepositApi().fetchCustomerDepositDetails(accountIdentifier);
    }

    public Observable<List<ProductDefinition>> fetchProductDefinitions() {
        return baseApiManager.getDepositApi().fetchProductDefinitions();
    }

    public Completable createDepositAccount(DepositAccount depositAccount) {
        return baseApiManager.getDepositApi().createDepositAccount(depositAccount);
    }

    public Completable updateDepositAccount(String accountIdentifier,
            DepositAccount depositAccount) {
        return baseApiManager.getDepositApi().updateDepositAccount(accountIdentifier,
                depositAccount);
    }
}
