package org.apache.fineract.ui.online.accounting.accounts.viewmodel

import org.apache.fineract.data.models.accounts.Account
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.couchbase.lite.Expression
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import org.apache.fineract.couchbase.DocumentType
import org.apache.fineract.couchbase.SynchronizationManager
import org.apache.fineract.data.Status
import org.apache.fineract.data.datamanager.DataManagerAccounting
import org.apache.fineract.data.datamanager.api.DataManagerAnonymous
import org.apache.fineract.data.local.PreferencesHelper
import org.apache.fineract.data.models.Group
import org.apache.fineract.data.models.customer.Command
import org.apache.fineract.ui.online.accounting.accounts.accounttasks.AccountCommand
import org.apache.fineract.utils.DateUtils
import org.apache.fineract.utils.serializeToMap
import org.apache.fineract.utils.toDataClass

/*
 * Created by Varun Jain on 18/July/2021
*/

class AccountsViewModel constructor(private val synchronizationManager: SynchronizationManager,
                                 private val dataManagerAnonymous: DataManagerAnonymous,
                                 private val preferencesHelper: PreferencesHelper,
                                 private val dataManagerAccounting: DataManagerAccounting
) : ViewModel() {

    var accountsList = MutableLiveData<ArrayList<Account>>()
    private var viewModelJob = Job()

    // Create a Coroutine scope using a job to be able to cancel when needed
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.IO)
    private var _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    @SuppressLint("CheckResult")
    fun getAccounts(): MutableLiveData<ArrayList<Account>>? {
        val expression = Expression.property("documentType")
            .equalTo(Expression.string(DocumentType.ACCOUNT.value))
            .and(Expression.property("state").notEqualTo(Expression.string("null")))
        val hashMapList = synchronizationManager.getDocuments(expression)
        if (hashMapList?.isEmpty() == null) {
            return null
        }
        val list = arrayListOf<Account>()
        for (map in hashMapList) {
            list.add(map.toDataClass())
        }
        accountsList.value = list

        return accountsList
    }

    fun searchAccount(
        accounts: ArrayList<Account>,
        query: String,
        searchedAccount: (ArrayList<Account>) -> Unit
    ) {
        searchedAccount(
            ArrayList(
                Observable.fromIterable(accounts).filter { account ->
                    account.identifier?.toLowerCase()?.contains(query.toLowerCase()).toString()
                        .toBoolean()
                }.toList().blockingGet()
            )
        )
    }

    @SuppressLint("CheckResult")
    fun createAccount(account: Account) {
        uiScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _status.value = Status.LOADING
                    account.createdBy = preferencesHelper.userName
                    account.createdOn = DateUtils.getCurrentDate()
                    account.lastModifiedBy = preferencesHelper.userName
                    account.lastModifiedOn = DateUtils.getCurrentDate()
                    account.state = Account.State.OPEN
                    synchronizationManager.saveDocument(account.identifier!!, account.serializeToMap())
                    _status.value = Status.DONE
                } catch (exception: Exception) {
                    _status.value = Status.ERROR
                }
            }
        }
    }

    fun updateAccount(account: Account) {
        uiScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _status.value = Status.LOADING
                    account.createdBy = preferencesHelper.userName
                    account.createdOn = DateUtils.getCurrentDate()
                    account.lastModifiedBy = preferencesHelper.userName
                    account.lastModifiedOn = DateUtils.getCurrentDate()
                    account.identifier?.let {
                        synchronizationManager.updateDocument(account.identifier!!, account.serializeToMap())
                        _status.value = Status.DONE
                    }
                } catch (exception: Exception) {
                    _status.value = Status.ERROR
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    fun deleteAccount(account: Account) {
        uiScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _status.value = Status.LOADING
                    account.lastModifiedBy = preferencesHelper.userName
                    account.lastModifiedOn = DateUtils.getCurrentDate()
                    account.state = Account.State.CLOSED
                    synchronizationManager.deleteDocument(account.identifier!!)
                    _status.value = Status.DONE
                } catch (exception: Exception) {
                    _status.value = Status.ERROR
                }
            }
        }
    }

    fun changeAccountStatus(identifier: String, account: Account, command: AccountCommand) {
        uiScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _status.value = Status.LOADING
                    when (command.action) {
                        AccountCommand.AccountTaskAction.UNLOCK -> account.state = Account.State.OPEN
                        AccountCommand.AccountTaskAction.REOPEN -> account.state = Account.State.OPEN
                        AccountCommand.AccountTaskAction.LOCK -> account.state = Account.State.LOCKED
                        AccountCommand.AccountTaskAction.CLOSE -> account.state = Account.State.CLOSED
                        else -> account.state = Account.State.OPEN
                    }
                    synchronizationManager.updateDocument(identifier, account.serializeToMap())
                    _status.value = Status.DONE
                } catch (e: Exception) {
                    _status.value = Status.ERROR
                }
            }
        }
    }

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        synchronizationManager.closeDatabase()
    }
}