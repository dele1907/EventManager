package EventManagementCore.PermissionRoleManagement;

import EventManagementCore.DatabaseCommunication.DatabaseConnector;
import EventManagementCore.UserManagement.UserManager;
import EventManagementCore.UserManagement.User;
import Helper.ConfigurationDataSupplierHelper;
import Helper.LoggerHelper;
import Helper.PermissionUserAssignmentHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;

import static org.jooq.generated.tables.Permission.PERMISSION;

public class PermissionManager {

    private UserManager userManager = new UserManager();
    PermissionUserAssignmentHelper permissionUserAssignmentHelper = new PermissionUserAssignmentHelper();

    private Permission deleUserPermission = getPermissionFromDatabaseByPermissionID(
            ConfigurationDataSupplierHelper.getDeleteUserPermissionID()
    );
    private Permission createUserPermission = getPermissionFromDatabaseByPermissionID(
            ConfigurationDataSupplierHelper.getCreateUserPermissionID()
    );
    private Permission editUserPermission = getPermissionFromDatabaseByPermissionID(
            ConfigurationDataSupplierHelper.getEditUserPermissionID()
    );
    private Permission getUserInformationPermission = getPermissionFromDatabaseByPermissionID(
            ConfigurationDataSupplierHelper.getGetUserInformationPermissionID()
    );
    private Permission giveAdminStatusPermission = getPermissionFromDatabaseByPermissionID(
            ConfigurationDataSupplierHelper.getGiveUserAdminStatusPermissionID()
    );
    private Permission removeAdminStatusPermission = getPermissionFromDatabaseByPermissionID(
            ConfigurationDataSupplierHelper.getRemoveUserAdminStatusPermissionID()
    );

    //#region admin only permissions
    public void addPermissionDeleteUserToUsersPermissions(User user) {
        permissionUserAssignmentHelper.addPermissionToUsersPermissions(user, deleUserPermission);
    }

    public void addPermissionCreateUserToUserToUsersPermissions(User user) {
        permissionUserAssignmentHelper.addPermissionToUsersPermissions(user, createUserPermission);
    }

    public void addPermissionEditUserToUserToUsersPermissions(User user) {
        permissionUserAssignmentHelper.addPermissionToUsersPermissions(user, editUserPermission);
    }

    public void addPermissionGetUserInformationByUserIDToUsersPermissions(User user) {
        permissionUserAssignmentHelper.addPermissionToUsersPermissions(user, getUserInformationPermission);
    }

    public void addPermissionUserCanModifyAdminStatusOfUserToUsersPermissions(User user) {
        addPermissionUserCanGiveAdminStatusToUserToUsersPermissions(user);
        addPermissionUserCanRemoveAdminStatusFromUserToUsersPermissions(user);
    }

    private void addPermissionUserCanGiveAdminStatusToUserToUsersPermissions(User user) {
        permissionUserAssignmentHelper.addPermissionToUsersPermissions(user, giveAdminStatusPermission);
    }

    private void addPermissionUserCanRemoveAdminStatusFromUserToUsersPermissions(User user) {
        permissionUserAssignmentHelper.addPermissionToUsersPermissions(user, removeAdminStatusPermission);
    }
    //#endregion admin only permissions

    //#region general user permissions
    //#endregion general user permissions

    //#region event owner (& admin) only permissions
    //#endregion event owner (& admin) permissions

    //#region user related to certain event (& admin) only permissions
    //#endregion user related to certain event (& admin) only permissions

    //#region database
    public static Permission getPermissionFromDatabaseByPermissionID(String permissionID) {
        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Record record =  create.select()
                    .from(PERMISSION)
                    .where(PERMISSION.PERMISSIONID.eq(permissionID))
                    .fetchOne();

            if (record != null) {

                return new Permission(
                        record.get(PERMISSION.PERMISSIONID),
                        record.get(PERMISSION.PERMISSIONNAME),
                        record.get(PERMISSION.ISADMINPERMISSION)
                );
            }
        }

        catch (SQLException exception) {
            LoggerHelper.logErrorMessage(PermissionManager.class, exception.getMessage());
        }

        return null;
    }
    //#endregion database
    
    //#region getter
    
    public Permission getDeleUserPermission() {
        return deleUserPermission;
    }

    public Permission getCreateUserPermission() {
        return createUserPermission;
    }

    public Permission getEditUserPermission() {
        return editUserPermission;
    }

    public Permission getGetUserInformationPermission() {
        return getUserInformationPermission;
    }

    public Permission getGiveAdminStatusPermission() {
        return giveAdminStatusPermission;
    }

    public Permission getRemoveAdminStatusPermission() {
        return removeAdminStatusPermission;
    }
    //#endregion getter
}
