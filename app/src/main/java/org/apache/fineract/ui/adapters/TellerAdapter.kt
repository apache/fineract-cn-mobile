package org.apache.fineract.ui.adapters


import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_teller.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.teller.Teller
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.utils.DateUtils
import org.apache.fineract.utils.StatusUtils
import javax.inject.Inject

class TellerAdapter @Inject constructor(@ApplicationContext val context: Context)
    : RecyclerView.Adapter<TellerAdapter.ViewHolder>() {

    var tellers: List<Teller> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tellerIdentifier: TextView = itemView.tv_teller_identifier
        val tvModifiedBy: TextView = itemView.tv_modified_by
        val tvModifiedOn: TextView = itemView.tv_modified_on
        val withDrawLimit: TextView = itemView.tv_cashWithdraw_limit
        val statusIndicator: AppCompatImageView = itemView.iv_status_indicator
    }
}