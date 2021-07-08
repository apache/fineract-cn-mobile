package org.apache.fineract.ui.online.teller.tellerlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.apache.fineract.couchbase.SynchronizationManager
import org.apache.fineract.data.datamanager.DataManagerTeller
import org.apache.fineract.data.datamanager.api.DataManagerAnonymous
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.injection.ApplicationContext
import javax.inject.Inject

/*
 * Created by Varun Jain on 14.06.2021
*/

class TellerViewModelFactory @Inject constructor(@ApplicationContext var context: Context,
                                                 private val synchronizationManager: SynchronizationManager,
                                                 private val dataManagerAnonymous: DataManagerAnonymous,
                                                 private val preferencesHelper: PreferencesHelper
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        return TellerViewModel(synchronizationManager, dataManagerAnonymous, preferencesHelper) as T
    }
}