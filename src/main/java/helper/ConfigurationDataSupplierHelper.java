package helper;

public class ConfigurationDataSupplierHelper {

    //#region permissionIDs
    private static final String DELETE_USER_PERMISSION_ID = "EFdTxfa05Ngu9z47jIw2";
    private static final String CREATE_USER_PERMISSION_ID = "59zbwnXJmciTIJlANNlB";
    private static final String EDIT_USER_PERMISSION_ID = "AVNnGoJ17zscv54hLMao";
    private static final String GET_USER_INFORMATION_PERMISSION_ID = "tsu7mY9GtQQXprG1rgVy";
    private static final String GIVE_USER_ADMIN_STATUS_PERMISSION_ID = "NOHKhcZd4THbUPqdZ7I8";
    private static final String REMOVE_USER_ADMIN_STATUS_PERMISSION_ID = "r71hr0wu8Wwgmh0qTQOh";
    //#endregion permissionIDs

    //#region column names permission database
    private static final String COLUMN_PERMISSION_ID = "permissionID";
    private static final String COLUMN_PERMISSION_NAME = "permissionName";
    private static final String COLUMN_PERMISSION_IS_ADMIN_PERMISSION = "isAdminPermission";
    //#endregion column names permission database

    //#region getter permissionIDs
    public static String getDeleteUserPermissionID() {
        return DELETE_USER_PERMISSION_ID;
    }

    public static String getCreateUserPermissionID() {
        return CREATE_USER_PERMISSION_ID;
    }

    public static String getEditUserPermissionID() {
        return EDIT_USER_PERMISSION_ID;
    }

    public static String getGetUserInformationPermissionID() {
        return GET_USER_INFORMATION_PERMISSION_ID;
    }

    public static String getGiveUserAdminStatusPermissionID() {
        return GIVE_USER_ADMIN_STATUS_PERMISSION_ID;
    }

    public static String getRemoveUserAdminStatusPermissionID() {
        return REMOVE_USER_ADMIN_STATUS_PERMISSION_ID;
    }
    //#endregion getter permissionIDs

    //#region getter column names permission database
    public static String getColumnPermissionID() {
        return COLUMN_PERMISSION_ID;
    }

    public static String getColumnPermissionName() {
        return COLUMN_PERMISSION_NAME;
    }

    public static String getColumnPermissionIsAdminPermission() {
        return COLUMN_PERMISSION_IS_ADMIN_PERMISSION;
    }
    //#endregion getter column names permission database
}
