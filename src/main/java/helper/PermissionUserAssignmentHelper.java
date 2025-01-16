package helper;

import de.eventmanager.core.database.Communication.DatabaseConnector;
import de.eventmanager.core.permissions.Permission;
import de.eventmanager.core.users.User;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.jooq.generated.tables.Permission.PERMISSION;
import static org.jooq.generated.tables.Has.HAS;



/**
 * Helper class for managing the assignment of permissions to users.
 */
public class PermissionUserAssignmentHelper {

    private static boolean checkAdminUserAdminPermission(User user, Permission permission) {
        return user.isAdmin() && permission.isAdminPermission();
    }

    /**
     * Adds a permission to a user's permissions after checking if the user is allowed to have it.
     *
     * @param user the user to whom the permission will be added
     * @param permission the permission to be added
     * @return true if the permission was added successfully, false otherwise
     */
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
    /**
     * Adds a permission for a user to the database.
     *
     * @param permission the permission to be added
     * @param user the user to whom the permission will be added
     * @return true if the permission was added successfully, false otherwise
     */
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
            LoggerHelper.logErrorMessage(PermissionUserAssignmentHelper.class,
                    "Database error: " + e.getMessage());

            return false;
        }
    }

    /**
     * Retrieves the permissions for a user from the database.
     *
     * @param user the user whose permissions will be retrieved
     * @return a list of permissions for the user
     */
    public static Optional<List<Permission>> getPermissionsForUserFromDatabase(User user) {
        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            List<Permission> permissions = create.select(PERMISSION.fields())
                    .from(HAS)
                    .join(PERMISSION)
                    .on(HAS.PERMISSIONID.eq(PERMISSION.PERMISSIONID))
                    .where(HAS.USERID.eq(user.getUserID()))
                    .fetchInto(Permission.class);

            return Optional.of(new ArrayList<>(permissions));
        } catch (SQLException e) {
            LoggerHelper.logErrorMessage(PermissionUserAssignmentHelper.class,
                    "Database error while retrieving permissions: " + e.getMessage());

            return Optional.empty();
        }
    }
    //#endregoin database
}
