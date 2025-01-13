package Helper;

import EventManagementCore.DatabaseCommunication.DatabaseConnector;
import EventManagementCore.UserManagement.UserManager;
import EventManagementCore.PermissionRoleManagement.Permission;
import EventManagementCore.UserManagement.User;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.Connection;
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
     * method to add an admin-checked permission to an user
     * */
    public boolean addPermissionToUsersPermissions(User user, Permission permission) {
        if (checkAdminUserAdminPermission(user, permission) || !permission.isAdminPermission()) {
            user.addPermissionToOwnUser(permission);
            /**
             * commented out so not every test related to this method will add an entry to the database
             * */
            //addPermissionForUserToDatabase(permission, user);
            return true;
        }

        return false;
    }

    //#regoin database
    private boolean addPermissionForUserToDatabase(Permission permission, User user) {
        try (Connection connection = DatabaseConnector.connect()) {
            DSLContext create = DSL.using(connection);

            int result = create.insertInto(
                            HAS,
                            HAS.USERID,
                            HAS.PERMISSIONID
                    )
                        .values(user.getUserID(), permission.getPermissionID())
                        .execute();

            if (result > 0) {
                LoggerHelper.logInfoMessage(PermissionUserAssignmentHelper.class,
                        "Permission " +
                                permission.getPermissionName() +
                                " added successfully for user " +
                                user.getFirstName()
                );

                return true;
            }

            LoggerHelper.logErrorMessage(
                    PermissionUserAssignmentHelper.class,
                    "Failed to add permission for user " + user.getFirstName()
            );

            return false;
        } catch (Exception e) {
            LoggerHelper.logErrorMessage(PermissionUserAssignmentHelper.class, e.getMessage());

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
