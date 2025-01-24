package de.eventmanager.core.database.Communication;

import helper.LoggerHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static String databasePath = "";

    public static void setDatabasePath(String path) {
        databasePath = path;
    }

    public static String getDatabasePath() {
        return databasePath;
    }

    public static Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + databasePath);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
