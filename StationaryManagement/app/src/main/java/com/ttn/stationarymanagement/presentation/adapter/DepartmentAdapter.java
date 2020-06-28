package com.ttn.stationarymanagement.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.model.PhongBan;

import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentViewHolder> {

    private Context mContext;
    private List<PhongBan> listDepartment;
    private LayoutInflater layoutInflater;

    public DepartmentAdapter(Context context, List<PhongBan> list) {
        this.mContext = context;
        this.listDepartment = list;
        layoutInflater = layoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_department, parent, false);
        return new DepartmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listDepartment.size();
    }


    public class DepartmentViewHolder extends RecyclerView.ViewHolder {
        public DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
