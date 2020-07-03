package com.ttn.stationarymanagement.data.local.interactor;

import com.j256.ormlite.dao.Dao;
import com.ttn.stationarymanagement.data.local.model.NhanVien;
import com.ttn.stationarymanagement.data.local.model.stationery.VanPhongPham;

import java.sql.SQLException;
import java.util.List;

public class StaftUseCase {

    public static int create(final Dao<NhanVien, Long> dao, NhanVien nhanVien) throws SQLException {
        return dao.create(nhanVien);
    }

    public static int update(final Dao<NhanVien, Long> dao, NhanVien nhanVien)throws SQLException{
        return dao.update(nhanVien);
    }

    public static int delete(final Dao<NhanVien, Long> dao, NhanVien nhanVien)throws SQLException{
        return dao.delete(nhanVien);
    }

    public static NhanVien getById(final Dao<NhanVien, Long> dao, long id)throws SQLException{
        return dao.queryForId(id);
    }

    public static List<NhanVien> getAll(final Dao<NhanVien, Long> dao) throws SQLException{
        return dao.queryForAll();
    }

}
