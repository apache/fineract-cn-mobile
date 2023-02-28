package org.apache.fineract.ui.online.groups.grouplist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.apache.fineract.data.datamanager.api.DataManagerAnonymous
import org.apache.fineract.data.datamanager.api.DataManagerGroups
import org.apache.fineract.couchbase.SynchronizationManager
import org.apache.fineract.data.datamanager.api.DataManagerAnonymous
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.injection.ApplicationContext
import javax.inject.Inject


/*
 * Created by saksham on 16/June/2019
*/

class GroupViewModelFactory @Inject constructor(@ApplicationContext var context: Context,
                                                private val synchronizationManager: SynchronizationManager,
                                                private val dataManagerAnonymous: DataManagerAnonymous,
                                                private val preferencesHelper: PreferencesHelper)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GroupViewModel(synchronizationManager, dataManagerAnonymous, preferencesHelper) as T
    }
}