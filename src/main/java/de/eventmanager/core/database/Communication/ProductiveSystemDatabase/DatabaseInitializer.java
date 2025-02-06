package de.eventmanager.core.database.Communication.ProductiveSystemDatabase;


import de.eventmanager.core.database.Communication.DatabaseConnector;
import helper.LoggerHelper;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseInitializer {

    //#region SQL statements

    private final static String USER_TABLE_MODEL = "CREATE TABLE IF NOT EXISTS user ("
            + " userID TEXT PRIMARY KEY,"
            + " firstName TEXT NOT NULL,"
            + " lastName TEXT NOT NULL,"
            + " birthDate TEXT NOT NULL,"
            + " eMail TEXT NOT NULL,"
            + " phoneNumber TEXT NOT NULL,"
            + " password TEXT NOT NULL,"
            + " isAdmin BOOLEAN NOT NULL"
            + ");";

    private final static String EVENTS_TABLE_MODEL = "CREATE TABLE IF NOT EXISTS events ("
            + " eventID TEXT PRIMARY KEY,"
            + " eventName TEXT NOT NULL,"
            + " eventStart TEXT NOT NULL,"
            + " eventEnd TEXT NOT NULL,"
            + " maximumCapacity TEXT NULL,"
            + " numberOfBookedUsersOnEvent TEXT NOT NULL,"
            + " category TEXT NOT NULL,"
            + " eventLocation TEXT NOT NULL,"
            + " postalCode TEXT NOT NULL,"
            + " address TEXT NOT NULL,"
            + " description TEXT NULL,"
            + " maxParticipants INTEGER NULL,"
            + " privateEvent BOOLEAN NOT NULL"
            + ");";

    private final static String CREATED_TABLE_MODEL = "CREATE TABLE IF NOT EXISTS created ("
            + " userID TEXT NOT NULL,"
            + " eventID TEXT NOT NULL,"
            + " PRIMARY KEY (userID, eventID),"
            + " FOREIGN KEY (eventID) REFERENCES events(eventID),"
            + " FOREIGN KEY (userID) REFERENCES user(userID)"
            + ");";

    private final static String BOOKED_TABLE_MODEL = "CREATE TABLE IF NOT EXISTS booked ("
            + " userID TEXT NOT NULL,"
            + " eventID TEXT NOT NULL,"
            + " PRIMARY KEY (userID, eventID),"
            + " FOREIGN KEY (eventID) REFERENCES events(eventID),"
            + " FOREIGN KEY (userID) REFERENCES user(userID)"
            + ");";

    private final static String CITIES_POSTAL_CODE_TABLE_MODEL =  "CREATE TABLE IF NOT EXISTS cities ("
            + " postalCode TEXT PRIMARY KEY,"
            + " cityName TEXT NOT NULL"
            + ");";

    private final static String NOTIFICATIONS_TABLE_MODEL = "CREATE TABLE IF NOT EXISTS cities ("
            + " notificationID TEXT PRIMARY KEY,"
            + " userID TEXT NOT NULL,"
            + " message TEXT NOT NULL,"
            + " FOREIGN KEY (userID) REFERENCES user(userID)"
            +");";

    private static List<String> tableModels = List.of(
            USER_TABLE_MODEL,
            EVENTS_TABLE_MODEL,
            CREATED_TABLE_MODEL,
            BOOKED_TABLE_MODEL,
            CITIES_POSTAL_CODE_TABLE_MODEL,
            NOTIFICATIONS_TABLE_MODEL);

    //#endregion SQL statements

    //#region methods

    public static void initialize() {
        Connection connection = DatabaseConnector.connect();
       try {
           initDataBaseTables(connection);
       } catch (SQLException e) {
           LoggerHelper.logErrorMessage(DatabaseInitializer.class,
                   "Error initializing database: " + e.getMessage());
       } finally {
              closeConnection(connection);
       }
    }

    private static void initDataBaseTables(Connection connection) throws SQLException {
        DSLContext create = DSL.using(connection);
        tableModels.forEach(tableModel -> {
            try {
                create.execute(tableModel);
            } catch (DataAccessException e) {
                LoggerHelper.logErrorMessage(DatabaseInitializer.class,
                        "Error initializing database with statement: " +
                                tableModel + " - " + e.getMessage());
            }
        });
    }

    private static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LoggerHelper.logErrorMessage(DatabaseInitializer.class,
                        "Error closing connection: " + e.getMessage());
            }
        }
    }

    //INFO: for testing purposes only
    public static void deInit(boolean isProductiveSystem) {
        if (!isProductiveSystem) {
            return;
        }

        String databasePath = DatabaseConnector.getDatabasePath();
        File databaseFile = new File(databasePath);

        if (databaseFile.exists()) {
            if (databaseFile.delete()) {
                LoggerHelper.logInfoMessage(DatabaseInitializer.class,
                        "Database file deleted successfully at: " +
                        databasePath);
            } else {
                LoggerHelper.logErrorMessage(DatabaseInitializer.class, "Failed to delete the database file.");
            }
        } else {
            LoggerHelper.logInfoMessage(DatabaseInitializer.class, "Database file does not exist.");
        }
    }

    //#endregion methods

}

