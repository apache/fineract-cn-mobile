package org.apache.fineract.ui.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.identification.Identification;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.ui.base.OnItemClickListener;
import org.apache.fineract.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 31/07/17.
 */
public class IdentificationAdapter extends RecyclerView.Adapter<IdentificationAdapter.ViewHolder> {

    private Context context;
    private List<Identification> identifications;
    public OnItemClickListener onItemClickListener;

    @Inject
    public IdentificationAdapter(@ApplicationContext Context context) {
        this.context = context;
        identifications = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_identification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Identification identification = identifications.get(position);
        holder.tvIdentificationType.setText(identification.getType());
        holder.tvIdentificationIssue.setText(context.getString(R.string.identification_issuer,
                identification.getIssuer()));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, identification.getExpirationDate().getYear());
        calendar.set(Calendar.MONTH, identification.getExpirationDate().getMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, identification.getExpirationDate().getDay());
        holder.tvExpirationDate.setText(DateUtils.convertServerDate(calendar));
    }

    @Override
    public int getItemCount() {
        return identifications.size();
    }

    public Identification getItem(int position) {
        return identifications.get(position);
    }

    public void setIdentifications(List<Identification> identifications) {
        this.identifications = identifications;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        @BindView(R.id.tv_identification_type)
        TextView tvIdentificationType;

        @BindView(R.id.tv_identification_issuer)
        TextView tvIdentificationIssue;

        @BindView(R.id.tv_expiration_date)
        TextView tvExpirationDate;

        @BindView(R.id.ll_identifier_card)
        LinearLayout cvCustomer;

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
