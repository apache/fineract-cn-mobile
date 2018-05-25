package org.apache.fineract.ui.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.ui.base.OnItemClickListener;
import org.apache.fineract.utils.ImageLoaderUtils;
import org.apache.fineract.utils.StatusUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

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
        holder.tvCustomerName.setText(context.getString(R.string.customer_name,
                customer.getGivenName(), customer.getSurname() ));

        if (customer.getAssignedEmployee() != null) {
            holder.tvAssignedEmployee.setText(context.getString(R.string.assigned_employee_list,
                    customer.getAssignedEmployee()));
        } else {
            holder.tvAssignedEmployee.setText(context.getString(R.string.assigned_employee_list,
                    context.getString(R.string.not_assigned)));
        }

        ImageLoaderUtils imageLoaderUtils = new ImageLoaderUtils(context);
        imageLoaderUtils.loadImage(imageLoaderUtils.buildCustomerPortraitImageUrl(
                customer.getIdentifier()), holder.ivCustomerImage,
                R.drawable.ic_account_circle_black_24dp);

        if (customer.getCurrentState() != null) {
            StatusUtils.setCustomerStatus(customer.getCurrentState(), holder.ivStausIndicator,
                    context);
        } else {
            holder.ivStausIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public void setCustomers(List<Customer> customerList) {
        customers = customerList;
        notifyDataSetChanged();
    }

    public void setMoreCustomers(List<Customer> customerList) {
        customers.addAll(customerList);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        @BindView(R.id.iv_customer_picture)
        CircleImageView ivCustomerImage;

        @BindView(R.id.tv_customer_name)
        TextView tvCustomerName;

        @BindView(R.id.tv_assigned_employee)
        TextView tvAssignedEmployee;

        @BindView(R.id.ll_customer)
        LinearLayout llCustomer;

        @BindView(R.id.iv_status_indicator)
        AppCompatImageView ivStausIndicator;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            llCustomer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemLongPress(v, getAdapterPosition());
            }
            return true;
        }
    }
}