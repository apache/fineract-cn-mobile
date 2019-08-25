package org.apache.fineract.ui.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_product.view.*
import org.apache.fineract.R
import org.apache.fineract.data.models.product.Product
import org.apache.fineract.injection.ApplicationContext
import org.apache.fineract.ui.base.OnItemClickListener
import org.apache.fineract.utils.DateUtils
import javax.inject.Inject

class ProductAdapter @Inject constructor(@ApplicationContext val context: Context)
    : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var products: List<Product> = ArrayList()

    lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val product = products[position]

        holder.tvProductIdentifier.text = product.identifier

        val modifiedBy = context.getString(R.string.last_modified_by) + context.getString(
                R.string.colon) + product.lastModifiedBy
        holder.tvModifiedBy.text = modifiedBy

        val lastModifiedOn = context.getString(R.string.last_modified_on) + context.getString(
                R.string.colon) +
                DateUtils.getDate(product.lastModifiedOn,
                        DateUtils.INPUT_DATE_FORMAT, DateUtils.OUTPUT_DATE_FORMAT)

        holder.tvModifiedOn.text = lastModifiedOn
        holder.tvName.text = product.name
    }

    fun setProductsList(products: List<Product>) {
        this.products = products
        notifyDataSetChanged()
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val tvProductIdentifier: TextView = itemView.tv_product_identifier
        val tvModifiedBy: TextView = itemView.tv_modified_by
        val tvModifiedOn: TextView = itemView.tv_modified_on
        val tvName: TextView = itemView.tv_name

        init {
            itemView.ll_loan_accounts.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(v, adapterPosition)
        }
    }
}