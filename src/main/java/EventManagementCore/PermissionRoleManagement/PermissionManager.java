package EventManagementCore.PermissionRoleManagement;

import EventManagementCore.UserManagement.User;
import Helper.PermissionHelper;

public class PermissionManager {
    private User notAnAdminDummyUser;

    public PermissionManager() {
        this.notAnAdminDummyUser = new User();
    }

    public boolean userCanHavePermissionDeleteUser(User user) {
        Permission deleteUserPermission = new Permission("Delete User", true);

        PermissionHelper.addPermissionToUsersPermissions(user, deleteUserPermission);

        return user.getPermissions().contains(deleteUserPermission);
    }

    /**public boolean hasPermissionAddUserFor(DummyUser dummyUser) {
        return dummyUser.getIsAdmin();
    }

    public boolean hasPermissionEditUser(DummyUser dummyUser) {
        return dummyUser.getIsAdmin();
    }*/
}
