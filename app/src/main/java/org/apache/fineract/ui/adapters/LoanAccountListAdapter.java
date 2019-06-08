package org.apache.fineract.ui.adapters;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.models.loan.LoanAccount;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.ui.base.OnItemClickListener;
import org.apache.fineract.utils.DateUtils;
import org.apache.fineract.utils.StatusUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 10/07/17.
 */
public class LoanAccountListAdapter extends
        RecyclerView.Adapter<LoanAccountListAdapter.ViewHolder> {

    private Context context;
    private List<LoanAccount> customerLoanAccounts;
    public OnItemClickListener onItemClickListener;

    @Inject
    public LoanAccountListAdapter(@ApplicationContext Context context) {
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
                R.string.colon) + DateUtils.getDate(loanAccount.getCreatedOn(),
                DateUtils.INPUT_DATE_FORMAT, DateUtils.OUTPUT_DATE_FORMAT);
        holder.tvModifiedOn.setText(modifiedOn);

        String modifiedBy = context.getString(R.string.last_modified_by) + context.getString(
                R.string.colon) + loanAccount.getLastModifiedBy();
        holder.tvModifiedBy.setText(modifiedBy);

        StatusUtils.setLoanAccountStatus(loanAccount.getCurrentState(),
                holder.ivStatusIndicator, context);

        holder.tvAccountBalance.setText(String.valueOf(
                loanAccount.getLoanParameters().getMaximumBalance()));
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

        @BindView(R.id.iv_status_indicator)
        AppCompatImageView ivStatusIndicator;

        @BindView(R.id.tv_account_balance)
        TextView tvAccountBalance;

        @BindView(R.id.tv_modified_by)
        TextView tvModifiedBy;

        @BindView(R.id.tv_modified_on)
        TextView tvModifiedOn;

        @BindView(R.id.ll_loan_accounts)
        LinearLayout llLoanAccount;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            llLoanAccount.setOnClickListener(this);
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