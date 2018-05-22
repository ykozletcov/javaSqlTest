package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQL_Singleton {
    private static SQL_Singleton instance;
    private static Connection DBConnection;

    private SQL_Singleton() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            IO_Ops.ClassNotFoundException();
        }
        try {
            DBConnection = DriverManager.getConnection("jdbc:sqlite:musicStoreDB.db");
        } catch (SQLException E) {
            IO_Ops.SQLExceptionMessage();
        }
    }

    public static SQL_Singleton getInstance() {
        if (instance == null)
            instance = new SQL_Singleton();
        return instance;
    }

    public static Connection getDBConnection() {
        return DBConnection;
    }
}
