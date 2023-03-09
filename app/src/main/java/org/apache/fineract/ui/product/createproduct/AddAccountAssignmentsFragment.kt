package org.apache.fineract.ui.product.createproduct

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.fragment_step_add_account_assignments.*
import kotlinx.android.synthetic.main.fragment_step_add_account_assignments.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.loan.AccountAssignment
import org.apache.fineract.ui.adapters.ProductAccountAssignmentsAdapter
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.online.groups.GroupAction
import org.apache.fineract.ui.product.ProductAction
import org.apache.fineract.utils.Constants
import org.apache.fineract.utils.MaterialDialog
import org.apache.fineract.utils.Utils
import javax.inject.Inject

/*
 * Created by Varun Jain on 15th August 2021
 */

class AddAccountAssignmentsFragment : FineractBaseFragment(), Step,
    ProductAccountAssignmentsAdapter.OnItemClickListener {

    lateinit var rootView: View
    var accountAssignments: ArrayList<AccountAssignment> = ArrayList()
    private var currentAction = ProductAction.CREATE
    private var editItemPosition = 0
    private lateinit var productAction: ProductAction

    @Inject
    lateinit var productAccountAssignmentsAdapter: ProductAccountAssignmentsAdapter

    companion object {
        fun newInstance(productAction: ProductAction) = AddAccountAssignmentsFragment().apply {
            val bundle = Bundle().apply {
                putSerializable(Constants.PRODUCT_ACTION, productAction)
            }
            arguments = bundle
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getSerializable(Constants.PRODUCT_ACTION)?.let {
            productAction = it as ProductAction
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =
            inflater.inflate(R.layout.fragment_step_add_account_assignments, container, false)
        ButterKnife.bind(this, rootView)
        (activity as CreateProductActivity).activityComponent.inject(this)
        rootView.rvProductAccountAssignments.adapter = productAccountAssignmentsAdapter
        productAccountAssignmentsAdapter.setOnItemClickListener(this)
        if (productAction == ProductAction.EDIT) {
            showDataOnViews()
        }
        return rootView
    }

    private fun showDataOnViews() {
        val product = (activity as CreateProductActivity).getProduct()
        accountAssignments = product.accountAssignments as ArrayList<AccountAssignment>
        if (accountAssignments.size == 0) {
            showRecyclerView(false)
        } else {
            showRecyclerView(true)
        }
        productAccountAssignmentsAdapter.submitList(accountAssignments)
    }

    private fun showRecyclerView(showRecyclerView: Boolean) {
        if (showRecyclerView) {
            rootView.rvProductAccountAssignments.visibility = View.VISIBLE
            rootView.tvAddProductAccountAssignments.visibility = View.GONE
        } else {
            rootView.rvProductAccountAssignments.visibility = View.GONE
            rootView.tvAddProductAccountAssignments.visibility = View.VISIBLE
        }
    }


    override fun verifyStep(): VerificationError? {
        if (accountAssignments.size == 0) {
            Toast.makeText(
                context,
                getString(R.string.error_product_atleast_1_account_assignment),
                Toast.LENGTH_SHORT
            ).show()
            return VerificationError("")
        }
        (activity as CreateProductActivity).setAccountAssignments(accountAssignments)
        return null
    }

    override fun onSelected() {
    }

    override fun onError(p0: VerificationError) {
    }

    @Optional
    @OnClick(R.id.ibAddAccountAssignment)
    fun showAddLeaderView() {
        showAddAccountAssignmentsView(ProductAction.CREATE, null)
    }

    private fun showAddAccountAssignmentsView(
        action: ProductAction,
        accountAssignment: AccountAssignment?
    ) {
        currentAction = action
        llAddProductAccountAssignments.visibility = View.VISIBLE
        when (action) {
            ProductAction.EDIT -> {
                btnAddProductAccountAssignment.text = getString(R.string.update)
                etDesignator.setText(accountAssignment?.designator)
                etAccountIdentifier.setText(accountAssignment?.accountIdentifier)
                etLedgerIdentifier.setText(accountAssignment?.ledgerIdentifier)
            }
            ProductAction.CREATE -> {
                btnAddProductAccountAssignment.text = getString(R.string.add)
            }
        }
    }

    @Optional
    @OnClick(R.id.btnAddProductAccountAssignment)
    fun addAccountAssignment() {
        if ( validateAccountIdentifier() || validateDesignator() || validateLedgerIdentifier()) {
            if (currentAction == ProductAction.CREATE) {
                accountAssignments.add(
                    AccountAssignment(
                        etDesignator.text.toString(),
                        etAccountIdentifier.text.toString(),
                        etLedgerIdentifier.text.toString()
                    )
                )
            } else {
                accountAssignments[editItemPosition].designator = etDesignator.text.toString()
                accountAssignments[editItemPosition].accountIdentifier =
                    etAccountIdentifier.text.toString()
                accountAssignments[editItemPosition].ledgerIdentifier =
                    etLedgerIdentifier.text.toString()
            }
            etDesignator.text.clear()
            etAccountIdentifier.text.clear()
            etLedgerIdentifier.text.clear()
            llAddProductAccountAssignments.visibility = View.GONE
            Utils.hideKeyboard(context, etDesignator)
            Utils.hideKeyboard(context, etAccountIdentifier)
            Utils.hideKeyboard(context, etLedgerIdentifier)
            showRecyclerView(true)
            productAccountAssignmentsAdapter.submitList(accountAssignments)

        }
    }

    private fun validateDesignator() : Boolean {
        return etDesignator.validator()
            .nonEmpty().addErrorCallback { etDesignator.error = it }.check()
    }


    private fun validateAccountIdentifier() : Boolean {
        return etAccountIdentifier.validator()
            .nonEmpty().addErrorCallback { etAccountIdentifier.error = it }.check()
    }


    private fun validateLedgerIdentifier() : Boolean {
        return etLedgerIdentifier.validator()
            .nonEmpty().addErrorCallback { etLedgerIdentifier.error = it }.check()
    }

    @Optional
    @OnClick(R.id.btnCancelProductAccountAssignments)
    fun cancelProductAccountAssignmentAddition() {
        etLedgerIdentifier.text.clear()
        etDesignator.text.clear()
        etAccountIdentifier.text.clear()
        llAddProductAccountAssignments.visibility = View.GONE
    }

    override fun onEditClicked(position: Int) {
        editItemPosition = position
        showAddAccountAssignmentsView(ProductAction.EDIT, accountAssignments[position])
    }

    override fun onDeleteClicked(position: Int) {
        MaterialDialog.Builder().init(context).apply {
            setTitle(getString(R.string.dialog_title_confirm_deletion))
            setMessage(
                getString(
                    R.string.dialog_message_confirm_name_deletion,
                    accountAssignments[position].designator
                )
            )
            setPositiveButton(
                getString(R.string.delete)
            ) { dialog: DialogInterface?, _ ->
                accountAssignments.removeAt(position)
                productAccountAssignmentsAdapter.submitList(accountAssignments)
                if (accountAssignments.size == 0) {
                    showRecyclerView(false)
                }
                dialog?.dismiss()
            }
            setNegativeButton(getString(R.string.dialog_action_cancel))
            createMaterialDialog()
        }.run { show() }
    }
}