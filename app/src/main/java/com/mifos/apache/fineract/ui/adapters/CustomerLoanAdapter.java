package com.mifos.apache.fineract.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.loan.LoanAccount;
import com.mifos.apache.fineract.injection.ApplicationContext;
import com.mifos.apache.fineract.ui.base.OnItemClickListener;
import com.mifos.apache.fineract.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 10/07/17.
 */
public class CustomerLoanAdapter extends RecyclerView.Adapter<CustomerLoanAdapter.ViewHolder> {

    private Context context;
    private List<LoanAccount> customerLoanAccounts;
    public OnItemClickListener onItemClickListener;

    @Inject
    public CustomerLoanAdapter(@ApplicationContext Context context) {
        this.context = context;
        customerLoanAccounts = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer_loans, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        LoanAccount loanAccount = customerLoanAccounts.get(position);

        holder.tvLoanIdentifier.setText(loanAccount.getIdentifier());

        String modifiedOn = context.getString(R.string.last_modified_on) + context.getString(
                R.string.colon) + DateUtils.getDateTime(loanAccount.getLastModifiedOn());
        holder.tvModifiedOn.setText(modifiedOn);

        String modifiedBy = context.getString(R.string.last_modified_by) + context.getString(
                R.string.colon) + loanAccount.getLastModifiedBy();
        holder.tvModifiedBy.setText(modifiedBy);

        String loanCurrentStatus = context.getString(R.string.current_status) + context.getString(
                R.string.colon) + loanAccount.getCurrentState();
        holder.tvLoanCurrentStatus.setText(loanCurrentStatus);

    }

    @Override
    public int getItemCount() {
        return customerLoanAccounts.size();
    }

    public void setCustomerLoanAccounts(List<LoanAccount> customerLoanAccounts) {
        this.customerLoanAccounts = customerLoanAccounts;
        notifyDataSetChanged();
    }

    public void setMoreCustomerLoanAccounts(List<LoanAccount> moreCustomerLoanAccounts) {
        customerLoanAccounts.addAll(moreCustomerLoanAccounts);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        @BindView(R.id.tv_loan_identifier)
        TextView tvLoanIdentifier;

        @BindView(R.id.tv_loan_current_status)
        TextView tvLoanCurrentStatus;

        @BindView(R.id.tv_balance)
        TextView tvBalance;

        @BindView(R.id.tv_modified_by)
        TextView tvModifiedBy;

        @BindView(R.id.tv_modified_on)
        TextView tvModifiedOn;

        @BindView(R.id.cv_customer_loan)
        CardView cvCustomerLoan;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            cvCustomerLoan.setOnClickListener(this);
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