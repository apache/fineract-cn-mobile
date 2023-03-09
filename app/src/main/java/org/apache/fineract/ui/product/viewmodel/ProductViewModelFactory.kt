package org.apache.fineract.ui.product.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.apache.fineract.couchbase.SynchronizationManager
import org.apache.fineract.data.datamanager.api.DataManagerAnonymous
import org.apache.fineract.data.datamanager.api.DataManagerProduct
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.injection.ApplicationContext
import javax.inject.Inject

/*
 * Created by Varun Jain on 11/August/2021
*/

class ProductViewModelFactory @Inject constructor(@ApplicationContext var context: Context,
                                                private val synchronizationManager: SynchronizationManager,
                                                private val dataManagerAnonymous: DataManagerAnonymous,
                                                private val preferencesHelper: PreferencesHelper,
                                                private val dataManagerProduct: DataManagerProduct
)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductViewModel(synchronizationManager, dataManagerAnonymous, preferencesHelper, dataManagerProduct) as T
    }
}