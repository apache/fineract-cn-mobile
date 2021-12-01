package org.apache.fineract.ui.online.teller.tellertasks

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
import kotlinx.android.synthetic.main.fragment_teller_tasks_bottom_sheet.view.*
import org.apache.fineract.R
import org.apache.fineract.data.Status
import org.apache.fineract.data.models.teller.Teller
import org.apache.fineract.data.models.teller.TellerCommand
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseBottomSheetDialogFragment
import org.apache.fineract.ui.base.Toaster
import org.apache.fineract.ui.online.teller.tellerlist.TellerViewModel
import org.apache.fineract.ui.online.teller.tellerlist.TellerViewModelFactory
import javax.inject.Inject

/*
 * Created by Varun Jain on 21.06.2021
*/

class TellerTasksBottomSheetFragment(val teller: Teller) : FineractBaseBottomSheetDialogFragment() {

    lateinit var rootView: View
    private var command = TellerCommand()
    lateinit var behavior: BottomSheetBehavior<*>
    private lateinit var viewModel: TellerViewModel

    @Inject
    lateinit var viewModelFactory: TellerViewModelFactory

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        rootView = View.inflate(context, R.layout.fragment_teller_tasks_bottom_sheet, null)
        dialog.setContentView(rootView)
        behavior = BottomSheetBehavior.from(rootView.parent as View)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        ButterKnife.bind(this, rootView)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TellerViewModel::class.java)

        setDataOnViews()
        subscribeUI()
        return dialog
    }

    private fun subscribeUI() {
        viewModel.status.observe(this, Observer { status ->
            when (status) {
                Status.LOADING -> {
                    showMifosProgressDialog(getString(R.string.please_wait_updating_teller_status))
                }
                Status.ERROR -> {
                    hideMifosProgressDialog()
                    Toaster.show(rootView, R.string.error_while_updating_teller_status, Toast.LENGTH_SHORT)
                }
                Status.DONE -> {
                    Toaster.show(rootView, getString(R.string.teller_identifier_updated_successfully, teller.tellerAccountIdentifier), Toast.LENGTH_SHORT)
                    hideMifosProgressDialog()
                    dismiss()
                }
            }
        })
    }

    private fun setDataOnViews() {
        when (teller.state) {
            Teller.State.ACTIVE -> {
                rootView.ivTellerTask.setImageDrawable(
                    ContextCompat.getDrawable(activity!!, R.drawable.ic_close_black_24dp))
                rootView.ivTellerTask.setColorFilter(ContextCompat.getColor(activity!!, R.color.red_dark))
                rootView.tvTellerTask.text = getString(R.string.close)
            }
            Teller.State.OPEN -> {
                rootView.ivTellerTask.setImageDrawable(
                    ContextCompat.getDrawable(activity!!, R.drawable.ic_close_black_24dp))
                rootView.ivTellerTask.setColorFilter(ContextCompat.getColor(activity!!, R.color.red_dark))
                rootView.tvTellerTask.text = getString(R.string.close)
            }
            Teller.State.CLOSED -> {
                rootView.ivTellerTask.setImageDrawable(
                    ContextCompat.getDrawable(activity!!, R.drawable.ic_check_circle_black_24dp))
                rootView.ivTellerTask.setColorFilter(ContextCompat.getColor(activity!!, R.color.status))
                rootView.tvTellerTask.text = getString(R.string.reopen)
            }
            Teller.State.PAUSED -> {
                rootView.ivTellerTask.setImageDrawable(
                    ContextCompat.getDrawable(activity!!, R.drawable.ic_check_circle_black_24dp))
                rootView.ivTellerTask.setColorFilter(ContextCompat.getColor(activity!!, R.color.status))
                rootView.tvTellerTask.text = getString(R.string.activate)
            }
        }
    }

    @OnClick(R.id.ivTellerTask)
    fun onTaskImageViewClicked() {
        when (teller.state) {
            Teller.State.ACTIVE -> {
                command.action = TellerCommand.TellerAction.CLOSE
                command.assignedEmployeeIdentifier = teller.assignedEmployee.toString()
                rootView.tvTellerHeader.text = getString(R.string.close)
                rootView.tvTellerSubHeader.text = getString(R.string.please_verify_following_teller_task, getString(R.string.close))
                rootView.btnTellerSubmitTask.text = getString(R.string.close)
            }
            Teller.State.PAUSED -> {
                command.action = TellerCommand.TellerAction.ACTIVATE
                command.assignedEmployeeIdentifier = teller.assignedEmployee.toString()
                rootView.tvTellerHeader.text = getString(R.string.activate)
                rootView.tvTellerSubHeader.text = getString(R.string.please_verify_following_teller_task, getString(R.string.activate))
                rootView.btnTellerSubmitTask.text = getString(R.string.activate)
            }
            Teller.State.CLOSED -> {
                command.action = TellerCommand.TellerAction.REOPEN
                command.assignedEmployeeIdentifier = teller.assignedEmployee.toString()
                rootView.tvTellerHeader.text = getString(R.string.reopen)
                rootView.tvTellerSubHeader.text = getString(R.string.please_verify_following_teller_task, getString(R.string.reopen))
                rootView.btnTellerSubmitTask.text = getString(R.string.reopen)
            }
            Teller.State.OPEN -> {
                command.action = TellerCommand.TellerAction.CLOSE
                command.assignedEmployeeIdentifier = teller.assignedEmployee.toString()
                rootView.tvTellerHeader.text = getString(R.string.close)
                rootView.tvTellerSubHeader.text = getString(R.string.please_verify_following_teller_task, getString(R.string.close))
                rootView.btnTellerSubmitTask.text = getString(R.string.close)
            }
        }
        rootView.llTellerTaskList.visibility = View.GONE
        rootView.llTellerTaskForm.visibility = View.VISIBLE
    }

    @OnClick(R.id.btnTellerSubmitTask)
    fun submitTask() {
        viewModel.changeTellerStatus(teller, command)
        activity!!.finish()
    }

    @OnClick(R.id.btnTellerCancel)
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