package helpertests;

import eventmanagementcore.usermanagement.UserManager;
import eventmanagementcore.permissionrolemanagement.Permission;
import eventmanagementcore.usermanagement.User;
import helper.PermissionUserAssignmentHelper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PermissionUserAssignmentHelperTestDrive {

    PermissionUserAssignmentHelper permissionUserAssignmentHelper = new PermissionUserAssignmentHelper();

    User nonAdminUser = new User("","","","","",0000000);
    User adminUser = UserManager.readUserByID("duuuY5XI4XyPQnzIChVL");

    Permission adminPermission = new Permission("adminPermission", true);
    Permission nonAdminPermission = new Permission("canEditOwnUser", false);

    @Test
    void testPermissionUserAssignmentHelper() {
        assert(permissionUserAssignmentHelper != null);
    }

    @Test
    void testAddPermissionToOwnUsersPermissions() {
        assertTrue(permissionUserAssignmentHelper.addPermissionToUsersPermissions(adminUser, adminPermission));

        assertTrue(permissionUserAssignmentHelper.addPermissionToUsersPermissions(adminUser, nonAdminPermission));

        assertFalse(permissionUserAssignmentHelper.addPermissionToUsersPermissions(nonAdminUser, adminPermission));

        assertTrue(permissionUserAssignmentHelper.addPermissionToUsersPermissions(nonAdminUser, nonAdminPermission));
    }

    @Test
    void testGetAddPermissionsToUserFromDatabase() {
        System.out.println("adminUser has: " +
                PermissionUserAssignmentHelper.getPermissionsForUserFromDatabase(adminUser).size() +
                " permissions"
        );
        System.out.println("adminUser user should have all 6 admin permissions.");

        assertTrue(PermissionUserAssignmentHelper.getPermissionsForUserFromDatabase(adminUser).size() == 6);
    }

}
