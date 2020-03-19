package org.apache.fineract.ui.online.roles.roledetails

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_roles_details.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.rolesandpermission.Role
import org.apache.fineract.ui.adapters.RolesPermissionAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.online.roles.createrole.CreateRoleFragment
import org.apache.fineract.ui.online.roles.createrole.RoleAction
import org.apache.fineract.utils.ConstantKeys
import org.apache.fineract.utils.Utils
import javax.inject.Inject


class RolesDetailsFragment : FineractBaseFragment() {

    private var roleItem: Role? = null
    private lateinit var rootView: View

    @Inject
    lateinit var rolesPermissionAdapter: RolesPermissionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            roleItem = it.getParcelable(ConstantKeys.ROLE)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_roles_details, container, false)
        (activity as FineractBaseActivity?)?.activityComponent?.inject(this)

        rootView.rv_permission.adapter = rolesPermissionAdapter
        rootView.rv_permission.addItemDecoration(DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL))

        showUserInterFace()
        return rootView
    }

    private fun showUserInterFace() {
        rootView.tv_identifier.text = roleItem?.identifier
        val permissionList = roleItem?.permissions
        permissionList?.let {
            rolesPermissionAdapter.setRoleAction(RoleAction.VIEW)
            rolesPermissionAdapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_roles_details, menu)
        Utils.setToolbarIconColor(activity, menu, R.color.white)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit_role -> {
                (activity as FineractBaseActivity).replaceFragment(
                        CreateRoleFragment.newInstance(roleItem, RoleAction.EDIT),
                        false, R.id.container
                )
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle(roleItem?.identifier.toString())
    }

    companion object {
        @JvmStatic
        fun newInstance(role: Role) =
                RolesDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ConstantKeys.ROLE, role)
                    }
                }
    }
}
