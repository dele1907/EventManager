package Helper;

import EventManagementCore.DatabaseCommunication.DatabaseConnector;
import EventManagementCore.DatabaseCommunication.UserManager;
import EventManagementCore.PermissionRoleManagement.Permission;
import EventManagementCore.UserManagement.User;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PermissionUserAssignmentHelper {
    UserManager userManager = new UserManager();

    /**
     * method which checks if the user is an admin and the permission is an admin related permission
     * so a non admin user can not gain an admin-only permission
     * */
    private static boolean checkAdminUserAdminPermission(User user, Permission permission) {

        return user.isAdmin() && permission.isAdminPermission();
    }

    /**
     * method to add a admin-checked permission to an user
     * */
    public boolean addPermissionToUsersPermissions(User user, Permission permission) {
        if (checkAdminUserAdminPermission(user, permission) || !permission.isAdminPermission()) {
            user.addPermissionToOwnUser(permission);
            /**
             * commented out so not every test related to this method will add an etry to the database
             * */
            //addPermissionForUserToDatabase(permission, user);
            return true;
        }

        return false;
    }

    //#regoin database
    private boolean addPermissionForUserToDatabase(Permission permission, User user) {
        user = userManager.readUserByID(user.getUserID());

        String sql = "INSERT INTO has (userID, permissionID) VALUES (?, ?)";

        try (
                Connection connection = DatabaseConnector.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ){
            preparedStatement.setString(1, user.getUserID());
            preparedStatement.setString(2, permission.getPermissionID());

            preparedStatement.executeUpdate();
            System.out.println("Permission" + permission.getPermissionName() + "added successfully for user " + user.getFirstName());

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return false;
        }
    }
    //#endregoin database
}
