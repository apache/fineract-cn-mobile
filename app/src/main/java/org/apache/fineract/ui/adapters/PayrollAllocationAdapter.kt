package org.apache.fineract.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_payroll_allocation.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.payroll.PayrollAllocation
import javax.inject.Inject

class PayrollAllocationAdapter @Inject constructor()
    : RecyclerView.Adapter<PayrollAllocationAdapter.ViewHolder>() {

    lateinit var payrollAllocation: List<PayrollAllocation>
    lateinit var onClickListener: OnClickEditDeleteListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_payroll_allocation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (accountNumber, amount, proportional) = payrollAllocation[position]
        holder.tvAccount.text = accountNumber
        holder.tvAmount.text = amount.toString()
        holder.cbProportional.isChecked = proportional
    }

    override fun getItemCount(): Int {
        return payrollAllocation.size
    }

    fun setPayrollAllocations(allocation: List<PayrollAllocation>) {
        this.payrollAllocation = allocation
        notifyDataSetChanged()
    }

    fun setOnClickEditDeleteListener(itemClickListener: OnClickEditDeleteListener) {
        this.onClickListener = itemClickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivEdit: ImageView = itemView.iv_edit
        val ivDelete: ImageView = itemView.iv_delete
        val tvAccount: TextView = itemView.tv_account
        val tvAmount: TextView = itemView.tv_amount
        val cbProportional: CheckBox = itemView.cb_Proportional

        init {
            ivEdit.setOnClickListener {
                onClickListener.onClickEdit(payrollAllocation[adapterPosition], adapterPosition)
            }

            ivDelete.setOnClickListener {
                onClickListener.onClickDelete(payrollAllocation[adapterPosition], adapterPosition)
            }
        }
    }

    interface OnClickEditDeleteListener {
        fun onClickEdit(payrollAllocation: PayrollAllocation, position: Int)
        fun onClickDelete(payrollAllocation: PayrollAllocation, position: Int)
    }
}