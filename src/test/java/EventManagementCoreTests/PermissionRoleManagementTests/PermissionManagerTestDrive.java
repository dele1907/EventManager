package EventManagementCoreTests.PermissionRoleManagementTests;

import EventManagementCore.PermissionRoleManagement.PermissionManager;

import static org.junit.jupiter.api.Assertions.*;

import EventManagementCore.UserManagement.User;
import org.junit.jupiter.api.Test;

public class PermissionManagerTestDrive {

    PermissionManager permissionManager = new PermissionManager();

    User nonAdminUser = new User
            (
                    "",
                    "",
                    "",
                    "",
                    "",
                    01223
            );

    User adminUser = new User
            (
                    "",
                    "",
                    "",
                    "",
                    "",
                    01223,
                    true

            );

    //#region admin only permissions tests
    @Test
    void testUserCanHavePermissionDeleteUserForNotAnAdminUserToUser() {
        System.out.println("\n\n__________________________________");
        System.out.println("Only admin user can delete an user");
        System.out.println("__________________________________");
        System.out.println(
                "\nNon admin user can delete a new user: " +
                permissionManager.addPermissionDeleteUserToUsersPermissions(nonAdminUser)
        );
        assertFalse(permissionManager.addPermissionDeleteUserToUsersPermissions(nonAdminUser));

        System.out.println(
                "Admin user can delete a new user: " +
                permissionManager.addPermissionDeleteUserToUsersPermissions(adminUser)
        );
        assertTrue(permissionManager.addPermissionDeleteUserToUsersPermissions(adminUser));
    }

    @Test
    void testUserCanHavePermissionCreateUserForNotAnAdminUserToUser() {
        System.out.println("\n\n_____________________________________");
        System.out.println("Only admin user can create a new user");
        System.out.println("_____________________________________");
        System.out.println(
                "\nNon admin user can create a new user: " +
                permissionManager.addPermissionCreateUserToUserToUsersPermissions(nonAdminUser)
        );
        assertFalse(permissionManager.addPermissionCreateUserToUserToUsersPermissions(nonAdminUser));

        System.out.println(
                "Admin user can create a new user: " +
                permissionManager.addPermissionCreateUserToUserToUsersPermissions(adminUser)
        );
        assertTrue(permissionManager.addPermissionCreateUserToUserToUsersPermissions(adminUser));
    }

    @Test
    void testUserCanHavePermissionEditUserForNotAnAdminUserToUser() {
        assertFalse(permissionManager.addPermissionEditUserToUserToUsersPermissions(nonAdminUser));

        assertTrue(permissionManager.addPermissionEditUserToUserToUsersPermissions(adminUser));
    }

    @Test
    void testUserCanHavePermissionGetUserInformationByUserID() {
        System.out.println("\n\n_______________________________________________________________________");
        System.out.println("Only admin user can see all information of an user based on it's userID");
        System.out.println("_______________________________________________________________________");
        System.out.println(
                "\nNon admin user can see information of an user: " +
                permissionManager.addPermissionGetUserInformationByUserIDToUsersPermissions(nonAdminUser)
        );
        assertFalse(permissionManager.addPermissionGetUserInformationByUserIDToUsersPermissions(nonAdminUser));

        System.out.println(
                "Admin user can see information of an user: " +
                permissionManager.addPermissionGetUserInformationByUserIDToUsersPermissions(adminUser)
        );
        assertTrue(permissionManager.addPermissionGetUserInformationByUserIDToUsersPermissions(adminUser));
    }

    @Test
    void testUserCanModifyAdminStatusOfUser() {
        System.out.println("\n\n__________________________________________________");
        System.out.println("Only admin user can modify admin status of an user");
        System.out.println("__________________________________________________");
        System.out.println(
                "\nNon admin user can modify admin status of an user: " +
                permissionManager.addPermissionUserCanModifyAdminStatusOfUserToUsersPermissions(nonAdminUser)
        );
        assertFalse(permissionManager.addPermissionUserCanModifyAdminStatusOfUserToUsersPermissions(nonAdminUser));

        System.out.println(
                "Admin user can modify admin status of an user: " +
                permissionManager.addPermissionUserCanModifyAdminStatusOfUserToUsersPermissions(adminUser) +
                "\n"
        );
        assertTrue(permissionManager.addPermissionUserCanModifyAdminStatusOfUserToUsersPermissions(adminUser));
    }

    @Test
    void testUserCanHaveAllAdminPermissions() {
        System.out.println("\n\n__________________________________________________");
        System.out.println("Only admin user have admin permissions");
        System.out.println("__________________________________________________");
        System.out.println(
                "\nNon admin user can have admin permissions: " +
                        permissionManager.getUserHasAdminPermissions(nonAdminUser)
        );
        assertFalse(permissionManager.getUserHasAdminPermissions(nonAdminUser));

        System.out.println(
                "Admin user can have admin permissions: " +
                        permissionManager.addPermissionUserCanModifyAdminStatusOfUserToUsersPermissions(adminUser) +
                        "\n"
        );
        assertTrue(permissionManager.getUserHasAdminPermissions(adminUser));
    }
    //#endregion admin only permissions tests

    //#region general user permissions tests
    //#endregion general user permissions tests

    //#region event owner (& admin) only permissions tests
    //#endregion event owner (& admin) permissions tests

    //#region user related to certain event (& admin) only permissions tests
    //#endregion user related to certain event (& admin) only permissions tests
}
