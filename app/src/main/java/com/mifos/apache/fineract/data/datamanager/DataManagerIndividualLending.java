package com.mifos.apache.fineract.data.datamanager;

import com.mifos.apache.fineract.data.local.PreferencesHelper;
import com.mifos.apache.fineract.data.models.payment.PlannedPaymentPage;
import com.mifos.apache.fineract.data.remote.BaseApiManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * @author Rajan Maurya
 *         On 13/07/17.
 */
@Singleton
public class DataManagerIndividualLending {

    private final BaseApiManager baseApiManager;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManagerIndividualLending(BaseApiManager baseApiManager,
            PreferencesHelper preferencesHelper) {
        this.baseApiManager = baseApiManager;
        this.preferencesHelper = preferencesHelper;
    }

    public Observable<PlannedPaymentPage> getPaymentScheduleForCase(String productIdentifier,
            String caseIdentifier, Integer pageIndex, Integer size, String initialDisbursalDate) {
        return baseApiManager.getIndividualLendingService().getPaymentScheduleForCase(
                productIdentifier, caseIdentifier, pageIndex, size, initialDisbursalDate);
    }
}
