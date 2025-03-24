package de.eventmanager.core.database.Communication.ProductiveSystemDatabase;


import de.eventmanager.core.database.Communication.DatabaseConnector;
import helper.LoggerHelper;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
            + " address TEXT NULL,"
            + " description TEXT NULL,"
            + " privateEvent BOOLEAN NOT NULL,"
            +  "minimumAge TEXT NULL"
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

    private final static String NOTIFICATIONS_TABLE_MODEL = "CREATE TABLE IF NOT EXISTS notifications ("
            + " notificationID TEXT PRIMARY KEY,"
            + " userID TEXT NOT NULL,"
            + " message TEXT NOT NULL,"
            + " markedAsRead BOOLEAN NOT NULL, "
            + " FOREIGN KEY (userID) REFERENCES user(userID)"
            +");";

    private static List<String> tableModels = List.of(
            USER_TABLE_MODEL,
            EVENTS_TABLE_MODEL,
            CREATED_TABLE_MODEL,
            BOOKED_TABLE_MODEL,
            CITIES_POSTAL_CODE_TABLE_MODEL,
            NOTIFICATIONS_TABLE_MODEL
    );
    //#endregion SQL statements

    //#region methods
    public static void initialize() {
        var connection = DatabaseConnector.connect();

        if (getDataBaseAlreadyInitialized()) {
            LoggerHelper.logInfoMessage(DatabaseInitializer.class,
                    "Database file already exists!" + "No need to initialize database.");
            return;
        }

       try {
           initDataBaseTables(connection);
           insertCitiesFromCSV(connection);
       } catch (SQLException e) {
           LoggerHelper.logErrorMessage(DatabaseInitializer.class,
                   "Error initializing database: " + e.getMessage());
       } finally {
              closeConnection(connection);
       }
    }

    private static boolean getDataBaseAlreadyInitialized() {
        var connection = DatabaseConnector.connect();

        try {
            var result = DSL.using(connection)
                    .fetch("SELECT name FROM sqlite_master WHERE type='table' AND name='user'");

            return !result.isEmpty();
        } catch (DataAccessException e) {
            LoggerHelper.logErrorMessage(DatabaseInitializer.class,
                    "Error checking database initialization: " +
                            e.getMessage());

            return false;
        } finally {
            closeConnection(connection);
        }
    }

    private static void initDataBaseTables(Connection connection) throws SQLException {
        var create = DSL.using(connection);

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

        var databasePath = DatabaseConnector.getDatabasePath();
        var databaseFile = new File(databasePath);

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

    //#region cities/postcode init from csv
    private static List<String[]> readCSV() {
        var records = new ArrayList<String[]>();

        try (var inputStream = DatabaseInitializer.class.getClassLoader().getResourceAsStream("Germany_postalcode_cityname.csv");
             var reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            reader.readLine(); // Erste Zeile überspringen (Header)

            while ((line = reader.readLine()) != null) {
                var values = line.split(",");
                records.add(new String[]{values[3], values[2]}); // plz und ort
            }
        } catch (IOException | NullPointerException e) {
            LoggerHelper.logErrorMessage(
                    DatabaseInitializer.class,
                    "Error reading CSV file: " + e.getMessage()
            );
        }

        return records;
    }


    private static void insertCitiesFromCSV(Connection connection) {
        var create = DSL.using(connection);
        var cities = readCSV();

        for (var city : cities) {
            try {
                create.insertInto(DSL.table("cities"),
                                DSL.field("postalCode"), DSL.field("cityName"))
                        .values(city[0], city[1])
                        .execute();
            } catch (DataAccessException e) {
                LoggerHelper.logErrorMessage(
                        DatabaseInitializer.class,
                        "Error inserting city: " + city[0] + ", " + city[1] + " - " + e.getMessage()
                );
            }
        }
    }
    //#endregion cities/postcode init from csv
}

