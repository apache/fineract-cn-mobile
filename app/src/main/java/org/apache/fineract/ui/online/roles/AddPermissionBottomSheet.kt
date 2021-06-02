package org.apache.fineract.ui.online.roles

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_add_permission.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.rolesandpermission.AllowedOperation
import org.apache.fineract.data.models.rolesandpermission.Permission
import org.apache.fineract.ui.online.roles.createrole.RoleAction
import org.apache.fineract.utils.ValidateIdentifierUtil

class AddPermissionBottomSheet(
        private val permission: Permission?,
        private val roleAction: RoleAction,
        private val listener: OnAddPermissionBottomSheetListener,
        private val position: Int
) : BottomSheetDialogFragment() {

    private lateinit var rootView: View
    private lateinit var behavior: BottomSheetBehavior<View>
    private var operations: ArrayList<AllowedOperation> = ArrayList()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =
                super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        rootView = View.inflate(context,
                R.layout.bottom_sheet_add_permission, null)
        dialog.setContentView(rootView)
        behavior = BottomSheetBehavior.from(rootView.parent as View)

        when (roleAction) {
            RoleAction.EDIT -> {
                rootView.tv_header.text = getString(R.string.edit_permission)
                rootView.btn_add_permission.text = getString(R.string.update_permission)
                permission?.allowedOperations?.let {
                    operations = it as ArrayList<AllowedOperation>
                }
                showDataOnViews()
            }
            RoleAction.CREATE -> {
            }
        }

        subscribeUI()
        return dialog
    }

    private fun showDataOnViews() {
        rootView.et_permittable_end_group_id.setText(
                permission?.permittableEndpointGroupIdentifier)

        for (operation in operations!!) {
            when (operation) {
                AllowedOperation.READ -> {
                    rootView.cp_read.isChecked = true
                }
                AllowedOperation.DELETE -> {
                    rootView.cp_delete.isChecked = true
                }
                AllowedOperation.CHANGE -> {
                    rootView.cp_change.isChecked = true
                }
            }
        }
    }

    private fun subscribeUI() {
        rootView.cp_change.setOnCheckedChangeListener { chip, b ->
            addOrDeleteOperation(AllowedOperation.CHANGE, b, chip)
        }
        rootView.cp_delete.setOnCheckedChangeListener { chip, b ->
            addOrDeleteOperation(AllowedOperation.DELETE, b, chip)
        }
        rootView.cp_read.setOnCheckedChangeListener { chip, b ->
            addOrDeleteOperation(AllowedOperation.READ, b, chip)
        }
        rootView.btn_add_permission.setOnClickListener {
            if (validatePermittableEndGroupIdentifier()) {
                val permission = Permission().apply {
                    permittableEndpointGroupIdentifier =
                            rootView.et_permittable_end_group_id.text.toString()
                    allowedOperations = operations
                }
                listener.onAddButtonClick(roleAction, permission, position)
                dismiss()
            }
        }
        rootView.btn_cancel.setOnClickListener {
            dismiss()
        }
    }

    private fun validatePermittableEndGroupIdentifier(): Boolean {
        return ValidateIdentifierUtil.isValid(activity,
                rootView.et_permittable_end_group_id.text.toString().trim { it <= ' ' },
                rootView.til_permittable_end_group_id)
    }

    private fun addOrDeleteOperation(
            operation: AllowedOperation, b: Boolean, chip: CompoundButton) {
        if (b) {
            chip.setTextColor(ContextCompat.getColor(context!!, R.color.white))
            operations.add(operation)
        } else {
            operations.remove(operation)
            chip.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        }
    }

    override fun onStart() {
        super.onStart()
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}

class OnAddPermissionBottomSheetListener(
        val listener: (action: RoleAction,
                       permission: Permission,
                       position: Int) -> Unit) {
    fun onAddButtonClick(action: RoleAction,
                         permission: Permission,
                         position: Int) =
            listener(action, permission, position)

}