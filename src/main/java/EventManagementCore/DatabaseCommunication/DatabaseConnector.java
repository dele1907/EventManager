package EventManagementCore.DatabaseCommunication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String databaseUrl = "jdbc:sqlite:src/main/resources/eventmanager.sqlite";

    private static final String CONNECTION_ESTABLISHED = "Connection to SQLite established";
    private static final String CONNECTION_NOT_ESTABLISHED = "Error establishing connection: ";

    static Logger logger = LogManager.getLogger(DatabaseConnector.class);

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(databaseUrl);
            logger.info(CONNECTION_ESTABLISHED);
        } catch (SQLException e) {
            logger.error(CONNECTION_NOT_ESTABLISHED + e.getMessage());
        }
        return connection;
    }

}
