package EventManagementCore.PermissionRoleManagement;

import EventManagementCore.DatabaseCommunication.DatabaseConnector;
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
    PermissionUserAssignmentHelper permissionUserAssignmentHelper = new PermissionUserAssignmentHelper();

    //#region permission variables
    private final Permission DELETE_USER_PERMISSION = getPermissionFromDatabaseByPermissionID(
            ConfigurationDataSupplierHelper.getDeleteUserPermissionID()
    );
    private final Permission CREATE_USER_PERMISSION = getPermissionFromDatabaseByPermissionID(
            ConfigurationDataSupplierHelper.getCreateUserPermissionID()
    );
    private final Permission EDIT_USER_PERMISSION = getPermissionFromDatabaseByPermissionID(
            ConfigurationDataSupplierHelper.getEditUserPermissionID()
    );
    private final Permission GET_USER_INFORMATION_PERMISSION = getPermissionFromDatabaseByPermissionID(
            ConfigurationDataSupplierHelper.getGetUserInformationPermissionID()
    );
    private final Permission GIVE_ADMIN_STATUS_PERMISSION = getPermissionFromDatabaseByPermissionID(
            ConfigurationDataSupplierHelper.getGiveUserAdminStatusPermissionID()
    );
    private final Permission REMOVE_ADMIN_STATUS_PERMISSION = getPermissionFromDatabaseByPermissionID(
            ConfigurationDataSupplierHelper.getRemoveUserAdminStatusPermissionID()
    );
    //#endregion permission variables

    //#region admin only permissions
    public void addPermissionDeleteUserToUsersPermissions(User user) {
        permissionUserAssignmentHelper.addPermissionToUsersPermissions(user, DELETE_USER_PERMISSION);
    }

    public void addPermissionCreateUserToUserToUsersPermissions(User user) {
        permissionUserAssignmentHelper.addPermissionToUsersPermissions(user, CREATE_USER_PERMISSION);
    }

    public void addPermissionEditUserToUserToUsersPermissions(User user) {
        permissionUserAssignmentHelper.addPermissionToUsersPermissions(user, EDIT_USER_PERMISSION);
    }

    public void addPermissionGetUserInformationByUserIDToUsersPermissions(User user) {
        permissionUserAssignmentHelper.addPermissionToUsersPermissions(user, GET_USER_INFORMATION_PERMISSION);
    }

    public void addPermissionUserCanModifyAdminStatusOfUserToUsersPermissions(User user) {
        addPermissionUserCanGiveAdminStatusToUserToUsersPermissions(user);
        addPermissionUserCanRemoveAdminStatusFromUserToUsersPermissions(user);
    }

    private void addPermissionUserCanGiveAdminStatusToUserToUsersPermissions(User user) {
        permissionUserAssignmentHelper.addPermissionToUsersPermissions(user, GIVE_ADMIN_STATUS_PERMISSION);
    }

    private void addPermissionUserCanRemoveAdminStatusFromUserToUsersPermissions(User user) {
        permissionUserAssignmentHelper.addPermissionToUsersPermissions(user, REMOVE_ADMIN_STATUS_PERMISSION);
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
    
    public Permission getDeleteUserPermission() {
        return DELETE_USER_PERMISSION;
    }

    public Permission getCreateUserPermission() {
        return CREATE_USER_PERMISSION;
    }

    public Permission getEditUserPermission() {
        return EDIT_USER_PERMISSION;
    }

    public Permission getGetUserInformationPermission() {
        return GET_USER_INFORMATION_PERMISSION;
    }

    public Permission getGiveAdminStatusPermission() {
        return GIVE_ADMIN_STATUS_PERMISSION;
    }

    public Permission getRemoveAdminStatusPermission() {
        return REMOVE_ADMIN_STATUS_PERMISSION;
    }
    //#endregion getter
}
