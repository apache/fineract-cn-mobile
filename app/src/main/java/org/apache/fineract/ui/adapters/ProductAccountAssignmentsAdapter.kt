package org.apache.fineract.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.account_assignments_item.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.loan.AccountAssignment
import javax.inject.Inject

class ProductAccountAssignmentsAdapter @Inject constructor() :
    RecyclerView.Adapter<ProductAccountAssignmentsAdapter.ViewHolder>() {

    private var accountAssignmentsList = ArrayList<AccountAssignment>()
    private var onItemClickListener: OnItemClickListener? = null
    private var isReview = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int = accountAssignmentsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(accountAssignmentsList[position], onItemClickListener, isReview)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    fun submitList(list: ArrayList<AccountAssignment>) {
        accountAssignmentsList = list
        notifyDataSetChanged()
    }

    fun setReview(isReview: Boolean) {
        this.isReview = isReview
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(accountAssignment: AccountAssignment, onItemClickListener: OnItemClickListener?, isReview: Boolean) {
            if (isReview) {
                itemView.ivEditProductAccountAss.visibility = View.GONE
                itemView.ivDeleteProductAccountAss.visibility = View.GONE
            } else {
                itemView.ivEditProductAccountAss.visibility = View.VISIBLE
                itemView.ivDeleteProductAccountAss.visibility = View.VISIBLE
            }
            itemView.tvDesignator.text = accountAssignment.designator
            itemView.tvAccountIdentifier.text = accountAssignment.accountIdentifier
            itemView.tvLedgerIdentifier.text = accountAssignment.ledgerIdentifier
            itemView.ivDeleteProductAccountAss.setOnClickListener {
                onItemClickListener?.onDeleteClicked(adapterPosition)
            }
            itemView.ivEditProductAccountAss.setOnClickListener {
                onItemClickListener?.onEditClicked(adapterPosition)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                return ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.account_assignments_item, parent, false)
                )
            }
        }
    }

    interface OnItemClickListener {
        fun onEditClicked(position: Int)
        fun onDeleteClicked(position: Int)
    }
}
