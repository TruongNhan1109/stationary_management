package com.ttn.stationarymanagement.data.local.model.test;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "TestDataBase")
public class TestDatabase {

        @DatabaseField(id = true, columnName = "id", generatedId = true)
        private Integer id;

        @DatabaseField
        private String name;

}
