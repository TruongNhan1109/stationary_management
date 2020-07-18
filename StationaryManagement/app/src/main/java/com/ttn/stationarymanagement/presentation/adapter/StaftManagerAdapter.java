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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.NhanVien;
import com.ttn.stationarymanagement.data.local.model.PhongBan;
import com.ttn.stationarymanagement.data.local.model.VaiTro;
import com.ttn.stationarymanagement.utils.GetDataToCommunicate;

import java.io.File;
import java.util.List;

import butterknife.BindView;
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
        View itemView = layoutInflater.inflate(R.layout.item_staft_manager, parent, false);
        return new StaftManagerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StaftManagerViewHolder holder, int position) {
        NhanVien mItem = listNhanVien.get(position);

        if (!TextUtils.isEmpty(mItem.getAnh())) {
            Picasso.get().load(new File(mItem.getAnh())).error(R.drawable.ic_people_color_32).fit().centerInside().into(holder.ivAvatar);
        } else {
            holder.ivAvatar.setImageResource(R.drawable.ic_people_color_32);
        }

        holder.tvNameStaft.setText(mItem.getTenNV());
        holder.tvNameStaft.append( " (" + mItem.getNgaySinh() + ")");

        if (mItem.getGT() == 0) {
            holder.tvGenderStaft.setText( mContext.getResources().getString(R.string.gender) + ": " + mContext.getResources().getString(R.string.male));
        } else if (mItem.getGT() == 1) {
            holder.tvGenderStaft.setText(mContext.getResources().getString(R.string.gender) + ": " + mContext.getResources().getString(R.string.female));
        } else {
            holder.tvGenderStaft.setText(mContext.getResources().getString(R.string.gender) + ": " + mContext.getResources().getString(R.string.other));
        }

        if (!TextUtils.isEmpty(mItem.getSDT())) {
            holder.tvPhoneStaft.setVisibility(View.VISIBLE);
            holder.tvPhoneStaft.setText(mItem.getSDT());
        } else {
            holder.tvPhoneStaft.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mItem.getEmail())) {
            holder.tvEmail.setVisibility(View.VISIBLE);
            holder.tvEmail.setText(mItem.getEmail());
        } else {
            holder.tvEmail.setVisibility(View.GONE);
        }

        VaiTro vaitro  = WorkWithDb.getInstance().getRoleById(mItem.getMaVT());

        if (vaitro != null) {
            holder.tvRoleStaft.setVisibility(View.VISIBLE);
            holder.tvRoleStaft.setText( mContext.getResources().getString(R.string.role) +": " + vaitro.getTenVaiTro());
        } else {
            holder.tvRoleStaft.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mItem.getGhiChu())) {
            holder.tvNote.setVisibility(View.VISIBLE);
            holder.tvNote.setText(mItem.getGhiChu());

        } else {
            holder.tvNote.setVisibility(View.GONE);
        }

        PhongBan phongBan =  WorkWithDb.getInstance().getDepartmentById(mItem.getMaPB());

        if (phongBan != null) {
            holder.tvDepartment.setVisibility(View.VISIBLE);
            holder.tvDepartment.setText(phongBan.getTenPB());
        } else {
            holder.tvDepartment.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(position);
            }
        });

        holder.ibtRemove.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onRemoveClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listNhanVien.size();
    }

    public class StaftManagerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_avartar_staft)
        ImageView ivAvatar;

        @BindView(R.id.tv_name_staft)
        TextView tvNameStaft;

        @BindView(R.id.tv_gender_staft)
        TextView tvGenderStaft;

        @BindView(R.id.tv_role_staft)
        TextView tvRoleStaft;

        @BindView(R.id.tv_phone_staft)
        TextView tvPhoneStaft;

        @BindView(R.id.tv_email_staft)
        TextView tvEmail;

        @BindView(R.id.ibt_item_role_remove)
        ImageButton ibtRemove;

        @BindView(R.id.tv_note_staft)
        TextView tvNote;

        @BindView(R.id.tv_department)
        TextView tvDepartment;

        public StaftManagerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface StaftManagerAdapterListener {
        void onItemClick(int position);
        void onRemoveClick(int position);
    }

    private StaftManagerAdapterListener mListener;

    public void setListener(StaftManagerAdapterListener listener) {
        this.mListener = listener;
    }

}
