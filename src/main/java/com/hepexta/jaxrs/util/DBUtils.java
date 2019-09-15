package com.hepexta.jaxrs.util;

import com.hepexta.jaxrs.bank.ex.TransferException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.h2.tools.RunScript;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {

    private static final String CONNECTION_URL = Utils.getStringProperty("database.connectionUrl");
    private static final String USER = Utils.getStringProperty("database.user");
    private static final String PASSWORD = Utils.getStringProperty("database.password");

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        config.setJdbcUrl(CONNECTION_URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource( config );
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
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
