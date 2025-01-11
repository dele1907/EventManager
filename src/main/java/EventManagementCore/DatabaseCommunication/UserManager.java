package EventManagementCore.DatabaseCommunication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import EventManagementCore.UserManagement.User;

public class UserManager {

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
            System.out.println("User added successfully.");

            return true;

        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());

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
            System.out.println("Error reading user: " + e.getMessage());
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
            System.out.println("Error reading user: " + e.getMessage());
        }

        return null;
    }

    //Todo @Laura updateUser-Methode funktioniert nicht

    // Benutzer ändern (UPDATE)
    public boolean updateUser(User user) {
        String sql = "UPDATE user SET firstName = ?, lastName = ?, birthDate = ?, eMail = ?, password = ?, phoneNumber = ?, isAdmin = ? WHERE userID = ?";
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getDateOfBirth());
            preparedStatement.setString(4, user.getEMailAddress());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setInt(6, user.getPhoneNumber());
            preparedStatement.setBoolean(7, user.isAdmin());

            preparedStatement.setString(8, user.getUserID()); // userID als WHERE-Bedingung

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User updated successfully.");

                return true;
            } else {
                System.out.println("No user found with the given ID.");

                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());

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
