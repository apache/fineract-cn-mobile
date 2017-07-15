package com.mifos.apache.fineract.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.payment.CostComponent;
import com.mifos.apache.fineract.data.models.payment.PlannedPayment;
import com.mifos.apache.fineract.injection.ApplicationContext;
import com.mifos.apache.fineract.utils.DateUtils;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 14/07/17.
 */
public class PlannedPaymentAdapter extends SectioningAdapter {

    private Context context;

    @Inject
    public PlannedPaymentAdapter(@ApplicationContext Context context) {
        this.context = context;
    }

    private class Section {
        Double remainingPrincipal;
        String date;
        ArrayList<CostComponent> costComponents = new ArrayList<>();
    }

    public class ItemViewHolder extends SectioningAdapter.ItemViewHolder {

        @BindView(R.id.tv_charge_name)
        TextView tvChargeName;

        @BindView(R.id.tv_charge_value)
        TextView tvChargeValue;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {

        @BindView(R.id.tv_payment_date)
        TextView tvPaymentDate;

        @BindView(R.id.tv_remaining_principal)
        TextView tvRemainingPrincipal;

        @BindView(R.id.iv_collapse)
        ImageView ivCollapse;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    ArrayList<Section> sections = new ArrayList<>();

    public void setPlannedPayment(List<PlannedPayment> plannedPayments) {
        sections.clear();
        for (PlannedPayment plannedPayment : plannedPayments) {
            Section section = new Section();
            section.costComponents.addAll(plannedPayment.getCostComponents());
            section.remainingPrincipal = plannedPayment.getRemainingPrincipal();
            section.date = plannedPayment.getDate();
            sections.add(section);
        }
        notifyAllSectionsDataSetChanged();
    }

    public void setMorePlannedPayment(List<PlannedPayment> morePlannedPayment) {
        for (PlannedPayment plannedPayment : morePlannedPayment) {
            Section section = new Section();
            section.costComponents.addAll(plannedPayment.getCostComponents());
            section.remainingPrincipal = plannedPayment.getRemainingPrincipal();
            section.date = plannedPayment.getDate();
            sections.add(section);
        }
        notifyAllSectionsDataSetChanged();
    }

    @Override
    public int getNumberOfSections() {
        return sections.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return sections.get(sectionIndex).costComponents.size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return false;
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_panned_payment, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_header_planned_payment, parent, false);
        return new HeaderViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex,
            int itemIndex, int itemType) {
        Section s = sections.get(sectionIndex);
        ItemViewHolder ivh = (ItemViewHolder) viewHolder;
        CostComponent costComponent = s.costComponents.get(itemIndex);
        ivh.tvChargeName.setText(
                Character.toUpperCase(costComponent.getChargeIdentifier().charAt(0))
                        + costComponent.getChargeIdentifier().substring(1));
        ivh.tvChargeValue.setText(String.valueOf(costComponent.getAmount()));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder,
            int sectionIndex, int headerType) {
        Section section = sections.get(sectionIndex);
        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
        hvh.tvPaymentDate.setText(DateUtils.getDate(section.date, DateUtils.INPUT_DATE_FORMAT,
                DateUtils.OUTPUT_DATE_FORMAT));
        hvh.tvRemainingPrincipal.setText(context.getString(R.string.remaining_principal)
                + context.getString(R.string.colon) + section.remainingPrincipal);
    }
}
