package org.apache.fineract.ui.online.customers.customerpayroll


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_payroll.*
import kotlinx.android.synthetic.main.layout_exception_handler.*
import org.apache.fineract.R
import org.apache.fineract.data.models.payroll.PayrollConfiguration
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.online.customers.customerpayroll.editcustomerpayroll.EditPayrollActivity
import org.apache.fineract.utils.ConstantKeys
import javax.inject.Inject


class PayrollFragment : FineractBaseFragment(), PayrollContract.View {

    @Inject
    lateinit var payrollPresenter: PayrollPresenter

    lateinit var customerIdentifier: String

    lateinit var payrollConfiguration: PayrollConfiguration

    companion object {
        fun newInstance(customerIdentifier: String) =
                PayrollFragment().apply {
                    arguments = Bundle().apply {
                        putString(ConstantKeys.CUSTOMER_IDENTIFIER, customerIdentifier)
                    }
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            customerIdentifier = it.getString(ConstantKeys.CUSTOMER_IDENTIFIER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_payroll, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        payrollPresenter.attachView(this)
        initializeFineractUIErrorHandler(activity, rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showUserInterface()

        btn_try_again.setOnClickListener {
            layoutError.visibility = View.GONE
            payrollPresenter.getPayrollConfiguration(customerIdentifier)
        }

        fabEditPayroll.setOnClickListener{
            val intent = Intent(activity,EditPayrollActivity::class.java)
            intent.putExtra(ConstantKeys.PAYROLL_CONFIG, payrollConfiguration)
            intent.putExtra(ConstantKeys.CUSTOMER_IDENTIFIER,customerIdentifier)
            startActivity(intent)
        }

        payrollPresenter.getPayrollConfiguration(customerIdentifier)
    }

    override fun showUserInterface() {
        setToolbarTitle(getString(R.string.payroll))
        clPayroll.visibility = View.INVISIBLE
    }

    override fun showPayrollConfiguration(payrollConfiguration: PayrollConfiguration) {

        this.payrollConfiguration = payrollConfiguration
        clPayroll.visibility = View.VISIBLE
        tvAccount.text = payrollConfiguration.mainAccountNumber
        tvCreatedBy.text = payrollConfiguration.createdBy
        tvLastModifiedBy.text = payrollConfiguration.lastModifiedBy

        if (payrollConfiguration.payrollAllocations.isEmpty()) {
            tvPayrollAllocations.text = getString(R.string.no_payroll_allocation)
        } else {
            val accNo = StringBuilder()
            payrollConfiguration.payrollAllocations.forEach {
                accNo.append("${it.accountNumber}\n")
            }

            tvPayrollAllocations.text = accNo.toString()
        }
    }


    override fun showProgressbar() {
        showMifosProgressBar()
    }

    override fun hideProgressbar() {
        hideMifosProgressBar()
    }

    override fun showNoInternetConnection() {
        clPayroll.visibility = View.INVISIBLE
        showFineractNoInternetUI()
    }

    override fun showError(message: String?) {
        showFineractErrorUI(getString(R.string.payroll_configuration))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        payrollPresenter.detachView()
    }
}
