package org.apache.fineract.ui.online.accounting.accounts.accounttasks

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_account_tasks_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_group_tasks_bottom_sheet.view.*
import org.apache.fineract.R
import org.apache.fineract.data.Status
import org.apache.fineract.data.models.accounts.Account
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseBottomSheetDialogFragment
import org.apache.fineract.ui.base.Toaster
import org.apache.fineract.ui.online.accounting.accounts.viewmodel.AccountsViewModel
import org.apache.fineract.ui.online.accounting.accounts.viewmodel.AccountsViewModelFactory
import javax.inject.Inject


/**
 * Created by Varun Jain on 24/July/2021
 */

class AccountTaskBottomSheetFragment(val account: Account) : FineractBaseBottomSheetDialogFragment() {

    lateinit var rootView: View
    private var accountCommand = AccountCommand()
    lateinit var behavior: BottomSheetBehavior<*>
    private lateinit var viewModel: AccountsViewModel

    @Inject
    lateinit var viewModelFactory: AccountsViewModelFactory

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        rootView = View.inflate(context, R.layout.fragment_account_tasks_bottom_sheet, null)
        dialog.setContentView(rootView)
        behavior = BottomSheetBehavior.from(rootView.parent as View)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        ButterKnife.bind(this, rootView)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AccountsViewModel::class.java)
        setDataOnViews()

        subscribeUI()
        return dialog
    }

    private fun subscribeUI() {
        viewModel.status.observe(this, Observer { status ->
            when (status) {
                Status.LOADING -> {
                    showMifosProgressDialog(getString(R.string.please_wait_updating_account_status))
                }
                Status.ERROR -> {
                    hideMifosProgressDialog()
                    Toaster.show(rootView, R.string.error_while_updating_account_status, Toast.LENGTH_SHORT)
                }
                Status.DONE -> {
                    Toaster.show(rootView, getString(R.string.account_identifier_updated_successfully, account.identifier), Toast.LENGTH_SHORT)
                    hideMifosProgressDialog()
                    dismiss()
                }
            }
        })
    }

    private fun setDataOnViews() {
        when (account.state) {
            Account.State.OPEN -> {
                rootView.ivAccountTask.setImageDrawable(
                    ContextCompat.getDrawable(activity!!, R.drawable.ic_lock_black_24dp))
                rootView.ivAccountTask.setColorFilter(ContextCompat.getColor(activity!!, R.color.red_dark))
                rootView.tvAccountTask.text = getString(R.string.lock)
                rootView.llAccountTask2.visibility = View.VISIBLE
                rootView.ivAccountTask2.setImageDrawable(
                    ContextCompat.getDrawable(activity!!, R.drawable.ic_close_black_24dp))
                rootView.ivAccountTask2.setColorFilter(ContextCompat.getColor(activity!!, R.color.red_dark))
                rootView.tvAccountTask2.text = getString(R.string.close)

            }
            Account.State.LOCKED -> {
                rootView.ivAccountTask.setImageDrawable(
                    ContextCompat.getDrawable(activity!!, R.drawable.ic_lock_open_black_24dp))
                rootView.ivAccountTask.setColorFilter(ContextCompat.getColor(activity!!, R.color.status))
                rootView.tvAccountTask.text = getString(R.string.un_lock)

            }
            Account.State.CLOSED -> {
                rootView.ivAccountTask.setImageDrawable(
                    ContextCompat.getDrawable(activity!!, R.drawable.ic_check_circle_black_24dp))
                rootView.ivAccountTask.setColorFilter(ContextCompat.getColor(activity!!, R.color.status))
                rootView.tvAccountTask.text = getString(R.string.reopen)

            }
        }
    }

    @OnClick(R.id.ivAccountTask)
    fun onTaskImageViewClicked() {
        when (account.state) {
            Account.State.OPEN -> {
                accountCommand.action = AccountCommand.AccountTaskAction.LOCK
                rootView.tvAccountTaskHeader.text = getString(R.string.lock)
                rootView.tvAccountTaskSubHeader.text = getString(R.string.please_verify_following_account_task, getString(R.string.lock))
                rootView.btnSubmitAccountTask.text = getString(R.string.lock)
            }
            Account.State.LOCKED -> {
                accountCommand.action = AccountCommand.AccountTaskAction.UNLOCK
                rootView.tvAccountTaskHeader.text = getString(R.string.un_lock)
                rootView.tvAccountTaskSubHeader.text = getString(R.string.please_verify_following_account_task, getString(R.string.un_lock))
                rootView.btnSubmitAccountTask.text = getString(R.string.un_lock)
            }
            Account.State.CLOSED -> {
                accountCommand.action = AccountCommand.AccountTaskAction.REOPEN
                rootView.tvAccountTaskHeader.text = getString(R.string.reopen)
                rootView.tvAccountTaskSubHeader.text = getString(R.string.please_verify_following_account_task, getString(R.string.reopen))
                rootView.btnSubmitAccountTask.text = getString(R.string.reopen)
            }
        }
        rootView.llAccountTaskList.visibility = View.GONE
        rootView.llAccountTaskForm.visibility = View.VISIBLE
    }

    @OnClick(R.id.ivAccountTask2)
    fun onTaskImageView2Clicked() {
        when (account.state) {
            Account.State.OPEN -> {
                accountCommand.action = AccountCommand.AccountTaskAction.CLOSE
                rootView.tvAccountTaskHeader.text = getString(R.string.close)
                rootView.tvAccountTaskSubHeader.text = getString(R.string.please_verify_following_account_task, getString(R.string.close))
                rootView.btnSubmitAccountTask.text = getString(R.string.close)
            }
        }
        rootView.llAccountTask2.visibility = View.INVISIBLE
        rootView.llAccountTaskList.visibility = View.GONE
        rootView.llAccountTaskForm.visibility = View.VISIBLE
    }

    @OnClick(R.id.btnSubmitAccountTask)
    fun submitTask() {
        accountCommand.comment = rootView.etCommentAccountTasks.text.toString().trim { it <= ' ' }
        rootView.etCommentAccountTasks.isEnabled = false
        account.identifier?.let {
            viewModel.changeAccountStatus(it, account, accountCommand)
        }
        activity!!.finish()
    }

    @OnClick(R.id.btnCancelAccountTasks)
    fun onCancel() {
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDestroy() {
        super.onDestroy()
        hideMifosProgressBar()
    }

}