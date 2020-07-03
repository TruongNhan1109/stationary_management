package com.ttn.stationarymanagement.data.local;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.model.NhanVien;
import com.ttn.stationarymanagement.data.local.model.PhongBan;
import com.ttn.stationarymanagement.data.local.model.VaiTro;
import com.ttn.stationarymanagement.data.local.model.stationery.VanPhongPham;
import com.ttn.stationarymanagement.data.local.model.test.TestDatabase;

import java.util.Date;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    // ---------------------------------------------------------------

    private Dao<TestDatabase, Long> testDao;
    private Dao<VaiTro, Long>  roleDao;
    private Dao<PhongBan, Long> departmentDao;
    private Dao<VanPhongPham, Long> productDao;
    private Dao<NhanVien, Long>  staftDao;


    // --------------------------------------------------------------
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {

            TableUtils.createTable(connectionSource, TestDatabase.class);
            TableUtils.createTable(connectionSource, VaiTro.class);
            TableUtils.createTable(connectionSource, PhongBan.class);
            TableUtils.createTable(connectionSource, VanPhongPham.class);
            TableUtils.createTable(connectionSource, NhanVien.class);


        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public <T>void cleanTable(Class<T> modelClass) throws java.sql.SQLException {
        try {
            TableUtils.clearTable(connectionSource, modelClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TableUtils.dropTable(connectionSource, modelClass, true);
        TableUtils.createTable(connectionSource, modelClass);

    }

    public Dao<TestDatabase, Long> getTestDao() throws java.sql.SQLException {
        if (testDao == null) {
            testDao = getDao(TestDatabase.class);
        }
        return testDao;
    }

    public Dao<VaiTro, Long> getRoleDao() throws java.sql.SQLException {
        if (roleDao == null) {
            roleDao = getDao(VaiTro.class);
        }
        return roleDao;
    }

    public Dao<PhongBan, Long> getDepartmentDao() throws java.sql.SQLException {
        if (departmentDao == null) {
            departmentDao = getDao(PhongBan.class);
        }
        return departmentDao;
    }

    public Dao<VanPhongPham, Long> getProductDao() throws java.sql.SQLException {
        if (productDao == null) {
            productDao = getDao(VanPhongPham.class);
        }
        return productDao;
    }

    public Dao<NhanVien, Long> getStaftDao() throws java.sql.SQLException  {
        if (staftDao == null) {
            staftDao = getDao(NhanVien.class);
        }
        return staftDao;
    }
}
