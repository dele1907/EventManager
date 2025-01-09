package EventManagementCore.PermissionRoleManagement;

import EventManagementCore.UserManagement.User;
import Helper.PermissionUserAssignmentHelper;

public class PermissionManager {

    public boolean userCanHavePermissionDeleteUser(User user) {
        //todo @Dennis replace when Permissions are available in database
        Permission deleteUserPermission = new Permission("Delete User", true);

        PermissionUserAssignmentHelper.addPermissionToUsersPermissions(user, deleteUserPermission);

        return user.getPermissions().contains(deleteUserPermission);
    }

    public boolean userCanHavePermissionCreateUser(User user) {
        //todo @Dennis replace when Permissions are available in database
        Permission createUserPermission = new Permission("Create User", true);

        PermissionUserAssignmentHelper.addPermissionToUsersPermissions(user, createUserPermission);

        return user.getPermissions().contains(createUserPermission);
    }
}
