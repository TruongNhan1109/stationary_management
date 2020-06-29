package com.ttn.stationarymanagement.data.local.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "PhongBan")
public class PhongBan {

    @DatabaseField(generatedId = true)
    private long MaPB;

    @DatabaseField
    private String TenPB;

    @DatabaseField
    private String NgayTao;

    @DatabaseField
    private String GhiChu;

    public PhongBan(String name, String note) {
        this.TenPB = name;
        this.GhiChu = note;
    }

    public PhongBan() {
    }


    public long getMaPB() {
        return MaPB;
    }

    public void setMaPB(long maPB) {
        MaPB = maPB;
    }

    public String getTenPB() {
        return TenPB;
    }

    public void setTenPB(String tenPB) {
        TenPB = tenPB;
    }

    public String getNgayTao() {
        return NgayTao;
    }

    public void setNgayTao(String ngayTao) {
        NgayTao = ngayTao;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        GhiChu = ghiChu;
    }

}
