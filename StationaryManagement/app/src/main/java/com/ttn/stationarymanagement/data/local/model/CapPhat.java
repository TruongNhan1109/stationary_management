package com.ttn.stationarymanagement.data.local.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "CapPhat")
public class CapPhat {

    @DatabaseField(columnName = "MaPhieu", generatedId = true)
    private long MaPhieu;

    @DatabaseField
    private long MaVPP;

    @DatabaseField
    private long MaNV;

    @DatabaseField
    private String NgayCap;

    @DatabaseField
    private int SoLuong;

    @DatabaseField
    private String NgayTD;

    @DatabaseField
    private int TrangThai;

    @DatabaseField
    private String GhiChu;

    @DatabaseField
    private double TongGia;

    public CapPhat() {

    }


    public long getMaPhieu() {
        return MaPhieu;
    }

    public void setMaPhieu(long maPhieu) {
        MaPhieu = maPhieu;
    }

    public long getMaVPP() {
        return MaVPP;
    }

    public void setMaVPP(long maVPP) {
        MaVPP = maVPP;
    }

    public long getMaNV() {
        return MaNV;
    }

    public void setMaNV(long maNV) {
        MaNV = maNV;
    }

    public String getNgayCap() {
        return NgayCap;
    }

    public void setNgayCap(String ngayCap) {
        NgayCap = ngayCap;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int soLuong) {
        SoLuong = soLuong;
    }

    public String getNgayTD() {
        return NgayTD;
    }

    public void setNgayTD(String ngayTD) {
        NgayTD = ngayTD;
    }

    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int trangThai) {
        TrangThai = trangThai;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        GhiChu = ghiChu;
    }

    public double getTongGia() {
        return TongGia;
    }

    public void setTongGia(double tongGia) {
        TongGia = tongGia;
    }
}
