package org.apache.fineract.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_group.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.Group
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.ui.base.OnItemClickListener
import org.apache.fineract.utils.DateUtils
import org.apache.fineract.utils.StatusUtils
import javax.inject.Inject


/*
 * Created by saksham on 16/June/2019
*/

class GroupsAdapter @Inject constructor(@ApplicationContext var context: Context?)
    : RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {

    var groups = ArrayList<Group>()
    lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group, parent, false))
    }

    override fun getItemCount(): Int = groups.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var group: Group = groups[position]

        group.status?.let {
            StatusUtils.setGroupsStatusIcon(group.status, holder.ivTypeIndicator, context)
        }

        holder.tvGroupIdentifier.text = group.identifier

        val modifiedBy = context?.getString(R.string.last_modified_by) + context?.getString(R.string.colon) + group.lastModifiedBy
        holder.tvLastModifiedBy.text = modifiedBy

        val lastModifiedOn = context?.getString(R.string.last_modified_on) + context?.getString(R.string.colon) + DateUtils.getDate(group.lastModifiedOn,
                DateUtils.INPUT_DATE_FORMAT, DateUtils.OUTPUT_DATE_FORMAT)
        holder.tvLastModifiedOn.text = lastModifiedOn
    }

    fun setGroupList(groups: ArrayList<Group>) {
        this.groups = groups
        notifyDataSetChanged()
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
        var llGroups: LinearLayout = itemView.ll_group
        var ivTypeIndicator: ImageView = itemView.iv_type_indicator
        var tvGroupIdentifier: TextView = itemView.tv_group_identifier
        var tvLastModifiedBy: TextView = itemView.tv_last_modified_by
        var tvLastModifiedOn: TextView = itemView.tv_last_modified_on

        init {
            llGroups.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, adapterPosition)
            }
        }
    }
}