package com.ttn.stationarymanagement.data.local;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.ttn.stationarymanagement.data.local.interactor.RoleUseCase;
import com.ttn.stationarymanagement.data.local.interactor.TestDatabaseUseCase;
import com.ttn.stationarymanagement.data.local.model.VaiTro;
import com.ttn.stationarymanagement.data.local.model.test.TestDatabase;
import com.ttn.stationarymanagement.presentation.baseview.MyApp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkWithDb {

    private static DatabaseHelper databaseHelper = null;
    private static final String TAG = WorkWithDb.class.getSimpleName();
    private static WorkWithDb workWithDb = null;
    private Context mContext;

    public static WorkWithDb getInstance(){
        if(workWithDb == null){
            workWithDb = new WorkWithDb();
        }
        return workWithDb;
    }

    public WorkWithDb(){
        mContext = MyApp.getAppContext();
        databaseHelper = OpenHelperManager.getHelper(mContext, DatabaseHelper.class);
    }

    public void insert(TestDatabase test) {
        try {
            TestDatabaseUseCase.create(databaseHelper.getTestDao(), test);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(TestDatabase test) {
        try {
            TestDatabaseUseCase.update(databaseHelper.getTestDao(), test);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int delete(TestDatabase test) {
        try {
            return TestDatabaseUseCase.delete(databaseHelper.getTestDao(), test);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public TestDatabase getCardRankById(long id) {
        TestDatabase testDatabase = new TestDatabase();
        try {
            testDatabase = TestDatabaseUseCase.getById(databaseHelper.getTestDao(), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return testDatabase;

    }

    public List<TestDatabase> getAllCardRank() {
        try {
            List<TestDatabase> card_rank = TestDatabaseUseCase.getAll(databaseHelper.getTestDao());
            return card_rank;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    // VaiTro Table --------------------------------------------------------------------------------

    public boolean insert(VaiTro role) {
        try {
           return  RoleUseCase.create(databaseHelper.getRoleDao(), role) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }

    public boolean update(VaiTro role) {
        try {
            RoleUseCase.update(databaseHelper.getRoleDao(), role);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(VaiTro role) {
        try {
            RoleUseCase.delete(databaseHelper.getRoleDao(), role);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public VaiTro getRoleById(long id) {
        VaiTro role = new VaiTro();
        try {
            role = RoleUseCase.getById(databaseHelper.getRoleDao(), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;

    }

    public List<VaiTro> getAllRole() {
        try {
            List<VaiTro> list = RoleUseCase.getAll(databaseHelper.getRoleDao());
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Department Table -----------------------------------------------------



}
