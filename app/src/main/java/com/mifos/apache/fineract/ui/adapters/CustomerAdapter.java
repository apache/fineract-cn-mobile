package com.mifos.apache.fineract.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.customer.Customer;
import com.mifos.apache.fineract.injection.ApplicationContext;

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
        holder.tvCustomerName.setText(customer.getGivenName());
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public void setCustomers(List<Customer> customerList) {
        customers.addAll(customerList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_customer_image)
        ImageView ivCustomerImage;

        @BindView(R.id.iv_customer_status)
        ImageView ivCustomerStatus;

        @BindView(R.id.tv_customer_name)
        TextView tvCustomerName;

        @BindView(R.id.tv_account_number)
        TextView tvAccountNumber;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}