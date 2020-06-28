package com.ttn.stationarymanagement.data.config;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.ttn.stationarymanagement.data.local.model.test.TestDatabase;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

// https://ormlite.com/javadoc/ormlite-core/doc-files/ormlite.html

public class OrmliteDatabaseConfigUtil extends OrmLiteConfigUtil {

    public static final Class<?>[] classes = new Class[] {
        TestDatabase.class,
    };

    public static void main(String[] args) throws IOException, SQLException {

        String curDirectory = "user.dir";
        String configPath = "/app/src/main/res/raw/ormlite_config.txt";
        String projectRoot = System.getProperty(curDirectory);
        String fullConfigPath = projectRoot + configPath;
        File configFile = new File(fullConfigPath);

        if (configFile.exists()) {
            configFile.delete();
            configFile = new File(fullConfigPath);
        }

        writeConfigFile(configFile, classes);
    }
}
