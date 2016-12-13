package com.tingalex29.simplewebapp.conn;
import java.sql.*;
/**
 * Created by Administrator on 11/18/2016.
 */
public class MySQLConnUtils {
    public static Connection getMySQLConnection()
            throws ClassNotFoundException, SQLException {

        // Note: Change the connection parameters accordingly.
        String hostName = "127.0.0.1";
        String dbName = "cookbook";
        String userName = "root";
        String password = "windows10";
        return getMySQLConnection(hostName, dbName, userName, password);
    }

    public static Connection getMySQLConnection(String hostName, String dbName,
                                                String userName, String password) throws SQLException,
            ClassNotFoundException {

        // Declare the class Driver for MySQL DB
        // This is necessary with Java 5 (or older)
        // Java6 (or newer) automatically find the appropriate driver.
        // If you use Java> 5, then this line is not needed.
        Class.forName("com.mysql.cj.jdbc.Driver");
        // URL Connection for MySQL
        // Example: jdbc:mysql://localhost:3306/simplehr
        String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName
                +"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC" +
                "&autoReconnect=true&useSSL=false";
        return  DriverManager.getConnection(connectionURL, userName,
                password);
        //modified
    }
}
