package org.apache.fineract.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.identification.ScanCard;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 01/08/17.
 */
public class IdentificationScanAdapter extends
        RecyclerView.Adapter<IdentificationScanAdapter.ViewHolder> {

    private List<ScanCard> scanCards;
    public OnItemClickListener onItemClickListener;

    @Inject
    public IdentificationScanAdapter() {
        scanCards = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_identification_scan_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScanCard scanCard = scanCards.get(position);
        holder.tvIdentifier.setText(scanCard.getIdentifier());
        holder.tvDescription.setText(scanCard.getDescription());
    }

    @Override
    public int getItemCount() {
        return scanCards.size();
    }

    public void setScanCards(List<ScanCard> scanCards) {
        this.scanCards = scanCards;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
    }

    public ScanCard getItem(int position) {
        return scanCards.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_delete_scan_card)
        ImageView ivDeleteScanCard;

        @BindView(R.id.tv_identifier)
        TextView tvIdentifier;

        @BindView(R.id.tv_description)
        TextView tvDescription;

        @BindView(R.id.ll_identifier_card)
        LinearLayout llIdentifierCard;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            llIdentifierCard.setOnClickListener(this);
            ivDeleteScanCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_identifier_card:
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                    break;
                case R.id.iv_delete_scan_card:
                    onItemClickListener.onItemDelete(v, getAdapterPosition());
                    break;
            }

        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

        void onItemDelete(View view, int position);
    }
}