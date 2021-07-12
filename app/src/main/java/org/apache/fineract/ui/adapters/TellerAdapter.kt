package org.apache.fineract.ui.adapters


import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.item_teller.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.teller.Teller
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.ui.base.OnItemClickListener
import org.apache.fineract.utils.DateUtils
import org.apache.fineract.utils.StatusUtils
import javax.inject.Inject

class TellerAdapter @Inject constructor(@ApplicationContext val context: Context)
    : RecyclerView.Adapter<TellerAdapter.ViewHolder>() {

    var tellers: List<Teller> = ArrayList()
    lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_teller,
                parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = tellers.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val teller = tellers[position]

        holder.tellerIdentifier.text = teller.tellerAccountIdentifier

        val modifiedBy = context.getString(R.string.last_modified_by) + context.getString(
                R.string.colon) + teller.lastModifiedBy
        holder.tvModifiedBy.text = modifiedBy

        val lastModifiedOn = context.getString(R.string.last_modified_on) + context.getString(
                R.string.colon) +
                DateUtils.getDate(teller.lastModifiedOn,
                        DateUtils.INPUT_DATE_FORMAT, DateUtils.OUTPUT_DATE_FORMAT)

        StatusUtils.setTellerStatus(teller.state, holder.statusIndicator, context)

        holder.tvModifiedOn.text = lastModifiedOn
        holder.withDrawLimit.text = tellers[position].cashdrawLimit.toString()
    }

    fun setTellerList(tellers: List<Teller>) {
        this.tellers = tellers
        notifyDataSetChanged()
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val llTeller: LinearLayout = itemView.ll_teller
        val tellerIdentifier: TextView = itemView.tv_teller_identifier
        val tvModifiedBy: TextView = itemView.tv_modified_by
        val tvModifiedOn: TextView = itemView.tv_modified_on
        val withDrawLimit: TextView = itemView.tv_cashWithdraw_limit
        val statusIndicator: AppCompatImageView = itemView.iv_status_indicator

        init {
            llTeller.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, adapterPosition)
            }
        }
    }
}