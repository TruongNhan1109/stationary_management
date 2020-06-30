package com.ttn.stationarymanagement.data.local.interactor;

import com.j256.ormlite.dao.Dao;
import com.ttn.stationarymanagement.data.local.model.PhongBan;
import java.sql.SQLException;
import java.util.List;

public class DepartmentUseCase {

    public static int create(final Dao<PhongBan, Long> dao, PhongBan phongBan) throws SQLException {
        return dao.create(phongBan);
    }

    public static int update(final Dao<PhongBan, Long> dao, PhongBan phongBan)throws SQLException{
        return dao.update(phongBan);
    }

    public static int delete(final Dao<PhongBan, Long> dao, PhongBan phongBan)throws SQLException{
        return dao.delete(phongBan);
    }

    public static PhongBan getById(final Dao<PhongBan, Long> dao, long id)throws SQLException{
        return dao.queryForId(id);
    }

    public static List<PhongBan> getAll(final Dao<PhongBan, Long> dao) throws SQLException{
        return dao.queryForAll();
    }
}
