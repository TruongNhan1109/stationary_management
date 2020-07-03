package com.ttn.stationarymanagement.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.model.PhongBan;
import com.ttn.stationarymanagement.data.local.model.VaiTro;

import java.util.List;

public class SelectDepartmentAdapter  extends BaseAdapter {

    private Context mContext;
    List<PhongBan> listPhongBan;
    LayoutInflater layoutInflater;

    public SelectDepartmentAdapter(Context context, List<PhongBan> list) {

        this.mContext = context;
        this.listPhongBan = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listPhongBan.size();
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

        convertView = layoutInflater.inflate(R.layout.item_custom_spiner, null);
        TextView tvValue = (TextView) convertView.findViewById(R.id.tv_item_custom_spiner);
        tvValue.setText(listPhongBan.get(position).getTenPB());

        return convertView;
    }

}