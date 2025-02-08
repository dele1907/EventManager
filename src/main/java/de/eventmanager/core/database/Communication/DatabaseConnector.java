package de.eventmanager.core.database.Communication;

import de.eventmanager.core.database.Communication.ProductiveSystemDatabase.DatabasePathManager;
import helper.ConfigurationDataSupplierHelper;
import helper.LoggerHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String JDBC_URL_PREFIX = "jdbc:sqlite:";
    private static String databasePath = ConfigurationDataSupplierHelper.IS_PRODUCTION_MODE ?
            "" :
            "src/main/resources/eventmanager.sqlite";

    private DatabaseConnector() {}

    public static void setDatabasePath(String path) {
        databasePath = path;
    }

    public static String getDatabasePath() {
        return databasePath;
    }

    public static Connection connect() {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL_PREFIX + databasePath);

            if (connection == null) {
                LoggerHelper.logErrorMessage(DatabaseConnector.class, "Database connection is null!");
            }

            return connection;
        } catch (SQLException e) {
            LoggerHelper.logErrorMessage(DatabaseConnector.class, "Error connecting to database: " + e.getMessage());

            return null;
        }
    }

}
