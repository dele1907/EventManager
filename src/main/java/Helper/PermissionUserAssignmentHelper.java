package Helper;

import EventManagementCore.PermissionRoleManagement.Permission;
import EventManagementCore.UserManagement.User;

public class PermissionUserAssignmentHelper {

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
    public static boolean addPermissionToUsersPermissions(User user, Permission permission) {
        if (checkAdminUserAdminPermission(user, permission)) {
            user.addPermissionToUser(permission);

            return true;
        }

        return false;
    }
}
