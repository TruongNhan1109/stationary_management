package com.ttn.stationarymanagement.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ttn.stationarymanagement.data.local.model.NhanVien;

import java.util.List;

import butterknife.ButterKnife;

public class StaftManagerAdapter  extends RecyclerView.Adapter<StaftManagerAdapter.StaftManagerViewHolder> {

    private Context mContext;
    private List<NhanVien> listNhanVien;
    private LayoutInflater layoutInflater;

    public  StaftManagerAdapter(Context context, List<NhanVien> list) {
        this.mContext = context;
        this.listNhanVien = list;
        layoutInflater = layoutInflater.from(mContext);

    }

    @NonNull
    @Override
    public StaftManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull StaftManagerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listNhanVien.size();
    }


    public class StaftManagerViewHolder extends RecyclerView.ViewHolder {

        public StaftManagerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
