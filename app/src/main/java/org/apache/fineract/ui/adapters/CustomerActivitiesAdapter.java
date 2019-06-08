package org.apache.fineract.ui.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.Command;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.utils.DateUtils;
import org.apache.fineract.utils.StatusUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 15/08/17.
 */
public class CustomerActivitiesAdapter extends
        RecyclerView.Adapter<CustomerActivitiesAdapter.ViewHolder> {

    private Context context;
    private List<Command> commands;

    @Inject
    public CustomerActivitiesAdapter(@ApplicationContext Context context) {
        this.context = context;
        commands = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer_activities, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Command command = commands.get(position);

        holder.tvActivity.setText(command.getAction().name());
        holder.tvCommandChangeDateTime.setText(
                context.getString(R.string.activities_created_by_on,
                        command.getCreatedBy(), DateUtils.getDate(command.getCreatedOn(),
                                DateUtils.STANDARD_DATE_TIME_FORMAT,
                                DateUtils.ACTIVITIES_DATE_FORMAT)));

        StatusUtils.setCustomerActivitiesStatusIcon(command.getAction(),
                holder.ivCommandStatus, context);
    }

    @Override
    public int getItemCount() {
        return commands.size();
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_command_status)
        ImageView ivCommandStatus;

        @BindView(R.id.tv_activity)
        TextView tvActivity;

        @BindView(R.id.tv_status_change_date_time)
        TextView tvCommandChangeDateTime;


        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}