package org.apache.fineract.ui.online.groups.grouptasks

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
import kotlinx.android.synthetic.main.fragment_group_tasks_bottom_sheet.view.*
import org.apache.fineract.R
import org.apache.fineract.data.Status
import org.apache.fineract.data.models.Group
import org.apache.fineract.data.models.customer.Command
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseBottomSheetDialogFragment
import org.apache.fineract.ui.base.Toaster
import org.apache.fineract.ui.online.groups.grouplist.GroupViewModel
import org.apache.fineract.ui.online.groups.grouplist.GroupViewModelFactory
import javax.inject.Inject

/**
 * Created by Ahmad Jawid Muhammadi on 04/April/2020
 */
class GroupTasksBottomSheetFragment(val group: Group) : FineractBaseBottomSheetDialogFragment() {

    lateinit var rootView: View
    private var command = Command()
    lateinit var behavior: BottomSheetBehavior<*>
    private lateinit var viewModel: GroupViewModel

    @Inject
    lateinit var viewModelFactory: GroupViewModelFactory

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        rootView = View.inflate(context, R.layout.fragment_group_tasks_bottom_sheet, null)
        dialog.setContentView(rootView)
        behavior = BottomSheetBehavior.from(rootView.parent as View)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        ButterKnife.bind(this, rootView)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GroupViewModel::class.java)
        setDataOnViews()

        subscribeUI()
        return dialog
    }

    private fun subscribeUI() {
        viewModel.status.observe(this, Observer { status ->
            when (status) {
                Status.LOADING -> {
                    showMifosProgressDialog(getString(R.string.please_wait_updating_group_status))
                }
                Status.ERROR -> {
                    hideMifosProgressDialog()
                    Toaster.show(rootView, R.string.error_while_updating_group_status, Toast.LENGTH_SHORT)
                }
                Status.DONE -> {
                    Toaster.show(rootView, getString(R.string.group_identifier_updated_successfully, group.identifier), Toast.LENGTH_SHORT)
                    hideMifosProgressDialog()
                    dismiss()
                }
            }
        })
    }

    private fun setDataOnViews() {
        when (group.status) {
            Group.Status.ACTIVE -> {
                rootView.iv_task.setImageDrawable(
                        ContextCompat.getDrawable(activity!!, R.drawable.ic_close_black_24dp))
                rootView.iv_task.setColorFilter(ContextCompat.getColor(activity!!, R.color.red_dark))
                rootView.tv_task.text = getString(R.string.close)
            }
            Group.Status.PENDING -> {
                rootView.iv_task.setImageDrawable(ContextCompat.getDrawable(activity!!,
                        R.drawable.ic_check_circle_black_24dp))
                rootView.iv_task.setColorFilter(ContextCompat.getColor(activity!!, R.color.status))
                rootView.tv_task.text = getString(R.string.activate)
            }
            Group.Status.CLOSED -> {
                rootView.iv_task.setImageDrawable(ContextCompat.getDrawable(activity!!,
                        R.drawable.ic_check_circle_black_24dp))
                rootView.iv_task.setColorFilter(ContextCompat.getColor(activity!!, R.color.status))
                rootView.tv_task.text = getString(R.string.reopen)
            }
        }
    }

    @OnClick(R.id.iv_task)
    fun onTaskImageViewClicked() {
        when (group.status) {
            Group.Status.ACTIVE -> {
                command.action = Command.Action.CLOSE
                rootView.tv_header.text = getString(R.string.close)
                rootView.tv_sub_header.text = getString(R.string.please_verify_following_group_task, getString(R.string.lock))
                rootView.btn_submit_task.text = getString(R.string.close)
            }
            Group.Status.PENDING -> {
                command.action = Command.Action.ACTIVATE
                rootView.tv_header.text = getString(R.string.activate)
                rootView.tv_sub_header.text = getString(R.string.please_verify_following_group_task, getString(R.string.activate))
                rootView.btn_submit_task.text = getString(R.string.activate)
            }
            Group.Status.CLOSED -> {
                command.action = Command.Action.REOPEN
                rootView.tv_header.text = getString(R.string.reopen)
                rootView.tv_sub_header.text = getString(R.string.please_verify_following_group_task, getString(R.string.reopen))
                rootView.btn_submit_task.text = getString(R.string.reopen)
            }
        }
        rootView.ll_task_list.visibility = View.GONE
        rootView.ll_task_form.visibility = View.VISIBLE
    }

    @OnClick(R.id.btn_submit_task)
    fun submitTask() {
        if (rootView.et_comment.text.toString().isEmpty()) {
            rootView.et_comment.error =
                    getString(R.string.required)
            return
        }
        command.comment = rootView.et_comment.text.toString().trim { it <= ' ' }
        rootView.et_comment.isEnabled = false
        group.identifier?.let {
            viewModel.changeGroupStatus(it, group, command)
        }
    }

    @OnClick(R.id.btn_cancel)
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
