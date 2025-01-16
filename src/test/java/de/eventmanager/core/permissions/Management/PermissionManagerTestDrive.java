package de.eventmanager.core.permissions.Management;


import static org.junit.jupiter.api.Assertions.*;

import de.eventmanager.core.permissions.Permission;
import de.eventmanager.core.users.User;
import org.junit.jupiter.api.Test;

public class PermissionManagerTestDrive {

    PermissionManager permissionManager = new PermissionManager();

    private Permission deleUserPermission = permissionManager.getDeleteUserPermission();
    private Permission createUserPermission = permissionManager.getCreateUserPermission();
    private Permission editUserPermission = permissionManager.getEditUserPermission();
    private Permission getUserInformationPermission = permissionManager.getGetUserInformationPermission();
    private Permission giveAdminStatusPermission = permissionManager.getGiveAdminStatusPermission();
    private Permission removeAdminStatusPermission = permissionManager.getRemoveAdminStatusPermission();

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
    void testAddPermissionDeleteUserToUser() {

        permissionManager.addPermissionDeleteUserToUsersPermissions(nonAdminUser);

        System.out.println("\n\n__________________________________");
        System.out.println("Only admin user can delete an user");
        System.out.println("__________________________________");
        System.out.println(
                "\nNon admin user can delete a new user: " +
                        nonAdminUser.getPermissions().contains(deleUserPermission.getPermissionID())
        );
        assertFalse(nonAdminUser.getPermissions().contains(deleUserPermission.getPermissionID()));

        permissionManager.addPermissionDeleteUserToUsersPermissions(adminUser);
        System.out.println(
                "Admin user can delete a new user: " +
                adminUser.getPermissions().contains(deleUserPermission.getPermissionID())
        );
        assertTrue(adminUser.getPermissions().contains(deleUserPermission.getPermissionID()));
    }

    @Test
    void testAddPermissionCreateUserToUser() {
        permissionManager.addPermissionCreateUserToUserToUsersPermissions(nonAdminUser);

        System.out.println("\n\n_____________________________________");
        System.out.println("Only admin user can create a new user");
        System.out.println("_____________________________________");
        System.out.println(
                "\nNon admin user can create a new user: " +
                nonAdminUser.getPermissions().contains(createUserPermission.getPermissionID())
        );
        assertFalse(nonAdminUser.getPermissions().contains(createUserPermission.getPermissionID()));

        permissionManager.addPermissionCreateUserToUserToUsersPermissions(adminUser);
        System.out.println(
                "Admin user can create a new user: " +
                adminUser.getPermissions().contains(createUserPermission.getPermissionID())
        );
        assertTrue(adminUser.getPermissions().contains(createUserPermission.getPermissionID()));
    }

    @Test
    void testAddPermissionEditUserToUser() {
        permissionManager.addPermissionEditUserToUserToUsersPermissions(nonAdminUser);
        assertFalse(nonAdminUser.getPermissions().contains(editUserPermission.getPermissionID()));

        permissionManager.addPermissionEditUserToUserToUsersPermissions(adminUser);
        assertTrue(adminUser.getPermissions().contains(editUserPermission.getPermissionID()));
    }

    @Test
    void testUserCanHavePermissionGetUserInformationByUserID() {
        System.out.println("\n\n_______________________________________________________________________");
        System.out.println("Only admin user can see all information of an user based on it's userID");
        System.out.println("_______________________________________________________________________");

        permissionManager.addPermissionGetUserInformationByUserIDToUsersPermissions(nonAdminUser);
        System.out.println(
                "\nNon admin user can see information of an user: " +
                nonAdminUser.getPermissions().contains(getUserInformationPermission.getPermissionID())
        );
        assertFalse(nonAdminUser.getPermissions().contains(getUserInformationPermission.getPermissionID()));

        permissionManager.addPermissionGetUserInformationByUserIDToUsersPermissions(adminUser);
        System.out.println(
                "Admin user can see information of an user: " +
                adminUser.getPermissions().contains(getUserInformationPermission.getPermissionID())
        );
        assertTrue(adminUser.getPermissions().contains(getUserInformationPermission.getPermissionID()));
    }

    @Test
    void testUserCanModifyAdminStatusOfUser() {
        System.out.println("\n\n__________________________________________________");
        System.out.println("Only admin user can modify admin status of an user");
        System.out.println("__________________________________________________");

        permissionManager.addPermissionUserCanModifyAdminStatusOfUserToUsersPermissions(nonAdminUser);
        System.out.println(
                "\nNon admin user can give admin status to an user: " +
                nonAdminUser.getPermissions().contains(giveAdminStatusPermission.getPermissionID())
        );
        System.out.println(
                "\nNon admin user can remove admin status from an user: " +
                        nonAdminUser.getPermissions().contains(removeAdminStatusPermission.getPermissionID())
        );

        assertFalse(
                nonAdminUser.getPermissions().contains(removeAdminStatusPermission.getPermissionID()) &&
                nonAdminUser.getPermissions().contains(giveAdminStatusPermission.getPermissionID())
        );

        permissionManager.addPermissionUserCanModifyAdminStatusOfUserToUsersPermissions(adminUser);
        System.out.println(
                "Admin user can give admin status to an user: " +
                adminUser.getPermissions().contains(giveAdminStatusPermission.getPermissionID()) +
                "\n"
        );
        System.out.println(
                "Admin user can remove admin status from an user: " +
                        adminUser.getPermissions().contains(removeAdminStatusPermission.getPermissionID()) +
                        "\n"
        );

        assertTrue(
                adminUser.getPermissions().contains(removeAdminStatusPermission.getPermissionID()) &&
                adminUser.getPermissions().contains(giveAdminStatusPermission.getPermissionID())
        );
    }
    //#endregion admin only permissions tests

    //#region general user permissions tests
    //#endregion general user permissions tests

    //#region event owner (& admin) only permissions tests
    //#endregion event owner (& admin) permissions tests

    //#region user related to certain event (& admin) only permissions tests
    //#endregion user related to certain event (& admin) only permissions tests
}
