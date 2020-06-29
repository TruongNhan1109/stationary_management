package com.ttn.stationarymanagement.data.local.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "VaiTro")
public class VaiTro {

    @DatabaseField(columnName = "MaVT", generatedId = true)
    private long MaVT;

    @DatabaseField
    private String TenVaiTro;

    @DatabaseField
    private  String NgayTao;


   public VaiTro(String name) {
       this.TenVaiTro = name;
   }

   public VaiTro(){}

    public long getMaVT() {
        return MaVT;
    }

    public void setMaVT(long maVT) {
        MaVT = maVT;
    }

    public String getTenVaiTro() {
        return TenVaiTro;
    }

    public void setTenVaiTro(String tenVaiTro) {
        TenVaiTro = tenVaiTro;
    }

    public String getNgayTao() {
        return NgayTao;
    }

    public void setNgayTao(String ngayTao) {
        NgayTao = ngayTao;
    }
}
