package com.ttn.stationarymanagement.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.model.stationery.VanPhongPham;

import java.util.List;

public class StationeryAdapter extends  RecyclerView.Adapter<StationeryAdapter.StationeryViewHolder> {

    private Context mContext;
    private List<VanPhongPham> vanPhongPhamList;
    private LayoutInflater layoutInflater;

    public StationeryAdapter(Context context, List<VanPhongPham> list) {
        this.vanPhongPhamList = list;
        this.mContext = context;
        layoutInflater = layoutInflater.from(context);

    }

    @NonNull
    @Override
    public StationeryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_stationery, parent, false);
        return new StationeryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StationeryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return vanPhongPhamList.size();
    }


    public class StationeryViewHolder extends RecyclerView.ViewHolder {
        public StationeryViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
