package EventManagementCore.PermissionRoleManagement;

import EventManagementCore.UserManagement.User;
import Helper.PermissionUserAssignmentHelper;

public class PermissionManager {

    public boolean getUserHasAdminPermissions(User user) {
        return addPermissionUserCanModifyAdminStatusOfUserToUsersPermissions(user) &&
                addPermissionDeleteUserToUsersPermissions(user) &&
                addPermissionCreateUserToUserToUsersPermissions(user) &&
                addPermissionEditUserToUserToUsersPermissions(user) &&
                addPermissionGetUserInformationByUserIDToUsersPermissions(user);
    }

    //#region admin only permissions
    public boolean addPermissionDeleteUserToUsersPermissions(User user) {
        //TODO @Dennis replace when Permissions are available in database
        Permission deleteUserPermission = new Permission("deleteUser", true);
        PermissionUserAssignmentHelper.addPermissionToUsersPermissions(user, deleteUserPermission);

        return user.getPermissions().contains(deleteUserPermission);
    }

    public boolean addPermissionCreateUserToUserToUsersPermissions(User user) {
        //TODO @Dennis replace when Permissions are available in database
        Permission createUserPermission = new Permission("createNewUser", true);
        PermissionUserAssignmentHelper.addPermissionToUsersPermissions(user, createUserPermission);

        return user.getPermissions().contains(createUserPermission);
    }

    public boolean addPermissionEditUserToUserToUsersPermissions(User user) {
        //TODO @Dennis replace when Permissions are available in database
        Permission editUserPermission = new Permission("editUser", true);
        PermissionUserAssignmentHelper.addPermissionToUsersPermissions(user, editUserPermission);

        return user.getPermissions().contains(editUserPermission);
    }

    public boolean addPermissionGetUserInformationByUserIDToUsersPermissions(User user) {
        //TODO @Dennis replace when Permissions are available in database
        Permission getUserInformationPermission = new Permission("getUserInformation", true);
        PermissionUserAssignmentHelper.addPermissionToUsersPermissions(user, getUserInformationPermission);

        return user.getPermissions().contains(getUserInformationPermission);
    }

    public boolean addPermissionUserCanModifyAdminStatusOfUserToUsersPermissions(User user) {
        //TODO @Dennis replace when Permissions are available in database
        return addPermissionUserCanGiveAdminStatusToUserToUsersPermissions(user) &&
                addPermissionUserCanRemoveAdminStatusFromUserToUsersPermissions(user);
    }

    private boolean addPermissionUserCanGiveAdminStatusToUserToUsersPermissions(User user) {
        //TODO @Dennis replace when Permissions are available in database
        Permission giveAdminStatusPermission = new Permission("giveAdminStatus", true);
        PermissionUserAssignmentHelper.addPermissionToUsersPermissions(user, giveAdminStatusPermission);

        return user.getPermissions().contains(giveAdminStatusPermission);
    }

    private boolean addPermissionUserCanRemoveAdminStatusFromUserToUsersPermissions(User user) {
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
