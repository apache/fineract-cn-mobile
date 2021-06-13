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
        rootView.et_teller_code.setText(teller.code)
        rootView.et_teller_password.setText(teller.password)
        rootView.et_teller_cashdrawlimit.setText(teller.cashdrawLimit.toString())
        rootView.et_teller_accidentifier.setText(teller.tellerAccountIdentifier)
        rootView.et_teller_vault_identifer.setText(teller.vaultAccountIdentifier)
        rootView.cb_teller_denomination_req.isChecked = teller.denominationRequired
        rootView.et_teller_cheaque.setText(teller.chequesReceivableAccount)
        rootView.et_teller_cashovershortacc.setText(teller.cashOverShortAccount)
        rootView.et_teller_assigned_employee.setText(teller.assignedEmployee)
    }

    override fun verifyStep(): VerificationError? {
        if ( !(validateIdentifier(et_teller_code)) || !(validateIdentifier(et_teller_password)) || !(validateIdentifier(et_teller_cashdrawlimit))
            || !(validateIdentifier(et_teller_accidentifier)) || !(validateIdentifier(et_teller_vault_identifer)) || !(validateIdentifier(et_teller_cheaque))
            || !(validateIdentifier(et_teller_cashovershortacc)) || !(validateIdentifier(et_teller_assigned_employee))) {
            return VerificationError(null)
        }
        (activity as CreateTellerActivity).setTellerDetails(
               et_teller_code.text.toString(),
            et_teller_password.text.toString(),
            BigDecimal(et_teller_cashdrawlimit.text.toString()),
            et_teller_accidentifier.text.toString(),
            et_teller_vault_identifer.text.toString(),
            et_teller_cheaque.text.toString(),
            et_teller_cashovershortacc.text.toString(),
            cb_teller_denomination_req.isChecked,
            et_teller_assigned_employee.text.toString()
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