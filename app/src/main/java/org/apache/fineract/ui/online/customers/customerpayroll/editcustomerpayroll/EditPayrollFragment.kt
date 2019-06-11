package org.apache.fineract.ui.online.customers.customerpayroll.editcustomerpayroll

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_edit_payroll.*
import org.apache.fineract.R
import org.apache.fineract.data.models.customer.DateOfBirth
import org.apache.fineract.data.models.payroll.PayrollConfiguration
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.utils.ConstantKeys
import java.text.SimpleDateFormat
import java.util.*


class EditPayrollFragment : FineractBaseFragment(), Step {

    lateinit var payrollConfiguration: PayrollConfiguration
    lateinit var listener: OnNavigationBarListener.Payroll

    val calendar: Calendar = Calendar.getInstance()

    val dateOfBirth by lazy { DateOfBirth() }

    companion object {
        fun newInstance(payrollConfiguration: PayrollConfiguration) = EditPayrollFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ConstantKeys.PAYROLL_CONFIG, payrollConfiguration)
            }
        }

        const val DATE_FORMAT = "dd MMM yyyy"
    }

    override fun onAttach(context: Context) {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_payroll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showUserInterface()
    }

    fun showUserInterface() {

        etAccount.setText(payrollConfiguration.mainAccountNumber)
        etCreatedBy.setText(payrollConfiguration.createdBy)
        etLastModifiedBy.setText(payrollConfiguration.lastModifiedBy)
        etCreatedOn.setText(payrollConfiguration.createdOn)
        etLastModifiedOn.setText(payrollConfiguration.lastModifiedOn)

        etLastModifiedOn.setOnClickListener {
            showDatePicker(it)
        }

        etCreatedOn.setOnClickListener {
            showDatePicker(it)
        }

    }

    private fun showDatePicker(buttonView: View) {
        val datePickerDialog = DatePickerDialog(activity!!,
                R.style.MaterialDatePickerTheme,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    dateOfBirth.day = dayOfMonth
                    dateOfBirth.month = monthOfYear + 1
                    dateOfBirth.year = year

                    if (buttonView.id == R.id.etLastModifiedOn) {
                        setLastModifiedDate()
                    } else {
                        setCreatedOnDate()
                    }

                }, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    fun setLastModifiedDate() {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
        etLastModifiedOn.setText(sdf.format(calendar.time))
    }

    fun setCreatedOnDate() {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
        etCreatedOn.setText(sdf.format(calendar.time))
    }


    override fun onSelected() {
    }

    override fun verifyStep(): VerificationError? {
        if (TextUtils.isEmpty(etAccount.text.toString()) ||
                TextUtils.isEmpty(etCreatedBy.text.toString())
                || TextUtils.isEmpty(etLastModifiedBy.text.toString())) {
            return VerificationError(null)

        } else {

            listener.setPayrollConfig(etAccount.text.toString(), etLastModifiedBy.text.toString(),
                    etLastModifiedOn.text.toString(), etCreatedBy.text.toString(),
                    etCreatedOn.text.toString())
        }

        return null
    }

    override fun onError(error: VerificationError) {

    }

}
