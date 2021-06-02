package org.apache.fineract.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.sub_ledger_item.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.ui.online.accounting.ledgers.LedgerAction
import javax.inject.Inject

class SubLedgerAdapter @Inject constructor(@ApplicationContext context: Context)
    : RecyclerView.Adapter<SubLedgerAdapter.ViewHolder>() {

    private var subLedgerList: List<Ledger> = emptyList()
    private var onItemClickListener: OnItemClickListener? = null
    private var isToReview = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int = subLedgerList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subLedgerList[position], isToReview, onItemClickListener)
    }

    fun submitList(list: ArrayList<Ledger>) {
        subLedgerList = list
        notifyDataSetChanged()
    }

    fun isToReview(isReview: Boolean) {
        isToReview = isReview
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    class ViewHolder private constructor(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        fun bind(ledger: Ledger?,
                 isToReview: Boolean,
                 onItemClickListener: OnItemClickListener?) {
            if (isToReview) {
                itemView.iv_edit.visibility = View.GONE
                itemView.iv_delete.visibility = View.GONE
            } else {
                itemView.iv_edit.setOnClickListener {
                    onItemClickListener?.onEditClick(adapterPosition)
                }
                itemView.iv_delete.setOnClickListener {
                    onItemClickListener?.onDeleteClick(adapterPosition)
                }
            }
            itemView.tv_name.text = ledger?.name
            itemView.tv_identifier.text = ledger?.identifier
            itemView.tv_type.text = ledger?.type.toString()
            itemView.tv_description.text = ledger?.description
            itemView.cb_show_acc_in_chart.isChecked = ledger?.showAccountsInChart == true
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                return ViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.sub_ledger_item, parent, false))
            }
        }
    }

    interface OnItemClickListener {
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
    }
}