package com.hepexta.jaxrs.util;

import com.hepexta.jaxrs.bank.ex.TransferException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {

    private static final String CONNECTION_URL = Utils.getStringProperty("database.connectionUrl");
    private static final String USER = Utils.getStringProperty("database.user");
    private static final String PASSWORD = Utils.getStringProperty("database.password");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
    }

    public static String getId(PreparedStatement stmt) throws SQLException {
        String result;
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                result = generatedKeys.getString(1);
            } else {
                throw new TransferException(TransferException.ERROR_INSERTING_ACCOUNT);
            }
        }
        return result;
    }

}
