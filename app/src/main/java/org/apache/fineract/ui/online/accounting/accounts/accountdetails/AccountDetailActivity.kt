package org.apache.fineract.ui.online.accounting.accounts.accountdetails

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_account_detail.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.Account
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.utils.ConstantKeys

class AccountDetailActivity : FineractBaseActivity() {

    lateinit var account: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_detail)

        account = intent.getParcelableExtra(ConstantKeys.ACCOUNT)
        setToolbarTitle(getString(R.string.account_details))
        showBackButton()
        populateUserInterface()
    }

    fun populateUserInterface() {
        tvType.text = account.type.toString()
        tvIdentifier.text = account.identifier
        tvName.text = account.name
        tvLedger.text = account.ledger
        tvState.text = account.state.toString()
        tvAlternativeAccountNumber.text = account.alternativeAccountNumber
        tvBalance.text = "$ ${account.balance}"
        tvReferenceAmount.text = "$ ${account.referenceAccount}"
        tvCreateOn.text = account.createdOn
        tvCreatedBy.text = account.createdBy
        tvLastModifiedBy.text = account.lastModifiedBy
        tvLastModifiedOn.text = account.lastModifiedOn

        tvHolders.text = "\n"
        if (account.holders?.size != 0) {
            var accountIterator = account.holders?.iterator()
            while (accountIterator!!.hasNext()) {
                tvHolders.append("${accountIterator.next()}\n")
            }
        } else {
            tvHolders.append("${getString(R.string.no_holder_found)}\n")
        }

        tvSignatureAuthorities.text = "\n"
        if (account.signatureAuthorities?.size != 0) {
            var signatureAuthoritiesIterator = account.signatureAuthorities?.iterator()
            while (signatureAuthoritiesIterator!!.hasNext()) {
                tvSignatureAuthorities.append("${signatureAuthoritiesIterator.next()}\n")
            }
        } else {
            tvSignatureAuthorities.append("${getString(R.string.no_signature_authorities_found)}\n")
        }

    }
}