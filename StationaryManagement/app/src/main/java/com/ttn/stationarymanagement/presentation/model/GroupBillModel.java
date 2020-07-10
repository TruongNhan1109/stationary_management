package com.ttn.stationarymanagement.presentation.model;

import com.ttn.stationarymanagement.data.local.model.CapPhat;
import com.ttn.stationarymanagement.data.local.model.VanPhongPham;

import java.util.List;

public class GroupBillModel {

    private String nameGroup;
    private List<CapPhat> listBills;

    public GroupBillModel(String nameGroup, List<CapPhat> bills) {
        this.nameGroup = nameGroup;
        this.listBills = bills;
    }

    public GroupBillModel() {
    }


    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    public List<CapPhat> getListBills() {
        return listBills;
    }

    public void setListBills(List<CapPhat> listBills) {
        this.listBills = listBills;
    }
}
