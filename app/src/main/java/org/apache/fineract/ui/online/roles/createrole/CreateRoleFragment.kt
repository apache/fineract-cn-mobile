package org.apache.fineract.ui.online.roles.createrole

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_create_role.view.*
import kotlinx.android.synthetic.main.fragment_roles_details.view.rv_permission
import kotlinx.android.synthetic.main.layout_exception_handler.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.rolesandpermission.Permission
import org.apache.fineract.data.models.rolesandpermission.Role
import org.apache.fineract.ui.adapters.OnItemClickListener
import org.apache.fineract.ui.adapters.RolesPermissionAdapter
import org.apache.fineract.ui.base.FineractBaseActivity
import org.apache.fineract.ui.base.FineractBaseFragment
import org.apache.fineract.ui.base.Toaster
import org.apache.fineract.ui.online.roles.AddPermissionBottomSheet
import org.apache.fineract.ui.online.roles.OnAddPermissionBottomSheetListener
import org.apache.fineract.utils.ConstantKeys
import org.apache.fineract.utils.Utils
import org.apache.fineract.utils.ValidateIdentifierUtil
import javax.inject.Inject

class CreateRoleFragment : FineractBaseFragment(),
        OnItemClickListener, CreateRoleContract.View {

    private lateinit var rootView: View
    private var role: Role? = null
    private lateinit var roleAction: RoleAction
    private var permissions: ArrayList<Permission> = ArrayList()

    @Inject
    lateinit var permissionAdapter: RolesPermissionAdapter

    @Inject
    lateinit var createRolePresenter: CreateRolePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            role = it.getParcelable(ConstantKeys.ROLE)
            roleAction = it.getSerializable(ConstantKeys.ROLE_ACTION) as RoleAction
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_create_role, container, false)
        (activity as FineractBaseActivity?)?.activityComponent?.inject(this)
        createRolePresenter.attachView(this)
        initializeFineractUIErrorHandler(context, rootView)

        rootView.rv_permission.adapter = permissionAdapter
        rootView.rv_permission.addItemDecoration(DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL))
        permissionAdapter.setOnItemClickListener(this)

        when (roleAction) {
            RoleAction.CREATE -> {
                (activity as FineractBaseActivity).setToolbarTitle(
                        getString(R.string.create_role))
                showRecyclerView(false)
                role = Role()
            }
            RoleAction.EDIT -> {
                (activity as FineractBaseActivity).setToolbarTitle(
                        getString(R.string.edit_role))
                showRecyclerView(true)
                showDataOnViews()
            }
        }

        rootView.iv_add_permission.setOnClickListener {
            showBottomSheetDialog(RoleAction.CREATE, null, 0)
        }

        rootView.btn_try_again.setOnClickListener {
            rootView.layout_error.visibility = View.GONE
        }

        return rootView
    }

    private fun showBottomSheetDialog(roleAction: RoleAction,
                                      permission: Permission?,
                                      position: Int = 0) {
        val dialog = AddPermissionBottomSheet(
                permission,
                roleAction,
                OnAddPermissionBottomSheetListener { action, permission, position ->
                    if (action == RoleAction.CREATE) {
                        permissions.add(permission)
                        showRecyclerView(true)
                        permissionAdapter.submitList(permissions)
                    } else {
                        permissions[position] = permission
                        showRecyclerView(true)
                        permissionAdapter.submitList(permissions)
                    }
                }, position)
        dialog.show(childFragmentManager, getString(R.string.create_role))
    }

    private fun showDataOnViews() {
        rootView.et_identifier.setText(role?.identifier)
        permissionAdapter.setRoleAction(roleAction)
        role?.permissions?.let {
            permissions.addAll(it)
            permissionAdapter.submitList(permissions)
        }
    }

    private fun showRecyclerView(isShow: Boolean) {
        if (isShow) {
            rootView.rv_permission.visibility = View.VISIBLE
            rootView.tv_empty_message.visibility = View.GONE
            rootView.rv_permission.invalidate()
        } else {
            rootView.rv_permission.visibility = View.GONE
            rootView.tv_empty_message.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_create_role, menu)
        Utils.setToolbarIconColor(activity, menu, R.color.white)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (roleAction == RoleAction.CREATE) {
            menu.findItem(R.id.menu_delete_role).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save_role -> {
                if (validateIdentifier() && roleAction == RoleAction.CREATE) {
                    role?.identifier = rootView.et_identifier.text.toString()
                    role?.permissions = permissions
                    createRolePresenter.createRole(role)

                } else if (validateIdentifier() && roleAction == RoleAction.EDIT) {
                    role?.identifier = rootView.et_identifier.text.toString()
                    role?.permissions = permissions
                    createRolePresenter.updateRole(role?.identifier, role)
                }

            }
            R.id.menu_delete_role -> {
                context?.let {
                    AlertDialog.Builder(it, R.style.AlertDialogTheme).apply {
                        setTitle(getString(R.string.are_you_sure))
                        setPositiveButton(getString(R.string.delete)) { _, _ ->
                            createRolePresenter.deleteRole(role?.identifier)
                        }
                        setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }

                    }.show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDeleteClick(position: Int) {
        context?.let {
            AlertDialog.Builder(it, R.style.AlertDialogTheme).apply {
                setTitle(getString(R.string.are_you_sure))
                setPositiveButton(getString(R.string.delete)) { _, _ ->
                    permissions.removeAt(position)
                    permissionAdapter.submitList(permissions)
                    if (permissionAdapter.itemCount == 0) {
                        showRecyclerView(false)
                    }
                }
                setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }

            }.show()
        }
    }


    override fun validateIdentifier(): Boolean {
        return ValidateIdentifierUtil.isValid(activity,
                rootView.et_identifier.text.toString().trim { it <= ' ' },
                rootView.til_identifier)
    }

    override fun roleCreatedSuccessfully() {
        Toaster.show(rootView, R.string.role_created_successfuly)
        FragmentManager.POP_BACK_STACK_INCLUSIVE
    }

    override fun roleUpdatedSuccessfully() {
        Toaster.show(rootView, R.string.role_updated_successfuly)
        FragmentManager.POP_BACK_STACK_INCLUSIVE
    }

    override fun roleDeletedSuccessfully() {
        Toaster.show(rootView, R.string.role_delete_successfuly)
        FragmentManager.POP_BACK_STACK_INCLUSIVE
    }

    override fun showProgressbar() {
        showMifosProgressBar()
    }

    override fun hideProgressbar() {
        hideMifosProgressBar()
    }

    override fun showError(message: String) {
        rootView.layout_error.visibility = View.VISIBLE
        showFineractErrorUI(message)
    }

    override fun showNoInternetConnection() {
        showNoInternetConnection()
    }

    override fun onEditClick(position: Int) {
        showBottomSheetDialog(RoleAction.EDIT, permissions[position], position)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        createRolePresenter.detachView()
    }

    companion object {
        @JvmStatic
        fun newInstance(role: Role?, roleAction: RoleAction) =
                CreateRoleFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ConstantKeys.ROLE, role)
                        putSerializable(ConstantKeys.ROLE_ACTION, roleAction)
                    }
                }
    }
}
