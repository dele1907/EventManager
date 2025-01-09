package Helper;

import EventManagementCore.PermissionRoleManagement.Permission;
import EventManagementCore.UserManagement.User;

public class PermissionHelper {
    private static boolean checkAdminUserAdminPermission(User user, Permission permission) {

        return user.isAdmin() && permission.isAdminPermission();
    }

    public static boolean addPermissionToUsersPermissions(User user, Permission permission) {
        if (checkAdminUserAdminPermission(user, permission)) {
            user.addPermissionToUser(permission);

            return true;
        }

        return false;
    }
}
