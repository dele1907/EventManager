package EventManagementCoreTests.PermissionRoleManagementTests;

import EventManagementCore.PermissionRoleManagement.RoleManager;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RoleManagerTestDrive {

    RoleManager roleManager = new RoleManager("550erfdsf667");

    @Test
    void testHasPermissionDeleteUser() {
        assertTrue(!roleManager.hasPermissionDeleteUser(roleManager.notAnAdminUser));
    }

    @Test
    void testHasPermissionAddUser() {
        assert(false);
    }

    @Test
    void testHasPermissionEditUser() {
        assert(false);
    }

    @Test
    void testHasPermissionGiveAdminStatusToUser() {
        assert(false);
    }

    @Test
    void testHasPermissionRemoveAdminStatusFromUser() {
        assert(false);
    }
}
