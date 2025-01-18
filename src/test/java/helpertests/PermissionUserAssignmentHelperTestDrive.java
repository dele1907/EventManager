package helpertests;

import de.eventmanager.core.users.Management.UserManager;
import de.eventmanager.core.permissions.Permission;
import de.eventmanager.core.users.User;
import helper.DatabaseSimulation.JsonDatabaseHelper;
import helper.PermissionUserAssignmentHelper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PermissionUserAssignmentHelperTestDrive {

    PermissionUserAssignmentHelper permissionUserAssignmentHelper = new PermissionUserAssignmentHelper();

    User nonAdminUser = new User("","","","","",0000000);
    //User adminUser = UserManager.readUserByID("duuuY5XI4XyPQnzIChVL").get();
    User adminUser = JsonDatabaseHelper.getUserByEmailFromJson("dele00003@htwsaar.de").get();

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
       // Optional<List<Permission>> optionalPermissions = PermissionUserAssignmentHelper.getPermissionsForUserFromDatabase(adminUser);
        Optional<List<Permission>> optionalJsonPermissions = JsonDatabaseHelper.getPermissionsForUserFromJson(adminUser);

        if (!optionalJsonPermissions.isPresent()) {
            fail("No permissions found for adminUser");
        }

        List<Permission> permissions = optionalJsonPermissions.get();
        System.out.println("adminUser has: " + permissions.size() + " permissions");
        System.out.println("adminUser should have all 6 admin permissions.");
        System.out.println("adminUser has the following permissions: ");

        for (Permission permission : permissions) {
            System.out.println(permission.getPermissionName());
        }

        assertTrue(permissions.size() == 6);
    }

}
