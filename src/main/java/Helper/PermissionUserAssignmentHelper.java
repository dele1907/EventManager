package Helper;

import EventManagementCore.DatabaseCommunication.DatabaseConnector;
import EventManagementCore.DatabaseCommunication.UserManager;
import EventManagementCore.PermissionRoleManagement.Permission;
import EventManagementCore.UserManagement.User;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.jooq.generated.tables.Permission.PERMISSION;
import static org.jooq.generated.tables.Has.HAS;




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

        try(
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

    public static ArrayList<Permission> getPermissionsForUserFromDatabase(User user) {
        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            List<Permission> permissions = create.select(PERMISSION.fields())
                    .from(HAS)
                    .join(PERMISSION)
                    .on(HAS.PERMISSIONID.eq(PERMISSION.PERMISSIONID))
                    .where(HAS.USERID.eq(user.getUserID()))
                    .fetchInto(Permission.class);

            return new ArrayList<>(permissions);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Datenbankfehler beim Abrufen der Berechtigungen.", e);
        }
    }


    //#endregoin database
}
