package EventManagementCore.UserManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import EventManagementCore.DatabaseCommunication.DatabaseConnector;
import EventManagementCore.UserManagement.User;
import Helper.LoggerHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserManager {

    private final String USER_ADDED = "User added successfully";
    private final String USER_NOT_ADDED = "Error adding user: ";
    private final String USER_NOT_READ = "Error reading user: ";
    private final String USER_UPDATED = "User updated successfully";
    private final String USER_NOT_UPDATED = "Error updating user: ";
    private final String USER_NOT_FOUND = "No user found with the given ID";
    private final String USER_DELETED = "User deleted successfully";
    private final String USER_NOT_DELETED = "Error deleting user: ";

    // Benutzer hinzufügen (CREATE)
    public boolean createNewUser(User user) {
        String sql = "INSERT INTO user (userID, firstName, lastName, birthDate, eMail, password, phoneNumber, isAdmin)"
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
            preparedStatement.setBoolean(8, user.isAdmin());

            preparedStatement.executeUpdate();
            LoggerHelper.logInfoMessage(UserManager.class, USER_ADDED);

            return true;

        } catch (SQLException e) {
            LoggerHelper.logErrorMessage(UserManager.class, USER_NOT_ADDED + e.getMessage());

            return false;
        }
    }

    // Benutzer laden (READ)
    public User readUserByID(String userID) {
        String sql = "SELECT * FROM user WHERE userID = ?";
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new User(
                        resultSet.getString("userID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("birthDate"),
                        resultSet.getString("eMail"),
                        resultSet.getString("password"),
                        resultSet.getInt("phoneNumber"),
                        resultSet.getBoolean("isAdmin")
                );
            }

        } catch (SQLException e) {
            LoggerHelper.logErrorMessage(UserManager.class, USER_NOT_READ + e.getMessage());
        }

        return null;
    }

    public User readUserByEMail(String eMailAddress) {
        String sql = "SELECT * FROM user WHERE eMail = ?";
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, eMailAddress);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new User(
                        resultSet.getString("userID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("birthDate"),
                        resultSet.getString("eMail"),
                        resultSet.getString("password"),
                        resultSet.getInt("phoneNumber"),
                        resultSet.getBoolean("isAdmin")
                );
            }

        } catch (SQLException e) {
            LoggerHelper.logErrorMessage(UserManager.class, USER_NOT_READ + e.getMessage());
        }

        return null;
    }

    // Benutzer ändern (UPDATE)
    public boolean updateUser(User user) {
        String sql = "UPDATE user SET firstName = ?, lastName = ?, birthDate = ?, eMail = ?, password = ?, phoneNumber = ? WHERE userID = ?";
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getDateOfBirth());
            preparedStatement.setString(4, user.getEMailAddress());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setInt(6, user.getPhoneNumber());

            preparedStatement.setString(7, user.getUserID()); // userID als WHERE-Bedingung

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                LoggerHelper.logInfoMessage(UserManager.class, USER_UPDATED);

                return true;
            } else {
                LoggerHelper.logInfoMessage(UserManager.class, USER_NOT_FOUND);

                return false;
            }

        } catch (SQLException e) {
            LoggerHelper.logErrorMessage(UserManager.class, USER_NOT_UPDATED + e.getMessage());

            return false;
        }
    }

    // Benutzer löschen (DELETE)
    public boolean deleteUserByID(String userID) {
        String sql = "DELETE FROM user WHERE userID = ?";
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                LoggerHelper.logInfoMessage(UserManager.class, USER_DELETED);
            } else {
                LoggerHelper.logInfoMessage(UserManager.class, USER_NOT_FOUND);
            }

            return rowsAffected > 0;

        } catch (SQLException e) {
            LoggerHelper.logErrorMessage(UserManager.class, USER_NOT_DELETED + e.getMessage());

            return false;
        }
    }

}
