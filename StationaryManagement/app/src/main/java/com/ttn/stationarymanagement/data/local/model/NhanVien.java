package com.ttn.stationarymanagement.data.local.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "NhanVien")
public class NhanVien {

    @DatabaseField(columnName = "MaNV", generatedId = true)
    private long MaNV;

    @DatabaseField
    private String TenNV;

    @DatabaseField
    private int GT;

    @DatabaseField
    private String NgaySinh;

    @DatabaseField
    private String SDT;

    @DatabaseField
    private String Email;

    @DatabaseField
    private String NgayTao;

    @DatabaseField
    private long MaPB;

    @DatabaseField
    private long MaVT;

    @DatabaseField
    private String GhiChu;

    @DatabaseField
    private String Anh;

    public NhanVien() {
    }

    public long getMaNV() {
        return MaNV;
    }

    public void setMaNV(long maNV) {
        MaNV = maNV;
    }

    public String getTenNV() {
        return TenNV;
    }

    public void setTenNV(String tenNV) {
        TenNV = tenNV;
    }

    public int getGT() {
        return GT;
    }

    public void setGT(int GT) {
        this.GT = GT;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        NgaySinh = ngaySinh;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNgayTao() {
        return NgayTao;
    }

    public void setNgayTao(String ngayTao) {
        NgayTao = ngayTao;
    }

    public long getMaPB() {
        return MaPB;
    }

    public void setMaPB(long maPB) {
        MaPB = maPB;
    }

    public long getMaVT() {
        return MaVT;
    }

    public void setMaVT(long maVT) {
        MaVT = maVT;
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
}
