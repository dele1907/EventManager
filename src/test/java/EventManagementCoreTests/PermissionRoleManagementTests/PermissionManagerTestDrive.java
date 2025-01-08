package EventManagementCoreTests.PermissionRoleManagementTests;

import EventManagementCore.PermissionRoleManagement.PermissionManager;
import EventManagementCore.PermissionRoleManagement.RoleManager;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PermissionManagerTestDrive {
    PermissionManager roleManager = new PermissionManager("550erfdsf667");

    @Test
    void testHasPermissionDeleteUserForNotAnAdminUser() {
        assertFalse(roleManager.hasPermissionDeleteUser(roleManager.getNotAnAdminDummyUser()));
    }

    @Test
    void testHasPermissionDeleteUserForAnAdminUser() {
        assertTrue(roleManager.hasPermissionDeleteUser(roleManager.getAnAdminDummyUser()));
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
