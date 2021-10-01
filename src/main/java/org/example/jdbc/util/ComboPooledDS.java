package org.example.jdbc.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;

public class ComboPooledDS {

    private static ComboPooledDataSource dataSource;

    private ComboPooledDS() {
    }

    public static synchronized ComboPooledDataSource getDatasource() {
        if (dataSource == null) {
            dataSource = new ComboPooledDataSource();
            Settings settings = new Settings();
            try {
                dataSource.setDriverClass(settings.getValue("driver"));
                dataSource.setJdbcUrl(settings.getValue("url"));
                dataSource.setUser(settings.getValue("username"));
                dataSource.setPassword(settings.getValue("password"));
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
        }
        return dataSource;
    }
}
