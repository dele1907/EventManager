package de.eventmanager.core.database.Communication;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectorTestDrive {

    // Testen auf erfolgreiche Verbindung
    @Test
    void testConnection() throws SQLException {

        Connection connection = DatabaseConnector.connect();
        assertNotNull(connection, "Connection should not be null");

        connection.close();
    }

}
