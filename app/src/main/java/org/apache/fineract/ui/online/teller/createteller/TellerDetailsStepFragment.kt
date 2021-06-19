package org.apache.fineract.ui.online.teller.createteller

import android.os.Bundle
import android.view.LayoutInflater
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.online.teller.TellerAction
import org.apache.fineract.utils.Constants
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.fragment_step_teller_details.*
import kotlinx.android.synthetic.main.fragment_step_teller_details.view.*
import org.apache.fineract.R
import java.math.BigDecimal

/*
 * Created by Varun Jain on 16.06.2021
*/

class TellerDetailsStepFragment : FineractBaseFragment(), Step {

    lateinit var rootView: View
    private lateinit var tellerAction: TellerAction

    companion object {
        fun newInstance(tellerAction: TellerAction) =
            TellerDetailsStepFragment().apply {
                val bundle = Bundle().apply {
                    putSerializable(Constants.TELLER_ACTION, tellerAction)
                }
                arguments = bundle
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getSerializable(Constants.TELLER_ACTION)?.let {
            tellerAction = it as TellerAction
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_step_teller_details, container, false)
        if (tellerAction == TellerAction.EDIT) {
            populateData()
        }
        return rootView
    }

    private fun populateData() {
        val teller = (activity as CreateTellerActivity).getTeller()
        rootView.etTellerCode.setText(teller.code)
        rootView.etTellerPassword.setText(teller.password)
        rootView.etTellerCashdrawlimit.setText(teller.cashdrawLimit.toString())
        rootView.etTellerAccountIdentifier.setText(teller.tellerAccountIdentifier)
        rootView.etTellerVaultIdentifier.setText(teller.vaultAccountIdentifier)
        rootView.cbTellerDenominationRequired.isChecked = teller.denominationRequired
        rootView.etTellerCheaque.setText(teller.chequesReceivableAccount)
        rootView.etTellerCashOverShortAccount.setText(teller.cashOverShortAccount)
        rootView.etTellerAssignedEmployee.setText(teller.assignedEmployee)
    }

    override fun verifyStep(): VerificationError? {
        if ( !(validateIdentifier(etTellerCode)) || !(validateIdentifier(etTellerPassword)) || !(validateIdentifier(etTellerCashdrawlimit))
            || !(validateIdentifier(etTellerAccountIdentifier)) || !(validateIdentifier(etTellerVaultIdentifier)) || !(validateIdentifier(etTellerCheaque))
            || !(validateIdentifier(etTellerCashOverShortAccount)) || !(validateIdentifier(etTellerAssignedEmployee))) {
            return VerificationError(null)
        }
        (activity as CreateTellerActivity).setTellerDetails(
               etTellerCode.text.toString(),
            etTellerPassword.text.toString(),
            BigDecimal(etTellerCashdrawlimit.text.toString()),
            etTellerAccountIdentifier.text.toString(),
            etTellerVaultIdentifier.text.toString(),
            etTellerCheaque.text.toString(),
            etTellerCashOverShortAccount.text.toString(),
            cbTellerDenominationRequired.isChecked,
            etTellerAssignedEmployee.text.toString()
        )
        return null
    }

    private fun validateIdentifier(editText: EditText): Boolean {
        return editText.validator()
            .nonEmpty()
            .addErrorCallback {
                editText.error = it
            }.check()
    }

    override fun onSelected() {}

    override fun onError(error: VerificationError) {}
}