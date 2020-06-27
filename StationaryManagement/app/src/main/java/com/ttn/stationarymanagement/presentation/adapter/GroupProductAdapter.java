package com.ttn.stationarymanagement.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.model.stationery.VanPhongPham;
import com.ttn.stationarymanagement.presentation.model.GroupProductModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupProductAdapter extends RecyclerView.Adapter<GroupProductAdapter.GroupProductViewHolder> {

    private Context mContext;
    private List<GroupProductModel> groupProductModels;
    private LayoutInflater layoutInflater;

    public GroupProductAdapter (Context context, List<GroupProductModel> list) {
        this.mContext = context;
        this.groupProductModels = list;
        this.layoutInflater = layoutInflater.from(mContext);

    }

    @NonNull
    @Override
    public GroupProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_group_alpha, parent, false);
        return new GroupProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupProductViewHolder holder, int position) {
        GroupProductModel mItem = groupProductModels.get(position);

        holder.tvIndex.setText(mItem.getTextAlpha());

        StationeryAdapter stationeryAdapter = new StationeryAdapter(mContext,mItem.getVanPhongPhamList() );
        holder.rvListProducts.setLayoutManager(holder.linearLayoutManager);
        holder.rvListProducts.setAdapter(stationeryAdapter);
    }

    @Override
    public int getItemCount() {
        return groupProductModels.size();
    }


    public class  GroupProductViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_group_alpha_index)
        TextView tvIndex;

        @BindView(R.id.rv_item_group_alpha_list_product)
        RecyclerView rvListProducts;

        LinearLayoutManager linearLayoutManager;

        StationeryAdapter stationeryAdapter;

        public GroupProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            linearLayoutManager = new LinearLayoutManager(mContext);
        }
    }

}
