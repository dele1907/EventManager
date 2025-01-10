package HelperTests;

import EventManagementCore.PermissionRoleManagement.Permission;
import EventManagementCore.UserManagement.User;
import Helper.PermissionUserAssignmentHelper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PermissionUserAssignmentHelperTestDrive {
    PermissionUserAssignmentHelper permissionUserAssignmentHelper = new PermissionUserAssignmentHelper();
    User nonAdminUser = new User("","","","","",0000000);
    User adminUser = new User("","","","","",0000000, true);

    Permission adminPermission = new Permission("canEditUser", true);
    Permission nonAdminPermission = new Permission("canEditOwnUser", false);

    @Test
    void testPermissionUserAssignmentHelper() {
        assert(permissionUserAssignmentHelper != null);
    }

    @Test
    void testAddPermissionToUsersPermissions() {
        assertTrue(PermissionUserAssignmentHelper.addPermissionToUsersPermissions(adminUser, adminPermission));

        assertTrue(PermissionUserAssignmentHelper.addPermissionToUsersPermissions(adminUser, nonAdminPermission));

        assertFalse(PermissionUserAssignmentHelper.addPermissionToUsersPermissions(nonAdminUser, adminPermission));

        assertTrue(PermissionUserAssignmentHelper.addPermissionToUsersPermissions(nonAdminUser, nonAdminPermission));
    }

}
