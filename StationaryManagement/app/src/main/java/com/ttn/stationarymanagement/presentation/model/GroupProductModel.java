package com.ttn.stationarymanagement.presentation.model;

import com.ttn.stationarymanagement.data.local.model.stationery.VanPhongPham;

import java.util.List;

public class GroupProductModel {

    private  String textAlpha;
    private List<VanPhongPham> vanPhongPhamList;


    public GroupProductModel(String alpha, List<VanPhongPham> list) {
        this.textAlpha = alpha;
        this.vanPhongPhamList = list;
    }

    public String getTextAlpha() {
        return textAlpha;
    }

    public void setTextAlpha(String textAlpha) {
        this.textAlpha = textAlpha;
    }

    public List<VanPhongPham> getVanPhongPhamList() {
        return vanPhongPhamList;
    }

    public void setVanPhongPhamList(List<VanPhongPham> vanPhongPhamList) {
        this.vanPhongPhamList = vanPhongPhamList;
    }
}
