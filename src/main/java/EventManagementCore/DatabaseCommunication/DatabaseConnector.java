package EventManagementCore.DatabaseCommunication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String databaseUrl = "jdbc:sqlite:src/main/resources/eventmanager.sqlite";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(databaseUrl);
            System.out.println("Connection to SQLite established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

}
