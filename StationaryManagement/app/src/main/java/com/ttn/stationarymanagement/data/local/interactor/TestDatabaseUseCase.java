package com.ldc.projectmaster.data.local.interactor;

import com.j256.ormlite.dao.Dao;
import com.ldc.projectmaster.data.local.model.test.TestDatabase;

import java.sql.SQLException;
import java.util.List;

public class TestDatabaseUseCase  {

    public static int create(final Dao<TestDatabase, Long> dao, TestDatabase test) throws SQLException {
        return dao.create(test);
    }

    public static int update(final Dao<TestDatabase, Long> dao, TestDatabase card_rank)throws SQLException{
        return dao.update(card_rank);
    }

    public static int delete(final Dao<TestDatabase, Long> dao, TestDatabase card_rank)throws SQLException{
        return dao.delete(card_rank);
    }


    public static TestDatabase getById(final Dao<TestDatabase, Long> dao, long idCard)throws SQLException{
        return dao.queryForId(idCard);
    }

    public static List<TestDatabase> getAll(final Dao<TestDatabase, Long> dao) throws SQLException{
        return dao.queryForAll();
    }
}
