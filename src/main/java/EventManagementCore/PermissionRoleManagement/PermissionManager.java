package EventManagementCore.PermissionRoleManagement;

import EventManagementCore.UserManagement.User;
import Helper.PermissionUserAssignmentHelper;

public class PermissionManager {

    public boolean getUserHasAdminPermissions(User user) {
        return getUserCanModifyAdminStatusOfUser(user) &&
                userCanHavePermissionDeleteUser(user) &&
                userCanHavePermissionCreateUser(user) &&
                userCanHavePermissionEditUser(user) &&
                getGetUserInformationByUserID(user);
    }

    //#region admin only permissions
    public boolean userCanHavePermissionDeleteUser(User user) {
        //TODO @Dennis replace when Permissions are available in database
        Permission deleteUserPermission = new Permission("deleteUser", true);
        PermissionUserAssignmentHelper.addPermissionToUsersPermissions(user, deleteUserPermission);

        return user.getPermissions().contains(deleteUserPermission);
    }

    public boolean userCanHavePermissionCreateUser(User user) {
        //TODO @Dennis replace when Permissions are available in database
        Permission createUserPermission = new Permission("createNewUser", true);
        PermissionUserAssignmentHelper.addPermissionToUsersPermissions(user, createUserPermission);

        return user.getPermissions().contains(createUserPermission);
    }

    public boolean userCanHavePermissionEditUser(User user) {
        //TODO @Dennis replace when Permissions are available in database
        Permission editUserPermission = new Permission("editUser", true);
        PermissionUserAssignmentHelper.addPermissionToUsersPermissions(user, editUserPermission);

        return user.getPermissions().contains(editUserPermission);
    }

    public boolean getGetUserInformationByUserID(User user) {
        //TODO @Dennis replace when Permissions are available in database
        Permission getUserInformationPermission = new Permission("getUserInformation", true);
        PermissionUserAssignmentHelper.addPermissionToUsersPermissions(user, getUserInformationPermission);

        return user.getPermissions().contains(getUserInformationPermission);
    }

    public boolean getUserCanModifyAdminStatusOfUser(User user) {
        //TODO @Dennis replace when Permissions are available in database
        return getUserCanGiveAdminStatusToUser(user) && getUserCanRemoveAdminStatusFromUser(user);
    }

    private boolean getUserCanGiveAdminStatusToUser(User user) {
        //TODO @Dennis replace when Permissions are available in database
        Permission giveAdminStatusPermission = new Permission("giveAdminStatus", true);
        PermissionUserAssignmentHelper.addPermissionToUsersPermissions(user, giveAdminStatusPermission);

        return user.getPermissions().contains(giveAdminStatusPermission);
    }

    private boolean getUserCanRemoveAdminStatusFromUser(User user) {
        //TODO @Dennis replace when Permissions are available in database
        Permission removeAdminStatusPermission = new Permission("removeAdminStatus", true);
        PermissionUserAssignmentHelper.addPermissionToUsersPermissions(user, removeAdminStatusPermission);

        return user.getPermissions().contains(removeAdminStatusPermission);
    }
    //#endregion admin only permissions

    //#region general user permissions
    //#endregion general user permissions

    //#region event owner (& admin) only permissions
    //#endregion event owner (& admin) permissions

    //#region user related to certain event (& admin) only permissions
    //#endregion user related to certain event (& admin) only permissions
}
