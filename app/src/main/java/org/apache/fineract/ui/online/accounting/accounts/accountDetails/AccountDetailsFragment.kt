package org.apache.fineract.ui.online.accounting.accounts.accountDetails

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_account_details.*
import kotlinx.android.synthetic.main.fragment_account_details.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.Account
import org.apache.fineract.ui.adapters.NameListAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.online.accounting.accounts.AccountAction
import org.apache.fineract.ui.online.accounting.accounts.accounttasks.AccountTaskBottomSheetFragment
import org.apache.fineract.ui.online.accounting.accounts.createaccount.CreateAccountActivity
import org.apache.fineract.ui.online.accounting.accounts.viewmodel.AccountsViewModel
import org.apache.fineract.ui.online.accounting.accounts.viewmodel.AccountsViewModelFactory
import org.apache.fineract.ui.views.CircularImageView
import org.apache.fineract.utils.Constants
import org.apache.fineract.utils.MaterialDialog
import org.apache.fineract.utils.Utils
import javax.inject.Inject


/*
 * Created by Varun Jain on 17/July/2021
*/

class AccountDetailsFragment : FineractBaseFragment() {

    lateinit var rootView: View
    lateinit var account: Account

    @Inject
    lateinit var holdersNameAdapter: NameListAdapter

    @Inject
    lateinit var signatureAuthoritiesNameAdapter: NameListAdapter

    private lateinit var viewModel: AccountsViewModel

    @Inject
    lateinit var viewModelFactory: AccountsViewModelFactory

    companion object {
        fun newInstance(account: Account) : AccountDetailsFragment{
            val fragment = AccountDetailsFragment()
            val args = Bundle()
            args.putParcelable(Constants.ACCOUNT, account)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        account = arguments?.get(Constants.ACCOUNT) as Account
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_account_details, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AccountsViewModel::class.java)
        ButterKnife.bind(this, rootView)
        rootView.rvHoldersAccount.adapter = holdersNameAdapter
        holdersNameAdapter.setReview(true)
        rootView.rvSignatureAuthoritiesAccount.adapter = signatureAuthoritiesNameAdapter
        signatureAuthoritiesNameAdapter.setReview(true)
        setToolbarTitle(account.identifier)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvIdentifierAccount.text = account.identifier
        tvAccountType.text = account.type.toString()
        setAccountStatusIcon(account.state, civStateAccount)
        account.holders?.let{
            holdersNameAdapter.submitList(it as ArrayList<String>)
        }
        account.signatureAuthorities?.let {
            signatureAuthoritiesNameAdapter.submitList(it as ArrayList<String>)
        }
        tvStateAccount.text = account.state.toString()
        tvNameAccount.text = account.name
        tvBalanceAccount.text = account.balance.toString()
        tvReferenceAmountAccount.text = account.referenceAccount
        tvLedgersAccount.text = account.ledger
        tvAltAccountNo.text = account.alternativeAccountNumber
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        Utils.setToolbarIconColor(context, menu, R.color.white)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_account_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuEditAccount -> {
                val intent = Intent(activity, CreateAccountActivity::class.java).apply {
                    putExtra(Constants.ACCOUNT, account)
                    putExtra(Constants.ACCOUNT_ACTION, AccountAction.EDIT)
                }
                startActivity(intent)
                activity!!.finish()
            }
            R.id.menuDeleteAccount -> {
                MaterialDialog.Builder().init(context).apply {
                    setTitle(getString(R.string.dialog_title_confirm_deletion))
                    setMessage(getString(R.string.dialog_message_confirm_name_deletion, account.name))
                    setPositiveButton(getString(R.string.delete)
                    ) { dialog: DialogInterface?, _ ->
                        viewModel.deleteAccount(account)
                        dialog?.dismiss()
                        activity!!.finish()
                    }
                    setNegativeButton(getString(R.string.dialog_action_cancel))
                    createMaterialDialog()
                }.run { show() }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @OnClick(R.id.cvTasksAccount)
    fun onTasksCardViewClicked() {
        val bottomSheet = AccountTaskBottomSheetFragment(account)
        bottomSheet.show(childFragmentManager, getString(R.string.tasks))
    }

    private fun setAccountStatusIcon(status: Account.State?, imageView: CircularImageView) {
        when (status) {
            Account.State.OPEN -> {
                imageView.setImageDrawable(Utils.setCircularBackground(R.color.blue, context))
            }
            Account.State.CLOSED -> {
                imageView.setImageDrawable(Utils.setCircularBackground(R.color.red_dark, context))
            }
            Account.State.LOCKED -> {
                imageView.setImageDrawable(Utils.setCircularBackground(R.color.light_yellow, context))
            }
        }
    }

}