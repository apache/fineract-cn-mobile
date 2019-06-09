package org.apache.fineract.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.models.payment.CostComponent;
import org.apache.fineract.data.models.payment.PlannedPayment;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.utils.DateUtils;
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
    private boolean showCollapsingSectionControls = true;

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

    public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder
            implements View.OnClickListener {

        @BindView(R.id.tv_payment_date)
        TextView tvPaymentDate;

        @BindView(R.id.tv_remaining_principal)
        TextView tvRemainingPrincipal;

        @BindView(R.id.iv_collapse)
        ImageView ivCollapse;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivCollapse.setOnClickListener(this);
            if (!showCollapsingSectionControls) {
                ivCollapse.setVisibility(View.GONE);
            }
        }

        void updateSectionCollapseToggle(boolean sectionIsCollapsed) {
            @DrawableRes int id = sectionIsCollapsed
                    ? R.drawable.ic_arrow_drop_down_black_24dp
                    : R.drawable.ic_arrow_drop_up_black_24dp;
            ivCollapse.setImageDrawable(ContextCompat.getDrawable(context, id));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            final int section = PlannedPaymentAdapter.this.getSectionForAdapterPosition(position);
            if (v == ivCollapse) {
                PlannedPaymentAdapter.this.onToggleSectionCollapse(section);
                updateSectionCollapseToggle(PlannedPaymentAdapter.this.isSectionCollapsed(section));
            }
        }
    }

    private void onToggleSectionCollapse(int sectionIndex) {
        setSectionIsCollapsed(sectionIndex, !isSectionCollapsed(sectionIndex));
    }

    private ArrayList<Section> sections = new ArrayList<>();

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
    public GhostHeaderViewHolder onCreateGhostHeaderViewHolder(ViewGroup parent) {
        final View ghostView = new View(parent.getContext());
        ghostView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return new GhostHeaderViewHolder(ghostView);
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
        if (section.date != null) {
            hvh.tvPaymentDate.setText(DateUtils.getDate(section.date, DateUtils.INPUT_DATE_FORMAT,
                    DateUtils.OUTPUT_DATE_FORMAT));
        } else {
            hvh.tvPaymentDate.setText(context.getString(R.string.planned_payment));
        }
        hvh.tvRemainingPrincipal.setText(context.getString(R.string.remaining_principal)
                + context.getString(R.string.colon) + section.remainingPrincipal);

        hvh.updateSectionCollapseToggle(isSectionCollapsed(sectionIndex));
    }
}
