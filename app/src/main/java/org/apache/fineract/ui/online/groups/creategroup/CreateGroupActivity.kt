package org.apache.fineract.ui.online.groups.creategroup

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.activity_create_group.*
import org.apache.fineract.R
import org.apache.fineract.data.Status
import org.apache.fineract.data.models.Group
import org.apache.fineract.data.models.customer.Address
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.Toaster
import org.apache.fineract.ui.online.groups.GroupAction
import org.apache.fineract.ui.online.groups.grouplist.GroupViewModel
import org.apache.fineract.ui.online.groups.grouplist.GroupViewModelFactory
import org.apache.fineract.utils.Constants
import org.apache.fineract.utils.DateUtils
import org.apache.fineract.utils.MaterialDialog
import javax.inject.Inject


/*
 * Created by saksham on 02/July/2019
*/

class CreateGroupActivity : FineractBaseActivity(), StepperLayout.StepperListener {

    private var group = Group()
    private var groupAction = GroupAction.CREATE

    private var isBackPressedOnce:Boolean = false

    @Inject
    lateinit var groupViewModelFactory: GroupViewModelFactory

    lateinit var viewModel: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        activityComponent.inject(this)
        groupAction = intent.getSerializableExtra(Constants.GROUP_ACTION) as GroupAction
        when (groupAction) {
            GroupAction.CREATE -> {
                setToolbarTitle(getString(R.string.create_group))
            }
            GroupAction.EDIT -> {
                setToolbarTitle(getString(R.string.edit_group))
                intent?.extras?.getParcelable<Group>(Constants.GROUP)?.let {
                    group = it
                }
            }
        }
        viewModel = ViewModelProviders.of(this, groupViewModelFactory).get(GroupViewModel::class.java)
        subscribeUI()
        showBackButton()
        slCreateGroup.adapter = CreateGroupAdapter(supportFragmentManager, this, groupAction)
        slCreateGroup.setOffscreenPageLimit(slCreateGroup.adapter.count)
        slCreateGroup.setListener(this)
    }

    override fun onStepSelected(newStepPosition: Int) {}

    override fun onBackPressed() {
        if (slCreateGroup.currentStepPosition !== 0) {
                    slCreateGroup.currentStepPosition = slCreateGroup.currentStepPosition - 1
        } else {
            MaterialDialog.Builder()
                    .init(this)
                    .setTitle(getString(R.string.dialog_title_confirm_exit))
                    .setMessage(getString(
                            R.string.dialog_message_confirmation_exit_create_edit_activity))
                    .setPositiveButton(getString(R.string.dialog_action_exit)
                    ) { dialog: DialogInterface?, which: Int -> super.onBackPressed() }
                    .setNegativeButton(getString(R.string.dialog_action_cancel))
                    .createMaterialDialog()
                    .show()
        }
    }

    override fun onError(verificationError: VerificationError?) {}

    override fun onReturn() {}

    private fun subscribeUI() {
        viewModel.status.observe(this, Observer { status ->
            when (status) {
                Status.LOADING ->
                    if (groupAction == GroupAction.CREATE) {
                        showMifosProgressDialog(getString(R.string.create_group))
                    } else {
                        showMifosProgressDialog(getString(R.string.updating_group_please_wait))
                    }
                Status.ERROR -> {
                    hideMifosProgressDialog()
                    if (groupAction == GroupAction.CREATE) {
                        Toaster.show(findViewById(android.R.id.content), R.string.error_while_creating_group, Toast.LENGTH_SHORT)
                    } else {
                        Toaster.show(findViewById(android.R.id.content), R.string.error_while_updating_group_status, Toast.LENGTH_SHORT)
                    }
                }
                Status.DONE -> {
                    hideMifosProgressDialog()
                    if (groupAction == GroupAction.CREATE) {
                        Toast.makeText(this, getString(R.string.group_identifier_updated_successfully, group.identifier), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, getString(R.string.group_identifier_updated_successfully, group.identifier), Toast.LENGTH_SHORT).show()
                    }
                    finish()
                }
            }
        })
    }

    override fun onCompleted(completeButton: View?) {
        when (groupAction) {
            GroupAction.EDIT -> group.identifier?.let {
                viewModel.updateGroup(it, group)
            }
            GroupAction.CREATE -> viewModel.createGroup(group)
        }
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
        val address = Address()
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