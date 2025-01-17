package de.eventmanager.core.database.Communication;

import helper.LoggerHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String databaseUrl = "jdbc:sqlite:src/main/resources/eventmanager.sqlite";

    private static final String CONNECTION_ESTABLISHED = "Connection to SQLite established";
    private static final String CONNECTION_NOT_ESTABLISHED = "Error establishing connection: ";

    public static Connection connect() {

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(databaseUrl);
            LoggerHelper.logInfoMessage(DatabaseConnector.class, CONNECTION_ESTABLISHED);
        } catch (SQLException e) {
            LoggerHelper.logErrorMessage(DatabaseConnector.class, CONNECTION_NOT_ESTABLISHED + e.getMessage());
        }

        return connection;
    }

}
