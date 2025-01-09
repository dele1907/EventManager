package EventManagementCore.PermissionRoleManagement;

import Helper.IDGenerationHelper;

public class Permission {
    private String permissionName;
    private String permissionID;
    private boolean isAdminPermission;

    public Permission(String permissionName, boolean isAdminPermission) {
        this.permissionName = permissionName;
        this.permissionID = IDGenerationHelper.generateRandomString(IDGenerationHelper.ID_DEFAULT_LENGHT);
        this.isAdminPermission = isAdminPermission;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public String getPermissionID() {
        return permissionID;
    }

    public boolean isAdminPermission() {
        return isAdminPermission;
    }
}
