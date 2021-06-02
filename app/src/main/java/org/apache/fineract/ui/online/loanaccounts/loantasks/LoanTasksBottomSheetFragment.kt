package org.apache.fineract.ui.online.loanaccounts.loantasks

import android.app.Dialog
import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_loan_task.*
import kotlinx.android.synthetic.main.bottom_sheet_loan_task.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.customer.Command
import org.apache.fineract.data.models.loan.LoanAccount
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseBottomSheetDialogFragment
import org.apache.fineract.ui.base.Toaster
import javax.inject.Inject

/**
 * Created by Ahmad Jawid Muhammadi on 6/7/20.
 */

class LoanTasksBottomSheetFragment @Inject constructor() : FineractBaseBottomSheetDialogFragment(),
        LoanTasksBottomSheetContract.View {

    private lateinit var rootView: View
    private lateinit var behavior: BottomSheetBehavior<View>
    private lateinit var command: Command

    private var identifier: String? = null
    private var loanState: LoanAccount.State? = LoanAccount.State.APPROVED

    @Inject
    lateinit var loanTasksBottomSheetPresenter: LoanTasksBottomSheetPresenter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        rootView = View.inflate(context, R.layout.bottom_sheet_loan_task, null)
        dialog.setContentView(rootView)
        behavior = BottomSheetBehavior.from(rootView.parent as View)
        (activity as FineractBaseActivity?)?.activityComponent?.inject(this)
        loanTasksBottomSheetPresenter.attachView(this)
        ButterKnife.bind(this, rootView)
        command = Command()

        //  rootView.llTasComment.visibility = View.GONE
        return dialog
    }


    @OnClick(R.id.iv_task1)
    fun onTaskClicked() {
        when (loanState) {
            LoanAccount.State.APPROVED -> {
                rootView.tvHeader.text = getString(R.string.approve)
                rootView.btnSubmitTask.text = getString(R.string.approve)
                rootView.tvSubHeader.text = getString(R.string.please_verify_following_loan_task, getString(R.string.approve))
            }

        }
        rootView.llLoanTasks.visibility = View.GONE
        rootView.llTasComment.visibility = View.VISIBLE
        //  loanTasksBottomSheetPresenter.changeLoanStatus()
    }

    @OnClick(R.id.ivReject)
    fun onRejectClicked() {

    }

    @OnClick(R.id.ivDelete)
    fun onDeleteClicked() {

    }

    @OnClick(R.id.btnCancel)
    fun onCancelClicked() {
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        dismiss()
    }


    override fun statusChangedSuccessfully() {
        Toaster.show(llLoanTasks, getString(R.string.loan_status_changed_successfully))
        dismiss()
    }

    override fun showProgressbar() {
        showMifosProgressBar()
    }

    override fun hideProgressbar() {
        hideMifosProgressBar()
    }

    override fun showError(message: String?) {
        dismiss()
    }

    override fun showNoInternetConnection() {}

    fun setIdentifier(identifier: String) {
        this.identifier = identifier
    }

    fun setLoanState(state: LoanAccount.State) {
        loanState = state
    }
}