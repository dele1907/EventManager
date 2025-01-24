package de.eventmanager.core.database.Communication.ProductiveSystemDatabase;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize(Connection conn) throws SQLException {
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

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
        }
    }
}

