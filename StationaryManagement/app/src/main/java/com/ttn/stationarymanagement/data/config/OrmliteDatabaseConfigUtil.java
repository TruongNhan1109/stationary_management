package com.ttn.stationarymanagement.data.config;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.ttn.stationarymanagement.data.local.model.CapPhat;
import com.ttn.stationarymanagement.data.local.model.NhanVien;
import com.ttn.stationarymanagement.data.local.model.PhongBan;
import com.ttn.stationarymanagement.data.local.model.VaiTro;
import com.ttn.stationarymanagement.data.local.model.VanPhongPham;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

// https://ormlite.com/javadoc/ormlite-core/doc-files/ormlite.html

public class OrmliteDatabaseConfigUtil extends OrmLiteConfigUtil {

    public static final Class<?>[] classes = new Class[] {
            VaiTro.class,
            PhongBan.class,
            VanPhongPham.class,
            NhanVien.class,
            CapPhat.class,

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
