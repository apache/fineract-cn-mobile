package com.mifos.apache.fineract.data.datamanager;

import com.mifos.apache.fineract.data.local.PreferencesHelper;
import com.mifos.apache.fineract.data.models.deposit.CustomerDepositAccounts;
import com.mifos.apache.fineract.data.remote.BaseApiManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

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

    public Observable<List<CustomerDepositAccounts>> getCustomerDepositAccounts(
            String customerIdentifier) {
        return baseApiManager.getDepositApi().fetchCustomersDeposits(customerIdentifier);
    }

    public Observable<CustomerDepositAccounts> getCustomerDepositAccountDetails(
            String accountIdentifier) {
        return baseApiManager.getDepositApi().fetchCustomerDepositDetails(accountIdentifier);
    }
}
