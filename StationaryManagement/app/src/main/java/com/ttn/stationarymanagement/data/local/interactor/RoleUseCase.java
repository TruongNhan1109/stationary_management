package com.ttn.stationarymanagement.data.local.interactor;

import com.j256.ormlite.dao.Dao;
import com.ttn.stationarymanagement.data.local.model.VaiTro;

import java.sql.SQLException;
import java.util.List;

public class RoleUseCase {

    public static int create(final Dao<VaiTro, Long> dao, VaiTro role) throws SQLException {
        return dao.create(role);
    }

    public static int update(final Dao<VaiTro, Long> dao, VaiTro role)throws SQLException{
        return dao.update(role);
    }

    public static int delete(final Dao<VaiTro, Long> dao, VaiTro role)throws SQLException{
        return dao.delete(role);
    }

    public static VaiTro getById(final Dao<VaiTro, Long> dao, long id)throws SQLException{
        return dao.queryForId(id);
    }

    public static List<VaiTro> getAll(final Dao<VaiTro, Long> dao) throws SQLException{
        return dao.queryForAll();
    }
}
