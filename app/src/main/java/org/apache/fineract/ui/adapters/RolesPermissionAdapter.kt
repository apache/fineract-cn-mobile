package org.apache.fineract.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.roles_details_item.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.rolesandpermission.AllowedOperation
import org.apache.fineract.data.models.rolesandpermission.Permission
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.ui.online.roles.createrole.RoleAction
import javax.inject.Inject

class RolesPermissionAdapter @Inject constructor(
        @ApplicationContext val context: Context)
    : RecyclerView.Adapter<RolesPermissionAdapter.ViewHolder>() {

    private var permissionList: ArrayList<Permission> = ArrayList()
    private var roleAction: RoleAction = RoleAction.CREATE
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return permissionList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(permissionList[position],
                context,
                roleAction,
                onItemClickListener)
    }

    fun submitList(permissionList: List<Permission>) {
        this.permissionList.clear()
        this.permissionList.addAll(permissionList)
        notifyDataSetChanged()
    }

    fun setRoleAction(roleAction: RoleAction) {
        this.roleAction = roleAction
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    class ViewHolder constructor(private val viewItem: View)
        : RecyclerView.ViewHolder(viewItem) {

        fun bind(permission: Permission,
                 context: Context,
                 roleAction: RoleAction,
                 onItemClickListener: OnItemClickListener?) {
            setItemActionsVisibility(viewItem, roleAction)
            viewItem.tv_permittable_end_group_id.text =
                    permission.permittableEndpointGroupIdentifier
            viewItem.cg_allowed_operation.removeAllViews()
            permission.allowedOperations?.let {
                for (operation in it) {
                    when (operation) {
                        AllowedOperation.CHANGE -> {
                            createChip(context, context.getString(R.string.change))
                        }
                        AllowedOperation.DELETE -> {
                            createChip(context, context.getString(R.string.delete))
                        }
                        AllowedOperation.READ -> {
                            createChip(context, context.getString(R.string.read))
                        }
                    }
                }
            }
            viewItem.iv_delete.setOnClickListener {
                onItemClickListener?.onDeleteClick(adapterPosition)
            }
            viewItem.iv_edit.setOnClickListener {
                onItemClickListener?.onEditClick(adapterPosition)
            }
        }

        private fun createChip(context: Context, text: String) {
            val chip = Chip(viewItem.cg_allowed_operation.context)
            chip.text = text
            chip.setChipBackgroundColorResource(R.color.colorPrimary)
            chip.setTextColor(Color.WHITE)
            chip.setChipIconTintResource(R.color.white)
            chip.chipIcon = ContextCompat.getDrawable(
                    context, R.drawable.ic_done_black_24dp)
            viewItem.cg_allowed_operation.addView(chip)
        }

        private fun setItemActionsVisibility(viewItem: View,
                                             roleAction: RoleAction) {
            when (roleAction) {
                RoleAction.CREATE, RoleAction.EDIT -> {
                    viewItem.iv_edit.visibility = View.VISIBLE
                    viewItem.iv_delete.visibility = View.VISIBLE
                }
                RoleAction.VIEW -> {
                    viewItem.iv_edit.visibility = View.GONE
                    viewItem.iv_delete.visibility = View.GONE
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                return ViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.roles_details_item, parent, false))
            }
        }
    }
}

interface OnItemClickListener {
    fun onDeleteClick(position: Int)
    fun onEditClick(position: Int)
}