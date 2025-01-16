package helpertests;

import de.eventmanager.core.users.Management.UserManager;
import de.eventmanager.core.permissions.Permission;
import de.eventmanager.core.users.User;
import helper.PermissionUserAssignmentHelper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

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
        Optional<List<Permission>> optionalPermissions = PermissionUserAssignmentHelper.getPermissionsForUserFromDatabase(adminUser);

        if (!optionalPermissions.isPresent()) {
            fail("No permissions found for adminUser");
        }

        List<Permission> permissions = optionalPermissions.get();
        System.out.println("adminUser has: " + permissions.size() + " permissions");
        System.out.println("adminUser should have all 6 admin permissions.");

        assertTrue(permissions.size() == 6);
    }

}
