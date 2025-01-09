package EventManagementCoreTests.DatabaseCommunicationTests;

import EventManagementCore.DatabaseCommunication.DatabaseConnector;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseConnectorTestDrive {

    // Testen auf erfolgreiche Verbindung
    @Test
    void testConnection() throws SQLException {
        Connection connection = DatabaseConnector.connect();
        assertNotNull(connection, "Connection should not be null");

        connection.close();
    }

}
