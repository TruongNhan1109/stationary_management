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
import com.ttn.stationarymanagement.presentation.model.GroupBillModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupBillAdapter  extends RecyclerView.Adapter<GroupBillAdapter.GroupBillViewHolder> {

    private Context mContext;
    private List<GroupBillModel> listGroups;
    private LayoutInflater layoutInflater;

    public GroupBillAdapter(Context context, List<GroupBillModel> list) {
        this.mContext = context;
        this.listGroups = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public GroupBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_group_bill, parent, false);
        return new GroupBillViewHolder(itemView, mContext);

    }

    @Override
    public void onBindViewHolder(@NonNull GroupBillViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listGroups.size();
    }

    class GroupBillViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_group_bill_name_group)
        TextView tvNameGroup;

        @BindView(R.id.rv_item_group_bill_list)
        RecyclerView rvListItem;

        private LinearLayoutManager linearLayoutManager;

        public GroupBillViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        }
    }

}
