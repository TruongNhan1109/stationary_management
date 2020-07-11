package com.ttn.stationarymanagement.presentation.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.model.PhongBan;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        PhongBan mItem = listDepartment.get(position);

        if(!TextUtils.isEmpty(mItem.getTenPB())) {
            holder.tvNameDepartment.setText(mItem.getTenPB());
        } else {
            holder.tvNameDepartment.setText("");
        }

        if(!TextUtils.isEmpty(mItem.getGhiChu())) {
            holder.tvNote.setVisibility(View.VISIBLE);
            holder.tvNote.setText(mItem.getGhiChu());
        } else {
           holder.tvNote.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(position);
            }
        });

        holder.btnRemove.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemRemove(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listDepartment.size();
    }


    public class DepartmentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ibt_item_department_remove)
        ImageButton btnRemove;

        @BindView(R.id.tv_item_department_note)
        TextView tvNote;

        @BindView(R.id.tv_item_department_name_department)
        TextView tvNameDepartment;

        public DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface  DepartmentAdapterListener {
        public void onItemClick(int position);
        public void onItemRemove(int position);

    }

    private DepartmentAdapterListener mListener;

    public void setListenter(DepartmentAdapterListener listenter)  {
        this.mListener = listenter;
    }

}
