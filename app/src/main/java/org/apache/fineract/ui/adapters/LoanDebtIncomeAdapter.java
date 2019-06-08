package org.apache.fineract.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.models.loan.CreditWorthinessFactor;
import org.apache.fineract.ui.online.loanaccounts.loanapplication.CreditWorthinessSource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 23/07/17.
 */
public class LoanDebtIncomeAdapter extends RecyclerView.Adapter<LoanDebtIncomeAdapter.ViewHolder> {

    private List<CreditWorthinessFactor> creditWorthinessFactors;
    private OnClickEditDeleteListener onClickEditDeleteListener;
    private CreditWorthinessSource creditWorthinessSource;
    private boolean isReviewAdapter = false;

    @Inject
    public LoanDebtIncomeAdapter() {
        creditWorthinessFactors = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_loan_debt_income, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CreditWorthinessFactor creditWorthinessFactor = creditWorthinessFactors.get(position);
        holder.tvAmount.setText(String.valueOf(creditWorthinessFactor.getAmount()));
        holder.tvDescription.setText(creditWorthinessFactor.getDescription());
    }

    public void isReview(boolean isReviewAdapter) {
        this.isReviewAdapter = isReviewAdapter;
    }

    @Override
    public int getItemCount() {
        return creditWorthinessFactors.size();
    }

    public void setOnClickEditDeleteListener(OnClickEditDeleteListener itemClickListener) {
        this.onClickEditDeleteListener = itemClickListener;
    }

    public void setCreditWorthinessSource(CreditWorthinessSource creditWorthinessSource) {
        this.creditWorthinessSource = creditWorthinessSource;
    }

    public void setCreditWorthinessFactors(List<CreditWorthinessFactor> creditWorthinessFactors) {
        this.creditWorthinessFactors = creditWorthinessFactors;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_amount)
        TextView tvAmount;

        @BindView(R.id.tv_description)
        TextView tvDescription;

        @BindView(R.id.iv_edit)
        ImageView ivEdit;

        @BindView(R.id.iv_delete)
        ImageView ivDelete;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            if (isReviewAdapter) {
                ivEdit.setVisibility(View.GONE);
                ivDelete.setVisibility(View.GONE);
            }
        }

        @OnClick(R.id.iv_edit)
        void onEdit() {
            onClickEditDeleteListener.onClickEdit(creditWorthinessSource, getAdapterPosition());
        }

        @OnClick(R.id.iv_delete)
        void onDelete() {
            onClickEditDeleteListener.onClickDelete(creditWorthinessSource, getAdapterPosition());
        }
    }

    public interface OnClickEditDeleteListener {
        void onClickEdit(CreditWorthinessSource creditWorthinessSource, int position);
        void onClickDelete(CreditWorthinessSource creditWorthinessSource, int position);
    }
}