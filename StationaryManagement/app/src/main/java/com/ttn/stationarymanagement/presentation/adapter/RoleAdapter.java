package com.ttn.stationarymanagement.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.model.VaiTro;

import java.util.List;

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

    }

    @Override
    public int getItemCount() {
        return listRole.size();
    }


    public class RoleViewHolder extends RecyclerView.ViewHolder {

        public RoleViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
