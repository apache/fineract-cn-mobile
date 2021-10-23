package org.apache.fineract.ui.online.accounting.accounts.createaccount

import android.content.Context
import android.content.DialogInterface
import android.opengl.Visibility
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.fragment_step_account_review.view.*
import kotlinx.android.synthetic.main.fragment_step_add_account_holder.*
import kotlinx.android.synthetic.main.fragment_step_add_group_member.view.*
import kotlinx.android.synthetic.main.fragment_step_add_signature_authorities.*
import kotlinx.android.synthetic.main.fragment_step_add_signature_authorities.view.*
import org.apache.fineract.R
import org.apache.fineract.ui.adapters.NameListAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.online.accounting.accounts.AccountAction
import org.apache.fineract.ui.online.groups.GroupAction
import org.apache.fineract.utils.Constants
import org.apache.fineract.utils.MaterialDialog
import org.apache.fineract.utils.Utils
import javax.inject.Inject


/*
 * Created by Varun Jain on 21/July/2021
*/

class AddAccountSignAuthoritiesFragment : FineractBaseFragment(), Step, NameListAdapter.OnItemClickListener {

    lateinit var rootView: View
    var signatureAuthorities: ArrayList<String> = ArrayList()
    private var currentAccountAction = AccountAction.CREATE
    private var editItemPosition = 0
    private lateinit var accountAction: AccountAction

    @Inject
    lateinit var  namesListAdapter: NameListAdapter

    companion object {
        fun newInstance (accountAction: AccountAction) =
            AddAccountSignAuthoritiesFragment().apply {
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
        rootView = inflater.inflate(R.layout.fragment_step_add_signature_authorities, container, false)
        ButterKnife.bind(this, rootView)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        rootView.rvAddSignatureAuthoritiesAccountStep.adapter = namesListAdapter
        namesListAdapter.setOnItemClickListener(this)
        if (accountAction == AccountAction.EDIT) {
            showDataOnViews()
        }
        return rootView
    }

    private fun showDataOnViews() {
        val account = (activity as CreateAccountActivity).getAccount()
        signatureAuthorities = account.signatureAuthorities as ArrayList<String>
        if (signatureAuthorities.size == 0) {
            showRecyclerView(false)
        } else {
            showRecyclerView(true)
        }
        namesListAdapter.submitList(signatureAuthorities)
    }

    @Optional
    @OnClick(R.id.ivAddSignatureAuthorities)
    fun showAddSignatureAuthoritiesView() {
        showAddSignatureAuthoritiesView(AccountAction.CREATE, null)
    }

    private fun showAddSignatureAuthoritiesView(action: AccountAction, name: String?) {
        currentAccountAction = action
        llAddSignatureAuthoritiesAccountStep.visibility = View.VISIBLE
        when (action) {
            AccountAction.CREATE -> {
                btnAddSignatureAuthority.text = getString(R.string.add)
            }
            AccountAction.EDIT -> {
                etNewSignatureAuthorities.setText(name)
                btnAddSignatureAuthority.text = getString(R.string.update)
            }
        }
    }

    @Optional
    @OnClick(R.id.btnAddSignatureAuthority)
    fun addSignatureAuthority() {
        if (etNewSignatureAuthorities.validator()
                .nonEmpty()
                .addErrorCallback { etNewSignatureAuthorities.error = it }.check()) {
            if (currentAccountAction == AccountAction.CREATE) {
                signatureAuthorities.add(etNewSignatureAuthorities.text.toString().trim { it <= ' ' })
            } else {
                signatureAuthorities[editItemPosition] = etNewSignatureAuthorities.text.toString().trim { it <= ' ' }
            }
            etNewSignatureAuthorities.text.clear()
            llAddSignatureAuthoritiesAccountStep.visibility = View.GONE
            Utils.hideKeyboard(context, etNewSignatureAuthorities)
            showRecyclerView(true)
            namesListAdapter.submitList(signatureAuthorities)
        }
    }


    private fun showRecyclerView(show: Boolean) {
        if (show) {
            rootView.rvAddSignatureAuthoritiesAccountStep.visibility = View.VISIBLE
            rootView.tvAddedSignatureAuthorities.visibility = View.GONE
        } else {
            rootView.rvAddSignatureAuthoritiesAccountStep.visibility = View.GONE
            rootView.tvAddedSignatureAuthorities.visibility = View.VISIBLE
        }
    }

    @Optional
    @OnClick(R.id.btnCancelAddSignatureAuthority)
    fun cancelSignatureAuthorityAddition() {
        etNewSignatureAuthorities.text.clear()
        llAddSignatureAuthoritiesAccountStep.visibility = View.GONE
    }

    override fun verifyStep(): VerificationError? {
        if (signatureAuthorities.size == 0) {
            Toast.makeText(context, getString(R.string.error_acc_atleast_1_signature_authority), Toast.LENGTH_SHORT).show()
            return VerificationError("")
        }
        (activity as CreateAccountActivity).setSignatureAuthorities(signatureAuthorities)
        return null
    }

    override fun onSelected() {}

    override fun onError(p0: VerificationError) {}

    override fun onEditClicked(position: Int) {
        editItemPosition = position
        showAddSignatureAuthoritiesView(AccountAction.EDIT, signatureAuthorities[position])
    }

    override fun onDeleteClicked(position: Int) {
        MaterialDialog.Builder().init(context).apply {
            setTitle(getString(R.string.dialog_title_confirm_deletion))
            setMessage(getString(R.string.dialog_message_confirm_name_deletion, signatureAuthorities[position]))
            setPositiveButton(getString(R.string.delete)
            ) { dialog: DialogInterface?, _ ->
                signatureAuthorities.removeAt(position)
                namesListAdapter.submitList(signatureAuthorities)
                if (signatureAuthorities.size == 0) {
                    showRecyclerView(false)
                }
                dialog?.dismiss()
            }
            setNegativeButton(getString(R.string.dialog_action_cancel))
            createMaterialDialog()
        }.run { show() }
    }
}