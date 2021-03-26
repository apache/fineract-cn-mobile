package org.apache.fineract.ui.online.groups.creategroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.wajahatkarim3.easyvalidation.core.rules.BaseRule
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.fragment_step_group_address.*
import org.apache.fineract.R
import org.apache.fineract.data.models.customer.Country
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.online.groups.GroupAction
import org.apache.fineract.ui.online.groups.grouplist.GroupViewModelFactory
import org.apache.fineract.ui.online.groups.grouplist.GroupViewModel
import org.apache.fineract.utils.Constants
import javax.inject.Inject


/*
 * Created by saksham on 03/July/2019
*/

class GroupAddressStepFragment : FineractBaseFragment(), Step {

    lateinit var rootView: View
    lateinit var countries: List<Country>
    lateinit var viewModel: GroupViewModel
    private lateinit var groupAction: GroupAction

    @Inject
    lateinit var groupViewModelFactory: GroupViewModelFactory

    companion object {
        fun newInstance(groupAction: GroupAction) = GroupAddressStepFragment().apply {
            arguments = Bundle().apply {
                putSerializable(Constants.GROUP_ACTION, groupAction)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getSerializable(Constants.GROUP_ACTION)?.let {
            groupAction = it as GroupAction
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_step_group_address, container, false)
        (activity as FineractBaseActivity).activityComponent.inject(this)
        viewModel = ViewModelProviders.of(this,
                groupViewModelFactory).get(GroupViewModel::class.java)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countries = viewModel.getCountries()
        if (countries.isNotEmpty()) {
            etCountry.setAdapter(ArrayAdapter(context, android.R.layout.simple_list_item_1,
                    viewModel.getCountryNames(countries)))
        }
        etCountry.threshold = 1

        if (groupAction == GroupAction.EDIT) {
            showDataOnViews()
        }
    }

    private fun showDataOnViews() {
        val group = (activity as CreateGroupActivity).getGroup()
        etStreet.setText(group.address?.street)
        etCity.setText(group.address?.city)
        etRegion.setText(group.address?.region)
        etPostalCode.setText(group.address?.postalCode)
        etCountry.setText(group.address?.country)
    }

    override fun onSelected() {

    }

    override fun verifyStep(): VerificationError? {
        if (!validateStreet() || !validateCity() || !validateRegion() || !validatePostalCode() || !validateCountry()) {
            return VerificationError("")
        }
        (activity as CreateGroupActivity).setGroupAddress(etStreet.text.toString(), etCity.text.toString(), etRegion.text.toString(),
                etPostalCode.text.toString(), etCountry.text.toString(), viewModel.getCountryCode(countries, etCountry.text.toString()))
        return null
    }

    override fun onError(error: VerificationError) {

    }

    private fun validateStreet(): Boolean {
        return etStreet.validator()
                .nonEmpty()
                .addErrorCallback {
                    etStreet.error = it
                }
                .check()
    }

    private fun validateCity(): Boolean {
        return etCity.validator()
                .nonEmpty()
                .addErrorCallback {
                    etCity.error = it
                }.check()
    }

    private fun validateRegion(): Boolean {
        return etRegion.validator()
                .nonEmpty()
                .addErrorCallback {
                    etRegion.error = it
                }.check()
    }

    private fun validatePostalCode(): Boolean {
        return etPostalCode.validator()
                .nonEmpty()
                .onlyNumbers()
                .addErrorCallback {
                    etPostalCode.error = it
                }.check()
    }

    private fun validateCountry(): Boolean {
        return etCountry.validator()
                .nonEmpty()
                .noNumbers()
                .addRule(object : BaseRule {
                    override fun validate(text: String): Boolean {
                        return viewModel.isCountryValid(countries, etCountry.text.toString())
                    }

                    override fun getErrorMessage(): String {
                        return if (countries.isEmpty()) {
                            getString(R.string.error_loading_countries)
                        } else getString(R.string.invalid_country)
                    }
                })
                .addErrorCallback {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }.check()
    }
}