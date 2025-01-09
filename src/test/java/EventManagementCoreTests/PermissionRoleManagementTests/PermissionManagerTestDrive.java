package EventManagementCoreTests.PermissionRoleManagementTests;

import EventManagementCore.PermissionRoleManagement.PermissionManager;

import static org.junit.jupiter.api.Assertions.*;

import EventManagementCore.UserManagement.User;
import Helper.IDGenerationHelper;
import org.junit.jupiter.api.Test;

public class PermissionManagerTestDrive {
    PermissionManager permissionManager = new PermissionManager();
    User nonAdminDummyUser = new User
            (
                IDGenerationHelper.generateRandomString(IDGenerationHelper.ID_DEFAULT_LENGHT),
                    "",
                    "",
                    "",
                    "",
                    "",
                    01223,
                    false
            );

    User adminDummyUser = new User
            (
                    IDGenerationHelper.generateRandomString(IDGenerationHelper.ID_DEFAULT_LENGHT),
                    "",
                    "",
                    "",
                    "",
                    "",
                    01223,
                    true
            );

    @Test
    void testUserCanHavePermissionDeleteUserForNotAnAdminUser() {

        assertFalse(permissionManager.userCanHavePermissionDeleteUser(nonAdminDummyUser));

        assertTrue(permissionManager.userCanHavePermissionDeleteUser(adminDummyUser));
    }

    @Test
    void testUserCanHavePermissionCreateUserForNotAnAdminUser() {

        assertFalse(permissionManager.userCanHavePermissionCreateUser(nonAdminDummyUser));

        assertTrue(permissionManager.userCanHavePermissionCreateUser(adminDummyUser));
    }
}
