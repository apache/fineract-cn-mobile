package com.mifos.apache.fineract.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.customer.Customer;
import com.mifos.apache.fineract.injection.ApplicationContext;
import com.mifos.apache.fineract.ui.base.OnItemClickListener;
import com.mifos.apache.fineract.ui.views.TextDrawable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 20/06/17.
 */
public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    private Context context;
    private List<Customer> customers;
    public OnItemClickListener onItemClickListener;

    @Inject
    public CustomerAdapter(@ApplicationContext Context context) {
        this.context = context;
        customers = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Customer customer = customers.get(position);
        holder.tvCustomerName.setText(customer.getGivenName() + " " + customer.getSurname());
        holder.tvCustomerStatus.setText(customer.getCurrentState());

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(customer.getGivenName().substring(0, 1), R.color.colorPrimary);
        holder.ivCustomerImage.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public void setCustomers(List<Customer> customerList) {
        customers.addAll(customerList);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        @BindView(R.id.iv_customer_picture)
        ImageView ivCustomerImage;

        @BindView(R.id.tv_customer_name)
        TextView tvCustomerName;

        @BindView(R.id.tv_customer_status)
        TextView tvCustomerStatus;

        @BindView(R.id.cv_customer)
        CardView cvCustomer;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            cvCustomer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            onItemClickListener.onItemLongPress(v, getAdapterPosition());
            return true;
        }
    }
}