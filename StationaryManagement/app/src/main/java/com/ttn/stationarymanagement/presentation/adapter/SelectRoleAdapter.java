package com.ttn.stationarymanagement.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.model.VaiTro;

import java.util.List;

public class SelectRoleAdapter extends BaseAdapter {

    private Context mContext;
    List<VaiTro> listVaiTro;
    LayoutInflater layoutInflater;

    public SelectRoleAdapter(Context context, List<VaiTro> list) {

        this.mContext = context;
        this.listVaiTro = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listVaiTro.size();
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
        tvValue.setText(listVaiTro.get(position).getTenVaiTro());
        return convertView;
    }

}
