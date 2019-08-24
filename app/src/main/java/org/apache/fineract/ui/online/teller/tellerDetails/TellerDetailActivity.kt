package org.apache.fineract.ui.online.teller.tellerDetails

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_teller_detail.*
import org.apache.fineract.R
import org.apache.fineract.data.models.teller.Teller
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.utils.ConstantKeys

class TellerDetailActivity : FineractBaseActivity() {

    lateinit var teller: Teller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teller_detail)

        teller = intent.getParcelableExtra(ConstantKeys.TELLER)
        setToolbarTitle(getString(R.string.ledger))
        showBackButton()
        populateUserInterface()
    }

    private fun populateUserInterface() {
        tvTellerAccountIdentifier.text = teller.tellerAccountIdentifier
        tvVaultAccountIdentifier.text = teller.vaultAccountIdentifier
        tvState.text = teller.state.toString()
        tvCode.text = teller.code
        tvPassword.text = teller.password
        tvChequesReceivableAccount.text = teller.chequesReceivableAccount
        tvCashOverShortAccount.text = "$ ${teller.cashdrawLimit.toString()}"
        tvCashdrawLimit.text = teller.cashOverShortAccount
        tvAssignedEmployee.text = teller.assignedEmployee
        tvDenominationRequired.text = teller.denominationRequired.toString()
        tvCreatedBy.text = teller.createdBy
        tvCreatedOn.text = teller.createdOn
        tvLastModifiedBy.text = teller.lastModifiedBy
        tvLastModifiedOn.text = teller.lastModifiedOn
        tvLastOpenedBy.text = teller.lastOpenedBy
        tvLastOpenedOn.text = teller.lastOpenedOn
    }
}