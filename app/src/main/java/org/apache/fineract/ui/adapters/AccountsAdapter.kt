package org.apache.fineract.ui.adapters

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_account.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.accounts.Account
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.utils.DateUtils
import org.apache.fineract.utils.StatusUtils
import javax.inject.Inject

class AccountsAdapter @Inject constructor(@ApplicationContext val context: Context)
    : RecyclerView.Adapter<AccountsAdapter.ViewHolder>() {

    var accounts: List<Account> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_account, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = accounts.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val account = accounts[position]

        holder.tvIdentifier.text = account.identifier

        val modifiedBy = context.getString(R.string.last_modified_by) + context.getString(
                R.string.colon) + account.lastModifiedBy
        holder.tvModifiedBy.text = modifiedBy

        val lastModifiedOn = context.getString(R.string.last_modified_on) + context.getString(
                R.string.colon) +
                DateUtils.getDate(account.lastModifiedOn,
                        DateUtils.INPUT_DATE_FORMAT, DateUtils.OUTPUT_DATE_FORMAT)

        StatusUtils.setAccountType(account.type, holder.ivAccountTypeIndicator, context)

        holder.tvModifiedOn.text = lastModifiedOn
        holder.tvName.text = account.name
    }

    fun setAccountsList(accounts: List<Account>) {
        this.accounts = accounts
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvIdentifier: TextView = itemView.tv_account_identifier
        val tvModifiedBy: TextView = itemView.tv_modified_by
        val tvModifiedOn: TextView = itemView.tv_modified_on
        val tvName: TextView = itemView.tv_name
        val ivAccountTypeIndicator: AppCompatImageView = itemView.iv_type_indicator
    }
}