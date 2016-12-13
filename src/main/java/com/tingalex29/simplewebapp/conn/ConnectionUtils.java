package com.tingalex29.simplewebapp.conn;
import java.sql.Connection;
import java.sql.SQLException;
/**
 * Created by Administrator on 11/18/2016.
 */
public class ConnectionUtils {
    public static Connection getConnection()
            throws ClassNotFoundException, SQLException {

        // Here I using Oracle Database.
        return MySQLConnUtils.getMySQLConnection();

        // return MySQLConnUtils.getMySQLConnection();
        // return SQLServerConnUtils_JTDS.getSQLServerConnection_JTDS();
        // return SQLServerConnUtils_SQLJDBC.getSQLServerConnection_SQLJDBC();
    }

    public static void closeQuietly(Connection conn) {
        try {
            conn.close();
        } catch (Exception e) {
        }
    }

    public static void rollbackQuietly(Connection conn) {
        try {
            conn.rollback();
        } catch (Exception e) {
        }
    }
}
