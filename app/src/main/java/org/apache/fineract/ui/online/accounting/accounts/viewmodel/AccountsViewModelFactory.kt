package org.apache.fineract.ui.online.accounting.accounts.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.apache.fineract.couchbase.SynchronizationManager
import org.apache.fineract.data.datamanager.DataManagerAccounting
import org.apache.fineract.data.datamanager.api.DataManagerAnonymous
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.injection.ApplicationContext
import javax.inject.Inject


/*
 * Created by Varun Jain on 18/July/2021
*/


class AccountsViewModelFactory @Inject constructor(@ApplicationContext var context: Context,
                                                private val synchronizationManager: SynchronizationManager,
                                                private val dataManagerAnonymous: DataManagerAnonymous,
                                                private val dataManagerAccounting: DataManagerAccounting,
                                                private val preferencesHelper: PreferencesHelper
)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AccountsViewModel(synchronizationManager, dataManagerAnonymous, preferencesHelper, dataManagerAccounting) as T
    }
}