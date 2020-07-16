package com.ttn.stationarymanagement.data.local.interactor;

import com.j256.ormlite.dao.Dao;
import com.ttn.stationarymanagement.data.local.model.CapPhat;
import com.ttn.stationarymanagement.data.local.model.NhanVien;

import java.sql.SQLException;
import java.util.List;

public class AllocationUseCase {

    public static int create(final Dao<CapPhat, Long> dao, CapPhat capPhat) throws SQLException {
        return dao.create(capPhat);
    }

    public static int update(final Dao<CapPhat, Long> dao, CapPhat capPhat)throws SQLException{
        return dao.update(capPhat);
    }

    public static int delete(final Dao<CapPhat, Long> dao, CapPhat capPhat)throws SQLException{
        return dao.delete(capPhat);
    }

    public static CapPhat getById(final Dao<CapPhat, Long> dao, long id)throws SQLException{
        return dao.queryForId(id);
    }

    public static List<CapPhat> getByIdStaft(final Dao<CapPhat, Long> dao, long id)throws SQLException{
        return dao.queryBuilder().where().eq("MaNV", id ).query();
    }

    public static List<CapPhat> getByIdStaft(final Dao<CapPhat, Long> dao, String id)throws SQLException{
        return dao.queryBuilder().where().eq("MaNV", id ).query();
    }

    public static List<CapPhat> getByProductId(final Dao<CapPhat, Long> dao, String idProduct)throws SQLException{
        return dao.queryBuilder().where().eq("MaVPP", idProduct ).query();
    }

    public static List<CapPhat> getAll(final Dao<CapPhat, Long> dao) throws SQLException{
        return dao.queryForAll();
    }

    public static List<CapPhat> getAllByIdBill(final Dao<CapPhat, Long> dao, String idBill) throws SQLException{
        return dao.queryBuilder().where()
                .eq("MaPhieu", idBill).query();
    }

    public static List<CapPhat> getAllByStaftId(final Dao<CapPhat, Long> dao, String staftId) throws SQLException{
        return dao.queryBuilder().where()
                .eq("MaNV", staftId).query();
    }

    public static List<CapPhat> getAllByIdProduct(final Dao<CapPhat, Long> dao, String idProduct) throws SQLException{
        return dao.queryBuilder().where()
                .eq("MaVPP", idProduct).query();
    }

}
