package org.apache.fineract.ui.adapters

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_ledger.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.Ledger
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.utils.DateUtils
import org.apache.fineract.utils.StatusUtils
import javax.inject.Inject

class LedgerListAdapter @Inject constructor(@ApplicationContext val context: Context)
    : RecyclerView.Adapter<LedgerListAdapter.ViewHolder>() {

    private var ledgers: List<Ledger> = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_ledger, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = ledgers.size

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ledger = ledgers[position]

        holder.tvLedgerIdentifier.text = ledger.identifier

        val modifiedBy = context.getString(R.string.last_modified_by) + context.getString(
                R.string.colon) + ledger.lastModifiedBy
        holder.tvModifiedBy.text = modifiedBy

        val lastModifiedOn = context.getString(R.string.last_modified_on) + context.getString(
                R.string.colon) +
                DateUtils.getDate(ledger.lastModifiedOn,
                        DateUtils.INPUT_DATE_FORMAT, DateUtils.OUTPUT_DATE_FORMAT)

        StatusUtils.setAccountType(ledger.type, holder?.ivAccountTypeIndicator, context)

        holder.tvModifiedOn.text = lastModifiedOn
        holder.tvTotalValue.text = ledger.totalValue.toString()
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }
    }

    fun setLedgerList(ledgers: List<Ledger>) {
        this.ledgers = ledgers
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvLedgerIdentifier: TextView = itemView.tv_ledger_identifier
        val tvModifiedBy: TextView = itemView.tv_modified_by
        val tvModifiedOn: TextView = itemView.tv_modified_on
        val tvTotalValue: TextView = itemView.tv_total_value
        val ivAccountTypeIndicator: AppCompatImageView = itemView.iv_type_indicator

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}