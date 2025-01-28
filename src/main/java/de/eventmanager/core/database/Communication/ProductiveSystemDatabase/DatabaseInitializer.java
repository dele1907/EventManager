package de.eventmanager.core.database.Communication.ProductiveSystemDatabase;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    //TODO: @Dennis try-catch + connection close
    public static void initialize(Connection connection) throws SQLException {
        initUserTable(connection);
        initEventsTable(connection);
        initCreatedTable(connection);
        initBookedTable(connection);
        initCitiesPostalCodeTable(connection);
    }

    private static void initUserTable(Connection connection) throws SQLException {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS user ("
                + " userID TEXT PRIMARY KEY,"
                + " firstName TEXT NOT NULL,"
                + " lastName TEXT NOT NULL,"
                + " birthDate TEXT NOT NULL,"
                + " eMail TEXT NOT NULL,"
                + " phoneNumber TEXT NOT NULL,"
                + " password TEXT NOT NULL,"
                + " isAdmin BOOLEAN NOT NULL"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsersTable);
        }
    }

    private static void initEventsTable(Connection connection) throws SQLException {
        String createEventsTable = "CREATE TABLE IF NOT EXISTS events ("
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

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createEventsTable);
        }
    }

    private static void initCreatedTable(Connection connection) throws SQLException {
        String createdEventsTable = "CREATE TABLE IF NOT EXISTS created ("
                + " userID TEXT NOT NULL,"
                + " eventID TEXT NOT NULL,"
                + " PRIMARY KEY (userID, eventID),"
                + " FOREIGN KEY (eventID) REFERENCES events(eventID),"
                + " FOREIGN KEY (userID) REFERENCES user(userID)"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createdEventsTable);
        }
    }

    private static void initBookedTable(Connection connection) throws SQLException {
        String bookedEventsTable = "CREATE TABLE IF NOT EXISTS booked ("
                + " userID TEXT NOT NULL,"
                + " eventID TEXT NOT NULL,"
                + " PRIMARY KEY (userID, eventID),"
                + " FOREIGN KEY (eventID) REFERENCES events(eventID),"
                + " FOREIGN KEY (userID) REFERENCES user(userID)"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(bookedEventsTable);
        }
    }

    private static void initCitiesPostalCodeTable(Connection connection) throws SQLException {
        String citiesPostalCodeTable = "CREATE TABLE IF NOT EXISTS cities ("
                + " postalCode TEXT PRIMARY KEY,"
                + " cityName TEXT NOT NULL"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(citiesPostalCodeTable);
        }
    }
}

