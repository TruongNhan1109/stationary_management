package com.ttn.stationarymanagement.data.local.interactor;

import com.j256.ormlite.dao.Dao;
import com.ttn.stationarymanagement.data.local.model.PhongBan;
import com.ttn.stationarymanagement.data.local.model.stationery.VanPhongPham;

import java.sql.SQLException;
import java.util.List;

public class ProductUseCase {

    public static int create(final Dao<VanPhongPham, Long> dao, VanPhongPham vanPhongPham) throws SQLException {
        return dao.create(vanPhongPham);
    }

    public static int update(final Dao<VanPhongPham, Long> dao, VanPhongPham vanPhongPham)throws SQLException{
        return dao.update(vanPhongPham);
    }

    public static int delete(final Dao<VanPhongPham, Long> dao, VanPhongPham vanPhongPham)throws SQLException{
        return dao.delete(vanPhongPham);
    }

    public static VanPhongPham getById(final Dao<VanPhongPham, Long> dao, long id)throws SQLException{
        return dao.queryForId(id);
    }

    public static List<VanPhongPham> getAll(final Dao<VanPhongPham, Long> dao) throws SQLException{
        return dao.queryForAll();
    }

}
