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
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.CapPhat;
import com.ttn.stationarymanagement.data.local.model.NhanVien;
import com.ttn.stationarymanagement.data.local.model.VanPhongPham;
import com.ttn.stationarymanagement.utils.GetDataToCommunicate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        CapPhat mItem = listBills.get(position);

        holder.tvIdBill.setText(mItem.getMaPhieu() + "");
        holder.tvDateCreate.setText(mItem.getNgayCap());
        VanPhongPham sanPham = WorkWithDb.getInstance().getProductById(mItem.getMaVPP());

        if (sanPham != null) {
            holder.tvNameProduct.setText(sanPham.getTenSP() + "");
        }


        NhanVien nhanVien = WorkWithDb.getInstance().getStaftById(mItem.getMaNV());

        if (nhanVien != null) {
            holder.tvNameStaft.setText(nhanVien.getTenNV());
        } else {
            holder.tvNameStaft.setText("");
        }

        holder.tvAmount.setText(mItem.getSoLuong() + "");
        holder.tvTotalPrice.setText(GetDataToCommunicate.changeToPrice( mItem.getTongGia() )+ "");

    }

    @Override
    public int getItemCount() {
        return listBills.size();
    }


    class BillHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ibt_item_bill_remove)
        ImageButton ibtRemove;

        @BindView(R.id.tv_item_bill_total_price)
        TextView tvTotalPrice;

        @BindView(R.id.tv_item_bill_amount)
        TextView tvAmount;

        @BindView(R.id.tv_item_bill_name_staft)
        TextView tvNameStaft;

        @BindView(R.id.tv_item_bill_name_product)
        TextView tvNameProduct;

        @BindView(R.id.tv_item_bill_id_bill)
        TextView tvIdBill;

        @BindView(R.id.tv_item_bill_date_create)
        TextView tvDateCreate;


        public BillHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
