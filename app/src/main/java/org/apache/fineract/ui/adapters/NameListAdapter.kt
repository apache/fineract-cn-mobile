package org.apache.fineract.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_name_list.view.*
import org.apache.fineract.R
import javax.inject.Inject

class NameListAdapter @Inject constructor()
    : RecyclerView.Adapter<NameListAdapter.ViewHolder>() {

    private var nameList = ArrayList<String>()
    private var onItemClickListener: OnItemClickListener? = null
    private var isReview = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int = nameList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(nameList[position], onItemClickListener, isReview)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    fun submitList(list: ArrayList<String>) {
        nameList = list
        notifyDataSetChanged()
    }

    fun setReview(isReview: Boolean) {
        this.isReview = isReview
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(name: String, onItemClickListener: OnItemClickListener?, isReview: Boolean) {
            if (isReview) {
                itemView.iv_edit.visibility = View.GONE
                itemView.iv_delete.visibility = View.GONE
            } else {
                itemView.iv_edit.visibility = View.VISIBLE
                itemView.iv_delete.visibility = View.VISIBLE
            }
            itemView.tv_name.text = name
            itemView.iv_delete.setOnClickListener {
                onItemClickListener?.onDeleteClicked(adapterPosition)
            }
            itemView.iv_edit.setOnClickListener {
                onItemClickListener?.onEditClicked(adapterPosition)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                return ViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_name_list, parent, false))
            }
        }
    }

    interface OnItemClickListener {
        fun onEditClicked(position: Int)
        fun onDeleteClicked(position: Int)
    }
}