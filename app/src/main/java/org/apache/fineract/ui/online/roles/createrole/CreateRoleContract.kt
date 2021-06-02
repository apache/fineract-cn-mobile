package org.apache.fineract.ui.online.roles.createrole

import org.apache.fineract.data.models.rolesandpermission.Role
import org.apache.fineract.ui.base.MvpView

interface CreateRoleContract {

    interface View : MvpView {

        fun roleCreatedSuccessfully()

        fun roleUpdatedSuccessfully()

        fun roleDeletedSuccessfully()

        fun showProgressbar()

        fun hideProgressbar()

        override fun showError(message: String)

        fun validateIdentifier(): Boolean
    }

    interface Presenter {

        fun createRole(role: Role?)

        fun updateRole(roleIdentifier: String?, role: Role?)

        fun deleteRole(identifier: String?)
    }
}