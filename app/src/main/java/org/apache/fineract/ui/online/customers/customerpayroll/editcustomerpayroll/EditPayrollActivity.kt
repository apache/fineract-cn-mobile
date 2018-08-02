package org.apache.fineract.ui.online.customers.customerpayroll.editcustomerpayroll

import android.os.Bundle
import android.view.View
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.activity_edit_payroll.*
import org.apache.fineract.R
import org.apache.fineract.data.models.payroll.PayrollAllocation
import org.apache.fineract.data.models.payroll.PayrollConfiguration
import org.apache.fineract.ui.adapters.EditPayrollStepAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.Toaster
import org.apache.fineract.ui.online.accounting.accounts.EditPayrollContract
import org.apache.fineract.utils.ConstantKeys
import javax.inject.Inject

class EditPayrollActivity : FineractBaseActivity(), StepperLayout.StepperListener,
        OnNavigationBarListener.Payroll, EditPayrollContract.View {

    lateinit var payrollConfig: PayrollConfiguration
    lateinit var customerIdentifier: String

    @Inject
    lateinit var editPayrollPresenter: EditPayrollPresenter

    override fun onStepSelected(newStepPosition: Int) {

    }

    override fun onError(verificationError: VerificationError?) {
    }

    override fun onReturn() {
    }

    override fun onCompleted(completeButton: View?) {
        editPayrollPresenter.updatePayrollConfiguration(customerIdentifier, payrollConfig)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_payroll)

        setToolbarTitle(getString(R.string.edit_payroll))
        activityComponent.inject(this)
        editPayrollPresenter.attachView(this)
        val payrollConfig = intent.getParcelableExtra<PayrollConfiguration>(ConstantKeys
                .PAYROLL_CONFIG)

        customerIdentifier = intent.getStringExtra(ConstantKeys.CUSTOMER_IDENTIFIER)

        val stepAdapter = EditPayrollStepAdapter(
                supportFragmentManager, this, payrollConfig)
        stepperLayout.adapter = stepAdapter
        stepperLayout.setListener(this)
        stepperLayout.setOffscreenPageLimit(stepAdapter.count)

        showBackButton()
    }

    override fun setPayrollConfig(accountNo: String, lastModifiedBy: String,
                                  lastModifiedOn: String, createdBy: String,
                                  createdOn: String) {
        payrollConfig = PayrollConfiguration(mainAccountNumber = accountNo,
                createdOn = createdOn, createdBy = createdBy,
                lastModifiedOn = lastModifiedOn, lastModifiedBy = lastModifiedBy)

    }

    override fun setPayrollAllocations(payrollAllocations: List<PayrollAllocation>) {
        payrollConfig = payrollConfig.copy(payrollAllocations = payrollAllocations)
    }

    override fun updatePayrollSuccess() {
        finish()
    }

    override fun showNoInternetConnection() {
        Toaster.show(findViewById(android.R.id.content),
                getString(R.string.no_internet_connection))
    }

    override fun showError(message: String?) {
        Toaster.show(findViewById(android.R.id.content), message)
    }

    override fun onDestroy() {
        super.onDestroy()
        editPayrollPresenter.detachView()
    }
}
