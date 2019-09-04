package com.hepexta.jaxrs.util;

import com.hepexta.jaxrs.bank.ex.TransferException;
import org.h2.Driver;
import org.h2.tools.RunScript;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {

    private static final String CONNECTION_URL = Utils.getStringProperty("database.connectionUrl");
    private static final String USER = Utils.getStringProperty("database.user");
    private static final String PASSWORD = Utils.getStringProperty("database.password");

    static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
    }

    public static void dataBaseInit(){
        try (Connection conn = getConnection()) {
            RunScript.execute(conn, new InputStreamReader(DBUtils.class.getResourceAsStream("/database.sql")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void dataBasePopulate(){
        try (Connection conn = getConnection()) {
            RunScript.execute(conn, new InputStreamReader(DBUtils.class.getResourceAsStream("/database_populate.sql")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
