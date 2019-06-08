package org.apache.fineract.ui.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.fineract.R;
import org.apache.fineract.data.models.rolesandpermission.Role;
import org.apache.fineract.injection.ApplicationContext;
import org.apache.fineract.ui.base.OnItemClickListener;
import org.apache.fineract.ui.views.TextDrawable;
import org.apache.fineract.utils.ColorGenerator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 24/08/17.
 */
public class RolesAdapter extends RecyclerView.Adapter<RolesAdapter.ViewHolder> {

    private Context context;
    private List<Role> roles;
    private ColorGenerator colorGenerator;
    private OnItemClickListener onItemClickListener;

    @Inject
    public RolesAdapter(@ApplicationContext Context context) {
        this.context = context;
        colorGenerator = ColorGenerator.MATERIAL;
        roles = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_roles, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Role role = roles.get(position);

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig().toUpperCase().endConfig()
                .buildRound(role.getIdentifier().substring(0, 1),
                        colorGenerator.getColor(role.getIdentifier()));
        holder.ivRole.setImageDrawable(drawable);

        holder.tvRoleName.setText(role.getIdentifier());
    }

    @Override
    public int getItemCount() {
        return roles.size();
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        @BindView(R.id.iv_role)
        ImageView ivRole;

        @BindView(R.id.tv_role_name)
        TextView tvRoleName;

        @BindView(R.id.ll_role)
        LinearLayout llRole;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            llRole.setOnClickListener(this);
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
