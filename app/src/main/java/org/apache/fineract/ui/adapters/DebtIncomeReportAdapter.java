package org.apache.fineract.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.models.loan.CreditWorthinessFactor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 30/07/17.
 */
public class DebtIncomeReportAdapter extends
        RecyclerView.Adapter<DebtIncomeReportAdapter.ViewHolder> {

    private List<CreditWorthinessFactor> creditWorthinessFactors;

    @Inject
    public DebtIncomeReportAdapter() {
        creditWorthinessFactors = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_debt_income_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CreditWorthinessFactor creditWorthinessFactor = creditWorthinessFactors.get(position);
        holder.tvAmount.setText(String.valueOf(creditWorthinessFactor.getAmount()));
        holder.tvDescription.setText(creditWorthinessFactor.getDescription());
    }

    @Override
    public int getItemCount() {
        return creditWorthinessFactors.size();
    }

    public void setCreditWorthinessFactors(List<CreditWorthinessFactor> creditWorthinessFactors) {
        this.creditWorthinessFactors = creditWorthinessFactors;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_amount)
        TextView tvAmount;

        @BindView(R.id.tv_description)
        TextView tvDescription;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}