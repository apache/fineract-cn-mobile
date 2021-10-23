package org.apache.fineract.ui.online.accounting.accounts.createaccount

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import com.stepstone.stepper.Step
import org.apache.fineract.ui.adapters.NameListAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.stepstone.stepper.VerificationError
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.fragment_step_account_review.view.*
import kotlinx.android.synthetic.main.fragment_step_add_account_holder.*
import kotlinx.android.synthetic.main.fragment_step_add_account_holder.view.*
import org.apache.fineract.R
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.online.accounting.accounts.AccountAction
import org.apache.fineract.utils.Constants
import org.apache.fineract.utils.MaterialDialog
import org.apache.fineract.utils.Utils
import javax.inject.Inject

/*
 * Created by Varun Jain on 21/July/2021
*/

class AddAccountHoldersStepFragment : FineractBaseFragment(), Step, NameListAdapter.OnItemClickListener {

    lateinit var rootView: View
    var holders: ArrayList<String> = ArrayList()
    private var currentAccountAction = AccountAction.CREATE
    private var editItemPosition = 0
    private lateinit var accountAction: AccountAction

    @Inject
    lateinit var nameListAdapter: NameListAdapter

    companion object {
        fun newInstance (accountAction: AccountAction) =
            AddAccountHoldersStepFragment().apply {
                val bundle = Bundle().apply {
                    putSerializable(Constants.ACCOUNT_ACTION, accountAction)
                }
                arguments = bundle
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getSerializable(Constants.ACCOUNT_ACTION)?.let {
            accountAction = it as AccountAction
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_step_add_account_holder, container, false)
        ButterKnife.bind(this, rootView)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        rootView.rvHolderName.adapter = nameListAdapter
        nameListAdapter.setOnItemClickListener(this)
        if (accountAction == AccountAction.EDIT) {
            populateViews()
        }
        return rootView
    }

    private fun populateViews() {
        val account = (activity as CreateAccountActivity).getAccount()
        holders = account.holders as ArrayList<String>
        if (holders.size == 0) {
            showRecyclerView(false)
        } else {
            showRecyclerView(true)
        }
        nameListAdapter.submitList(holders)
    }

    @Optional
    @OnClick(R.id.ivAddHolder)
    fun showAddHolderView() {
        showAddHolderView(AccountAction.CREATE, null)
    }

    private fun showAddHolderView(action: AccountAction, name: String?) {
        currentAccountAction = action
        llAddHolderAccountStep.visibility = View.VISIBLE
        when (action) {
            AccountAction.CREATE -> {
                btnAddHolder.text = getString(R.string.add)
            }
            AccountAction.EDIT -> {
                etNewHolder.setText(name)
                btnAddHolder.text = getString(R.string.update)
            }
        }
    }

    @Optional
    @OnClick(R.id.btnAddHolder)
    fun addHolder() {
        if (etNewHolder.validator()
                .nonEmpty()
                .addErrorCallback { etNewHolder.error = it }.check()) {
            if (currentAccountAction == AccountAction.CREATE) {
                holders.add(etNewHolder.text.toString().trim { it <= ' ' })
            } else {
                holders[editItemPosition] = etNewHolder.text.toString().trim { it <= ' ' }
            }
            etNewHolder.text.clear()
            llAddHolderAccountStep.visibility = View.GONE
            Utils.hideKeyboard(context, etNewHolder)
            showRecyclerView(true)
            nameListAdapter.submitList(holders)
        }
    }

    fun showRecyclerView(isShow: Boolean) {
        if (isShow) {
            rootView.rvHolderName.visibility = View.VISIBLE
            rootView.tvAddedHolder.visibility = View.GONE
        } else {
            rootView.rvHolderName.visibility = View.GONE
            rootView.tvAddedHolder.visibility = View.VISIBLE
        }
    }

    @Optional
    @OnClick(R.id.btnCancelAddHolder)
    fun cancelHolderAddition() {
        etNewHolder.text.clear()
        llAddHolderAccountStep.visibility = View.GONE
    }

    override fun verifyStep(): VerificationError? {
        if (holders.size == 0) {
            Toast.makeText(context, getString(R.string.error_acc_atleast_1_holder), Toast.LENGTH_SHORT).show()
            return VerificationError("")
        }
        (activity as CreateAccountActivity).setHolders(holders)
        return null
    }

    override fun onSelected() {}

    override fun onError(p0: VerificationError) {}

    override fun onEditClicked(position: Int) {
        editItemPosition = position
        showAddHolderView(AccountAction.EDIT, holders[position])
    }

    override fun onDeleteClicked(position: Int) {
        MaterialDialog.Builder().init(context).apply {
            setTitle(getString(R.string.dialog_title_confirm_deletion))
            setMessage(getString(R.string.dialog_message_confirm_name_deletion, holders[position]))
            setPositiveButton(getString(R.string.delete)
            ) { dialog: DialogInterface?, _ ->
                holders.removeAt(position)
                nameListAdapter.submitList(holders)
                if (holders.size == 0) {
                    showRecyclerView(false)
                }
                dialog?.dismiss()
            }
            setNegativeButton(getString(R.string.dialog_action_cancel))
            createMaterialDialog()
        }.run { show() }
    }
}