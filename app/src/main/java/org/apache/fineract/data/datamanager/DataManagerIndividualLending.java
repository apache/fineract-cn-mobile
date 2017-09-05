package org.apache.fineract.data.datamanager;

import org.apache.fineract.data.local.PreferencesHelper;
import org.apache.fineract.data.models.payment.PlannedPaymentPage;
import org.apache.fineract.data.remote.BaseApiManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * @author Rajan Maurya
 *         On 13/07/17.
 */
@Singleton
public class DataManagerIndividualLending extends FineractBaseDataManager {

    private final BaseApiManager baseApiManager;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManagerIndividualLending(BaseApiManager baseApiManager,
            PreferencesHelper preferencesHelper, DataManagerAuth dataManagerAuth) {
        super(dataManagerAuth, preferencesHelper);
        this.baseApiManager = baseApiManager;
        this.preferencesHelper = preferencesHelper;
    }

    public Observable<PlannedPaymentPage> getPaymentScheduleForCase(String productIdentifier,
            String caseIdentifier, Integer pageIndex, Integer size, String initialDisbursalDate) {
        return authenticatedObservableApi(baseApiManager
                .getIndividualLendingService().getPaymentScheduleForCase(
                        productIdentifier, caseIdentifier, pageIndex, size, initialDisbursalDate));
    }
}
