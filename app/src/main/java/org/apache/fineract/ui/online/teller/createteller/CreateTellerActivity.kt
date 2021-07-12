package org.apache.fineract.ui.online.teller.createteller

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.activity_create_teller.*
import org.apache.fineract.R
import org.apache.fineract.data.Status
import org.apache.fineract.data.models.teller.Teller
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.Toaster
import org.apache.fineract.ui.online.teller.TellerAction
import org.apache.fineract.ui.online.teller.tellerlist.TellerViewModel
import org.apache.fineract.ui.online.teller.tellerlist.TellerViewModelFactory
import org.apache.fineract.utils.Constants
import java.math.BigDecimal
import javax.inject.Inject

/*
 * Created by Varun Jain on 16.06.2021
*/

class CreateTellerActivity : FineractBaseActivity(), StepperLayout.StepperListener  {

    private var teller = Teller()
    private var tellerAction = TellerAction.CREATE

    @Inject
    lateinit var tellerViewModelFactory: TellerViewModelFactory

    lateinit var tellerViewModel: TellerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_teller)
        activityComponent.inject(this)
        tellerAction = intent.getSerializableExtra(Constants.TELLER_ACTION) as TellerAction
        when (tellerAction) {
            TellerAction.CREATE -> {
                setToolbarTitle(getString(R.string.create_teller))
            }
            TellerAction.EDIT -> {
                setToolbarTitle(getString(R.string.edit_teller))
                intent?.extras?.getParcelable<Teller>(Constants.TELLER)?.let {
                    teller = it
                }
            }
        }
        tellerViewModel = ViewModelProviders.of(this, tellerViewModelFactory).get(TellerViewModel::class.java)
        subscribeUI()
        showBackButton()
        slCreateTeller.adapter = CreateTellerAdapter(supportFragmentManager, this, tellerAction)
        slCreateTeller.setOffscreenPageLimit(slCreateTeller.adapter.count)
        slCreateTeller.setListener(this)
    }

    private fun subscribeUI() {
        tellerViewModel.status.observe(this, Observer { status ->
            when (status) {
                Status.LOADING ->
                    if (tellerAction == TellerAction.CREATE) {
                        showMifosProgressDialog(getString(R.string.creating_teller))
                    } else {
                        showMifosProgressDialog(getString(R.string.updating_teller_please_wait))
                    }
                Status.ERROR -> {
                    hideMifosProgressDialog()
                    if (tellerAction == TellerAction.CREATE) {
                        Toaster.show(findViewById(android.R.id.content), R.string.error_while_creating_teller, Toast.LENGTH_SHORT)
                    } else {
                        Toaster.show(findViewById(android.R.id.content), R.string.error_while_updating_teller, Toast.LENGTH_SHORT)
                    }
                }
                Status.DONE -> {
                    hideMifosProgressDialog()
                    if (tellerAction == TellerAction.CREATE) {
                        Toast.makeText(this, getString(R.string.teller_identifier_created_successfully, teller.tellerAccountIdentifier), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, getString(R.string.teller_identifier_updated_successfully, teller.tellerAccountIdentifier), Toast.LENGTH_SHORT).show()
                    }
                    finish()
                }
            }
        })
    }

    override fun onCompleted(completeButton: View?) {
        when (tellerAction) {
            TellerAction.EDIT -> teller.tellerAccountIdentifier?.let {
                tellerViewModel.updateTeller(teller)
            }
            TellerAction.CREATE -> tellerViewModel.createTeller(teller)
        }
    }

    override fun onError(verificationError: VerificationError?) {}

    override fun onStepSelected(newStepPosition: Int) {}

    override fun onReturn() {}

    fun setTellerDetails(code: String,
                         password: String,
                         cashwithdrawlimit: BigDecimal,
                         telleracc_identifier: String,
                         vaultacc_identifier: String,
                         cheaquesrecievableamount: String,
                         cashovershortacc: String,
                         denomation_req: Boolean,
                         assignedEmployee: String) {
        teller.code = code
        teller.password = password
        teller.cashdrawLimit = cashwithdrawlimit
        teller.tellerAccountIdentifier = telleracc_identifier
        teller.vaultAccountIdentifier = vaultacc_identifier
        teller.chequesReceivableAccount = cheaquesrecievableamount
        teller.cashOverShortAccount = cashovershortacc
        teller.denominationRequired = denomation_req
        teller.assignedEmployee = assignedEmployee

    }

    fun getTeller(): Teller {
        return teller
    }
}