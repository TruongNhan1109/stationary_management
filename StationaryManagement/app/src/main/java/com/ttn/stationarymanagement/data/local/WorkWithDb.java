package com.ttn.stationarymanagement.data.local;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.ldc.projectmaster.data.local.interactor.TestDatabaseUseCase;
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

}
