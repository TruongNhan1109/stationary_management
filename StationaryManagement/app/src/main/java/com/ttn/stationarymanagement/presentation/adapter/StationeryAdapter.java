package com.ttn.stationarymanagement.presentation.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.model.stationery.VanPhongPham;
import com.ttn.stationarymanagement.utils.GetDataToCommunicate;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StationeryAdapter extends  RecyclerView.Adapter<StationeryAdapter.StationeryViewHolder> {

    private Context mContext;
    private List<VanPhongPham> vanPhongPhamList;
    private LayoutInflater layoutInflater;
    private GroupProductAdapter.GroupProductApapterListener mListener;

    public void setListener(GroupProductAdapter.GroupProductApapterListener listener) {
        this.mListener = listener;
    }

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
        VanPhongPham mItem = vanPhongPhamList.get(position);

        if (!TextUtils.isEmpty(mItem.getAnh())) {
            Picasso.get().load(new File(mItem.getAnh())).fit().centerInside().into(holder.ivPhoto);
        } else {
            holder.ivPhoto.setImageResource(R.drawable.ic_part_color_24);
        }

        holder.tvNameProduct.setText(!TextUtils.isEmpty(mItem.getTenSP()) ? mItem.getTenSP() : "");

        holder.tvNumber.setText("Số lượng " +  mItem.getSoLuong()  + (TextUtils.isEmpty(mItem.getDonVi()) ? "" : " (" + mItem.getDonVi() + ")"));
        holder.tvPrice.setText("Đơn giá "  + GetDataToCommunicate.changeToPrice( mItem.getDonGia()) + "");

        if (!TextUtils.isEmpty(mItem.getGhiChu())) {
            holder.tvNote.setVisibility(View.VISIBLE);
            holder.tvNote.setText(mItem.getGhiChu());
        } else {
            holder.tvNote.setVisibility(View.GONE);
        }

        holder.ibtShowMenu.setOnClickListener(v -> {

            PopupMenu popupMenu = new PopupMenu(mContext, v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_option_product_manager, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {

                switch (item.getItemId()) {

                    case R.id.mn_edit_product:
                        if (mListener != null) {
                            mListener.onProductClick(vanPhongPhamList.get(position));
                        }
                        break;

                    case R.id.mn_import_product:

                        break;

                    case R.id.mn_delete_product:
                        mListener.onDeleteProduct(0, mItem);
                        break;
                }

                return false;
            });

            popupMenu.show();

            holder.itemView.setOnClickListener(v1 -> {
                if (mListener != null) {
                    mListener.onItemClick(vanPhongPhamList.get(position));
                }

            });


        });


    }

    @Override
    public int getItemCount() {
        return vanPhongPhamList.size();
    }


    public class StationeryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_item_stationery_image_product)
        ImageView ivPhoto;

        @BindView(R.id.tv_item_stationery_number)
        TextView tvNumber;

        @BindView(R.id.tv_item_stationery_price)
        TextView tvPrice;

        @BindView(R.id.tv_item_stationery_name_product)
        TextView tvNameProduct;

        @BindView(R.id.tv_item_stationery_note)
        TextView tvNote;

        @BindView(R.id.ibt_item_stationery_show_menu)
        ImageButton ibtShowMenu;


        public StationeryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
