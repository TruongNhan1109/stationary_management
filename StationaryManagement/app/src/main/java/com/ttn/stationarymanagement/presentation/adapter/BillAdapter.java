package com.ttn.stationarymanagement.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.model.CapPhat;
import com.ttn.stationarymanagement.data.local.model.VanPhongPham;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillHolder> {

    private Context mContext;
    private List<CapPhat> listBills;
    private LayoutInflater layoutInflater;

    public BillAdapter(Context context, List<CapPhat> list) {
        this.mContext = context;
        this.listBills = list;
        layoutInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public BillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_bill, parent, false);
        return new BillHolder(itemView) ;
    }

    @Override
    public void onBindViewHolder(@NonNull BillHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listBills.size();
    }


    class BillHolder extends RecyclerView.ViewHolder {
        public BillHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
