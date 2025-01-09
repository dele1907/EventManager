package EventManagementCoreTests.PermissionRoleManagementTests;

import EventManagementCore.PermissionRoleManagement.Permission;
import org.junit.jupiter.api.Test;

public class PermissionTestDrive {

    String PERMISSION_NAME_ADMIN_PERMISSION = "canEditUser";
    String PERMISSION_NAME_NON_ADMIN_PERMISSION = "canNotEditUser";
    private Permission adminPermission = new Permission(PERMISSION_NAME_ADMIN_PERMISSION, true);
    private Permission nonAdminPermission = new Permission(PERMISSION_NAME_NON_ADMIN_PERMISSION, false);

    @Test
    void testPermissionName() {
        System.out.println("Permission name is: " + adminPermission.getPermissionName());
        System.out.println("Permission name should be: " + PERMISSION_NAME_ADMIN_PERMISSION);

        assert(adminPermission.getPermissionName().equals(PERMISSION_NAME_ADMIN_PERMISSION));
    }

    @Test
    void testPermissionIsAdminPermission() {
        System.out.println("Permission is admin permission: " + adminPermission.isAdminPermission());
        System.out.println("Permission should be admin permission: " + true);

        assert(adminPermission.isAdminPermission() == true);
    }
}
