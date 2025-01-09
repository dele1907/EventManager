package EventManagementCoreTests.PermissionRoleManagementTests;

import EventManagementCore.PermissionRoleManagement.PermissionManager;

import static org.junit.jupiter.api.Assertions.*;

import EventManagementCore.UserManagement.User;
import Helper.PermissionHelper;
import org.junit.jupiter.api.Test;

public class PermissionManagerTestDrive {
    PermissionManager permissionManager = new PermissionManager();

    @Test
    void testUserCanHavePermissionDeleteUserForNotAnAdminUser() {
        User user = new User();

        assertFalse(permissionManager.userCanHavePermissionDeleteUser(user));
    }

    /**@Test
    void testUserCanHavePermissionDeleteUserForAnAdminUser() {
        assertTrue(permissionManager.userCanHavePermissionDeleteUser(permissionManager.getAnAdminDummyUser()));
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
    }*/
}
