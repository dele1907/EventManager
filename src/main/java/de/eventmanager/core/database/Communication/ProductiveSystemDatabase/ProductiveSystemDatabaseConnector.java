package de.eventmanager.core.database.Communication.ProductiveSystemDatabase;

import de.eventmanager.core.database.Communication.DatabaseConnector;
import de.eventmanager.core.presentation.EventManagerTextBasedUIInstance;
import helper.LoggerHelper;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductiveSystemDatabaseConnector {
    private static String databasePath = "";
    private static final String PATH_FILE = "database_path.txt";
    private static final String CONNECTION_NOT_ESTABLISHED = "Error establishing connection: ";

    public static String getDatabasePath() {
        return databasePath;
    }

    public static void setDatabasePath(String path) {
        databasePath = path + "/test.sqlite";
    }

    public static void saveDatabasePath(String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_FILE))) {
            writer.write(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadDatabasePath() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_FILE))) {
            databasePath = reader.readLine();
            if (databasePath == null) {
                databasePath = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
        } catch (SQLException e) {
            LoggerHelper.logErrorMessage(DatabaseConnector.class, CONNECTION_NOT_ESTABLISHED + e.getMessage());
        }
        return connection;
    }

    public static void initDatabase() {
        String databasePath = ProductiveSystemDatabaseConnector.getDatabasePath();
        String url = "jdbc:sqlite:" + databasePath;

        try (Connection conn = ProductiveSystemDatabaseConnector.connect()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();

                // Create Users table
                String createUsersTable = "CREATE TABLE IF NOT EXISTS user (\n"
                        + " userID TEXT PRIMARY KEY,\n"
                        + " firstName TEXT NOT NULL,\n"
                        + " lastName TEXT NOT NULL,\n"
                        + "birthDate TEXT NOT NULL,\n"
                        + " eMail TEXT NOT NULL,\n"
                        + "phoneNumber TEXT NOT NULL,\n"
                        + " password TEXT NOT NULL,\n"
                        + " isAdmin BOOLEAN NOT NULL\n"
                        + ");";
                stmt.execute(createUsersTable);

                LoggerHelper.logInfoMessage(EventManagerTextBasedUIInstance.class, "Database initialized at: " + databasePath);
            }
        } catch (SQLException e) {
            LoggerHelper.logErrorMessage(EventManagerTextBasedUIInstance.class, "Failed to initialize database: " + e.getMessage());
        }
    }
}
