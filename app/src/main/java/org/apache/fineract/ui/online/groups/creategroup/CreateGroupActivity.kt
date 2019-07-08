package org.apache.fineract.ui.online.groups.creategroup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.activity_create_group.*
import org.apache.fineract.R
import org.apache.fineract.data.models.Group
import org.apache.fineract.data.models.customer.Address
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.online.groups.grouplist.GroupViewModelFactory
import org.apache.fineract.ui.online.groups.grouplist.GroupsViewModel
import org.apache.fineract.utils.DateUtils
import javax.inject.Inject

/*
 * Created by saksham on 02/July/2019
*/

class CreateGroupActivity : FineractBaseActivity(), StepperLayout.StepperListener {

    private var group = Group()

    @Inject
    lateinit var groupViewModelFactory: GroupViewModelFactory

    lateinit var viewModel: GroupsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        setToolbarTitle(getString(R.string.create_group))

        activityComponent.inject(this)
        viewModel = ViewModelProviders.of(this, groupViewModelFactory).get(GroupsViewModel::class.java)

        showBackButton()
        slCreateGroup.adapter = CreateGroupAdapter(supportFragmentManager, this)
        slCreateGroup.setOffscreenPageLimit(slCreateGroup.adapter.count)
        slCreateGroup.setListener(this)
    }

    override fun onStepSelected(newStepPosition: Int) {

    }

    override fun onError(verificationError: VerificationError?) {
    }

    override fun onReturn() {
    }

    override fun onCompleted(completeButton: View?) {
        showMifosProgressDialog(getString(R.string.create_group))
        viewModel.createGroup().observe(this, Observer {
            hideMifosProgressDialog()
            Toast.makeText(this, getString(R.string.group_identifier_created_successfully, group.identifier), Toast.LENGTH_SHORT).show()
            finish()
        })
    }

    fun setGroupDetails(identifier: String, groupDefinitionIdentifier: String, name: String, office: String, assignedEmployee: String) {
        group.identifier = identifier
        group.groupDefinitionIdentifier = groupDefinitionIdentifier
        group.name = name
        group.office = office
        group.assignedEmployee = assignedEmployee
        group.weekday = DateUtils.getWeekDayIndex(DateUtils.getPresentDay())
    }

    fun setMembers(members: List<String>) {
        group.members = members
    }

    fun setLeaders(leaders: List<String>) {
        group.leaders = leaders
    }

    fun setGroupAddress(street: String, city: String, region: String, postalCode: String, country: String, countryCode: String?) {
        var address = Address()
        address.street = street
        address.city = city
        address.region = region
        address.postalCode = postalCode
        address.country = country
        address.countryCode = countryCode
        group.address = address
    }

    fun getGroup(): Group {
        return group
    }


}