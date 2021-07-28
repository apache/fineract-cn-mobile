package org.apache.fineract.ui.online.accounting.accounts.createaccount

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_create_group.*
import org.apache.fineract.R
import org.apache.fineract.data.Status
import org.apache.fineract.data.models.accounts.Account
import org.apache.fineract.data.models.accounts.AccountType
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.Toaster
import org.apache.fineract.ui.online.accounting.accounts.AccountAction
import org.apache.fineract.ui.online.accounting.accounts.viewmodel.AccountsViewModel
import org.apache.fineract.ui.online.accounting.accounts.viewmodel.AccountsViewModelFactory
import org.apache.fineract.utils.Constants
import javax.inject.Inject

/*
 * Created by Varun Jain on 20/July/2021
*/

class CreateAccountActivity : FineractBaseActivity(), StepperLayout.StepperListener {

    private var account = Account()
    private var accountAction = AccountAction.CREATE


    @Inject
    lateinit var accountsViewModelFactory: AccountsViewModelFactory

    lateinit var accountsViewModel: AccountsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        activityComponent.inject(this)
        accountAction = intent.getSerializableExtra(Constants.ACCOUNT_ACTION) as AccountAction
        when (accountAction) {
            AccountAction.CREATE -> {
                setToolbarTitle(getString(R.string.create_account))
            }
            AccountAction.EDIT -> {
                setToolbarTitle(getString(R.string.edit_account))
                intent?.extras?.getParcelable<Account>(Constants.ACCOUNT)?.let {
                    account = it
                }
            }
        }
        accountsViewModel =
            ViewModelProviders.of(this, accountsViewModelFactory).get(AccountsViewModel::class.java)
        subscribeUI()
        showBackButton()
        slCreateAccount.adapter = CreateAccountAdapter(supportFragmentManager, this, accountAction)
        slCreateAccount.setOffscreenPageLimit(slCreateAccount.adapter.count)
        slCreateAccount.setListener(this)
    }

    override fun onCompleted(p0: View?) {
        when (accountAction) {
            AccountAction.CREATE -> account.identifier?.let {
                accountsViewModel.createAccount(account)
            }
            AccountAction.EDIT -> account.identifier?.let {
                accountsViewModel.updateAccount(account)
            }
        }
    }

    override fun onError(verificationError: VerificationError?) {}

    override fun onStepSelected(newStepPosition: Int) {}

    override fun onReturn() {}


    private fun subscribeUI() {
        accountsViewModel.status.observe(this, Observer { status ->
            when (status) {
                Status.LOADING ->
                    if (accountAction == AccountAction.CREATE) {
                        showMifosProgressDialog(getString(R.string.creating_account_please_wait))
                    } else {
                        showMifosProgressDialog(getString(R.string.updating_account_please_wait))
                    }
                Status.ERROR -> {
                    hideMifosProgressDialog()
                    if (accountAction == AccountAction.CREATE) {
                        Toaster.show(
                            findViewById(android.R.id.content),
                            R.string.error_while_creating_account,
                            Toast.LENGTH_SHORT
                        )
                    } else {
                        Toaster.show(
                            findViewById(android.R.id.content),
                            R.string.error_while_updating_account,
                            Toast.LENGTH_SHORT
                        )
                    }
                }
                Status.DONE -> {
                    hideMifosProgressDialog()
                    if (accountAction == AccountAction.CREATE) {
                        Toast.makeText(
                            this,
                            getString(
                                R.string.account_identifier_created_successfully,
                                account.identifier
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            getString(
                                R.string.account_identifier_updated_successfully,
                                account.identifier
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    finish()
                }
            }
        })
    }

    fun setHolders(holders: List<String>) {
        account.holders = holders
    }

    fun setSignatureAuthorities(setSignatureAuthorities: List<String>) {
        account.signatureAuthorities = setSignatureAuthorities
    }

    fun setAccountDetails(
        identifier: String,
        balance: Double,
        type: AccountType,
        alternativeAccountNum: String,
        name: String,
        ledger: String,
        refAccount: String
    ) {
        account.identifier = identifier
        account.balance = balance
        account.type = type
        account.alternativeAccountNumber = alternativeAccountNum
        account.name = name
        account.ledger = ledger
        account.referenceAccount = refAccount
    }

    fun getAccount(): Account {
        return account
    }
}