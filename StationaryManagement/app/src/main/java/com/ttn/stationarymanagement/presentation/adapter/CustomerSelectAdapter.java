package com.ttn.stationarymanagement.presentation.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.model.NhanVien;

import java.io.File;
import java.util.List;

public class CustomerSelectAdapter  extends BaseAdapter {

    Context mContext;
    List<NhanVien> listStafts;
    LayoutInflater layoutInflater;

    public  CustomerSelectAdapter (Context context, List<NhanVien> list) {
        this.mContext = context;
        this.listStafts = list;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return listStafts.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.item_select_customer, null);

        ImageView ivImage = (ImageView) convertView.findViewById(R.id.iv_item_select_customer_image);
        TextView tvNameStaft = (TextView) convertView.findViewById(R.id.tv_item_select_customer_name_customer);

        NhanVien nhanVien = listStafts.get(position);

        if (!TextUtils.isEmpty(nhanVien.getTenNV())) {
            tvNameStaft.setText(nhanVien.getTenNV());
        } else {
            tvNameStaft.setText("");
        }

        if (!TextUtils.isEmpty(nhanVien.getAnh())) {
            Picasso.get().load(new File(nhanVien.getAnh())).error(R.drawable.ic_people_color_32).resize(48, 48).into(ivImage);
        } else {
            ivImage.setImageResource(R.drawable.ic_people_color_32);
        }

        return convertView;
    }
}
