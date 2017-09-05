package org.apache.fineract.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.ui.views.TextDrawable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 15/08/17.
 */
public class BeneficiaryAdapter extends RecyclerView.Adapter<BeneficiaryAdapter.ViewHolder> {

    private Context context;
    private List<String> beneficiary;
    private boolean deleteImageStatus;

    @Inject
    public BeneficiaryAdapter(@ApplicationContext Context context) {
        this.context = context;
        beneficiary = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_beneficiary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String identifier = beneficiary.get(position);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(identifier.substring(0, 1), R.color.white);
        holder.ivCustomerImage.setImageDrawable(drawable);

        holder.tvIdentifier.setText(identifier);

        if (deleteImageStatus) {
            holder.ivRemoveIdentifier.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return beneficiary.size();
    }

    public void setBeneficiary(String beneficiary) {
        if (!this.beneficiary.contains(beneficiary)) {
            this.beneficiary.add(beneficiary);
            notifyDataSetChanged();
        }
    }

    public void setAllBeneficiary(List<String> beneficiary) {
        this.beneficiary.clear();
        this.beneficiary.addAll(beneficiary);
        notifyDataSetChanged();
    }

    public List<String> getAllBeneficiary() {
        return beneficiary;
    }

    public void hideDeleteImageStatus(boolean deleteImageStatus) {
        this.deleteImageStatus = deleteImageStatus;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        @BindView(R.id.iv_customer_picture)
        ImageView ivCustomerImage;

        @BindView(R.id.tv_identifier)
        TextView tvIdentifier;

        @BindView(R.id.iv_remove_identifier)
        ImageView ivRemoveIdentifier;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            ivRemoveIdentifier.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            beneficiary.remove(getAdapterPosition());
            notifyDataSetChanged();
        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }
}