package org.apache.fineract.ui.online.customers.customerpayroll.editcustomerpayroll

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_edit_payroll_allocation.*
import org.apache.fineract.R
import org.apache.fineract.data.models.payroll.PayrollAllocation
import org.apache.fineract.data.models.payroll.PayrollConfiguration
import org.apache.fineract.ui.adapters.PayrollAllocationAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.online.customers.customerpayroll.editcustomerpayroll
        .editpayrollbottomsheet.EditPayrollBottomSheet
import org.apache.fineract.ui.online.customers.customerpayroll.editcustomerpayroll
        .editpayrollbottomsheet.OnBottomSheetDialogListener
import org.apache.fineract.ui.online.customers.customerpayroll.editcustomerpayroll.editpayrollbottomsheet.PayrollSource
import org.apache.fineract.utils.ConstantKeys
import javax.inject.Inject


class EditPayrollAllocationFragment : FineractBaseFragment(), Step,
        PayrollAllocationAdapter.OnClickEditDeleteListener,
        OnBottomSheetDialogListener {

    lateinit var payrollConfiguration: PayrollConfiguration
    lateinit var listener: OnNavigationBarListener.Payroll
    lateinit var payrollAllocations: MutableList<PayrollAllocation>

    @Inject
    lateinit var payrollAllocationAdapter: PayrollAllocationAdapter

    companion object {
        fun newInstance(payrollConfiguration: PayrollConfiguration) =
                EditPayrollAllocationFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ConstantKeys.PAYROLL_CONFIG, payrollConfiguration)
                    }
                }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnNavigationBarListener.Payroll) {
            listener = context
        } else {
            throw RuntimeException(context.toString() +
                    " must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            payrollConfiguration = it.getParcelable(ConstantKeys.PAYROLL_CONFIG)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as FineractBaseActivity).activityComponent.inject(this)
        return inflater.inflate(R.layout.fragment_edit_payroll_allocation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showUserInterface()
        fabAddPayrollAllocation.setOnClickListener {
            val editPayrollBottomSheet = EditPayrollBottomSheet()
            editPayrollBottomSheet.addPayrollSource(PayrollSource.ADD)
            editPayrollBottomSheet.setBottomSheetListener(this)
            editPayrollBottomSheet.show(childFragmentManager, getString(R.string.payroll_allocation))
        }
    }

    fun showUserInterface() {

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = RecyclerView.VERTICAL
        rvPayrollAllocation.layoutManager = layoutManager
        rvPayrollAllocation.setHasFixedSize(true)

        this.payrollAllocations = payrollConfiguration.payrollAllocations.toMutableList()
        payrollAllocationAdapter.setPayrollAllocations(payrollAllocations)
        payrollAllocationAdapter.setOnClickEditDeleteListener(this)
        rvPayrollAllocation.adapter = payrollAllocationAdapter
    }

    override fun onSelected() {

    }

    override fun onClickEdit(payrollAllocation: PayrollAllocation, position: Int) {
        val editPayrollBottomSheet = EditPayrollBottomSheet()
        editPayrollBottomSheet.addPayrollSource(PayrollSource.EDIT)
        editPayrollBottomSheet.editPayrollAllocation(payrollAllocation, position)
        editPayrollBottomSheet.setBottomSheetListener(this)
        editPayrollBottomSheet.show(childFragmentManager, getString(R.string.payroll_allocation))
    }

    override fun onClickDelete(payrollAllocation: PayrollAllocation, position: Int) {
        payrollAllocations.removeAt(position)
        payrollAllocationAdapter.setPayrollAllocations(payrollAllocations)
    }

    override fun editPayrollAllocation(payrollAllocation: PayrollAllocation, position: Int) {
        payrollAllocations.removeAt(position)
        payrollAllocations.add(position, payrollAllocation)
        payrollAllocationAdapter.setPayrollAllocations(payrollAllocations)
    }

    override fun addPayrollAllocation(payrollAllocation: PayrollAllocation) {
        payrollAllocations.add(payrollAllocation)
        payrollAllocationAdapter.setPayrollAllocations(payrollAllocations)
    }

    override fun verifyStep(): VerificationError? {
        listener.setPayrollAllocations(payrollAllocations.toList())
        return null
    }

    override fun onError(error: VerificationError) {

    }

}
