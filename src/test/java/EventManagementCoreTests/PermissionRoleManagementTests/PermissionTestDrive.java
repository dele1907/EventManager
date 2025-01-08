package EventManagementCoreTests.PermissionRoleManagementTests;

import EventManagementCore.PermissionRoleManagement.Permission;
import org.junit.jupiter.api.Test;

public class PermissionTestDrive {
    private Permission adminPermission = new Permission("canEditUser", true);

    @Test
    void testPermissionName() {
        assert(adminPermission.getPermissionName().equals("canEditUser"));
    }

    @Test
    void testPermissionIsAdminPermission() {
        assert(adminPermission.isAdminPermission() == true);
    }
}
