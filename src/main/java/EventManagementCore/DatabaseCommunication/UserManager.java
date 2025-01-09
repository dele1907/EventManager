package EventManagementCore.DatabaseCommunication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import EventManagementCore.UserManagement.User;

public class UserManager {

    // Benutzer hinzufügen (CREATE)
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (userID, firstName, lastName, dateOfBirth, eMailAddress, password, phoneNumber, isAdmin)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUserID());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getDateOfBirth());
            preparedStatement.setString(5, user.getEMailAddress());
            preparedStatement.setString(6, user.getPassword());
            preparedStatement.setInt(7, user.getPhoneNumber());
            preparedStatement.setString(8, String.valueOf(user.isAdmin()));

            preparedStatement.executeUpdate();
            System.out.println("User added successfully.");

            return true;

        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());

            return false;
        }
    }

    /*
    // Benutzer laden (READ)
    public User readUserByID(String userID) {
        String sql = "SELECT * FROM users WHERE userID = ?";
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new User(
                        resultSet.getString("userID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("dateOfBirth"),
                        resultSet.getString("eMailAddress"),
                        resultSet.getString("password"),
                        resultSet.getInt("phoneNumber"),
                        resultSet.getBoolean("isAdmin")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error reading user: " + e.getMessage());
        }

        return null;
    }
    */

    // Benutzer löschen (DELETE)
    public boolean deleteUserByID(String userID) {
        String sql = "DELETE FROM users WHERE userID = ?";
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userID);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("No user found to delete.");
            }

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());

            return false;
        }
    }

}
