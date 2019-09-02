package com.hepexta.jaxrs.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBUtils {

    private static final String CONNECTION_URL = Utils.getStringProperty("database.connectionUrl");
    private static final String USER = Utils.getStringProperty("database.user");
    private static final String PASSWORD = Utils.getStringProperty("database.password");

      public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
    }

}
