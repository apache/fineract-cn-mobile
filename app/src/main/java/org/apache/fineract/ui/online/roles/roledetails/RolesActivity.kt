package org.apache.fineract.ui.online.roles.roledetails

import android.os.Bundle
import org.apache.fineract.R
import org.apache.fineract.data.models.rolesandpermission.Role
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.online.roles.createrole.CreateRoleFragment
import org.apache.fineract.ui.online.roles.createrole.RoleAction
import org.apache.fineract.utils.ConstantKeys

class RolesActivity : FineractBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_container)
        activityComponent.inject(this)

        val roleAction = intent?.getSerializableExtra(
                ConstantKeys.ROLE_ACTION) as RoleAction

        when (roleAction) {
            RoleAction.CREATE -> {
                replaceFragment(
                        CreateRoleFragment.newInstance(null, RoleAction.CREATE),
                        false, R.id.container
                )
            }
            RoleAction.VIEW -> {
                val role = intent?.getParcelableExtra<Role>(ConstantKeys.ROLE)
                role?.let {
                    replaceFragment(
                            RolesDetailsFragment.newInstance(role),
                            false, R.id.container
                    )
                }
            }
        }

        showBackButton()
    }
}
