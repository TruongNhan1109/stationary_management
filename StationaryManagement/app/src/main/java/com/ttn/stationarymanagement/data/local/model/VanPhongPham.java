package com.ttn.stationarymanagement.data.local.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "VanPhongPham")
public class VanPhongPham {

    @DatabaseField(generatedId = true)
    private  long MaVPP;

    @DatabaseField
    private String TenSP;

    @DatabaseField
    private String DonVi;

    @DatabaseField
    private int SoLuong;

    @DatabaseField
    private double DonGia;

    @DatabaseField
    private String NgayTao;

    @DatabaseField
    private String NgayTD;

    @DatabaseField
    private String GhiChu;

    @DatabaseField
    private String Anh;

    @DatabaseField
    private  int DaDung;

    public VanPhongPham () {
    }

    public long getMaVPP() {
        return MaVPP;
    }

    public void setMaVPP(long maVPP) {
        MaVPP = maVPP;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String tenSP) {
        TenSP = tenSP;
    }

    public String getDonVi() {
        return DonVi;
    }

    public void setDonVi(String donVi) {
        DonVi = donVi;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int soLuong) {
        SoLuong = soLuong;
    }

    public double getDonGia() {
        return DonGia;
    }

    public void setDonGia(double donGia) {
        DonGia = donGia;
    }

    public String getNgayTao() {
        return NgayTao;
    }

    public void setNgayTao(String ngayTao) {
        NgayTao = ngayTao;
    }

    public String getNgayTD() {
        return NgayTD;
    }

    public void setNgayTD(String ngayTD) {
        NgayTD = ngayTD;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        GhiChu = ghiChu;
    }

    public String getAnh() {
        return Anh;
    }

    public void setAnh(String anh) {
        Anh = anh;
    }

    public int getDaDung() {
        return DaDung;
    }

    public void setDaDung(int daDung) {
        DaDung = daDung;
    }
}
