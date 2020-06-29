package com.ttn.stationarymanagement.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.model.VaiTro;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.RoleViewHolder> {

    private Context mContext;
    private List<VaiTro> listRole;
    private LayoutInflater layoutInflater;

    public RoleAdapter (Context context, List<VaiTro> list) {
        this.mContext = context;
        this.listRole = list;
        layoutInflater = layoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RoleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_role, parent, false);
        return new RoleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoleViewHolder holder, int position) {
        VaiTro mItem = listRole.get(position);

        holder.tvNameRole.setText(mItem.getTenVaiTro() != null ? mItem.getTenVaiTro() : "");

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(position);
            }
        });


        holder.ibtRemove.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onRemoveButtonClick(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listRole.size();
    }

    public class RoleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_role_name)
        TextView tvNameRole;

        @BindView(R.id.ibt_item_role_remove)
        ImageButton ibtRemove;

        public RoleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setListener(RoleAdapterListener listener) {
        this.mListener = listener;
    }

    private RoleAdapterListener mListener;

    public interface RoleAdapterListener {
        public void onItemClick(int position);
        public void onRemoveButtonClick(int position);
    }

}
