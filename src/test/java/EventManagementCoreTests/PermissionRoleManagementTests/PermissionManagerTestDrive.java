package EventManagementCoreTests.PermissionRoleManagementTests;

import EventManagementCore.PermissionRoleManagement.PermissionManager;

import static org.junit.jupiter.api.Assertions.*;

import EventManagementCore.UserManagement.User;
import Helper.IDGenerationHelper;
import org.junit.jupiter.api.Test;

public class PermissionManagerTestDrive {
    PermissionManager permissionManager = new PermissionManager();

    /**                         Anmerkung
     *
     * Ihr habt den Standard-User boolean isAdmin standardmäßig auf False gesetzt
     * Dadurch ignoriert der User bei der instanzierung den boolean Wert den ihr setzt. Daher braucht der
     * Konstruktor das Boolean-Argument nicht mehr und ich habe es entfernt. Dafür musste ich das true und false
     * bei deinen beiden Usern entfernen. Du kannst einen User jetzt nur noch durch die Methode addAdminStatusToUser()
     * zum Admin machen
    * */

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
                    01223

            );

    //#region admin only permissions tests
    @Test
    void testUserCanHavePermissionDeleteUserForNotAnAdminUser() {
        System.out.println("\n\n__________________________________");
        System.out.println("Only admin user can delete an user");
        System.out.println("__________________________________");
        System.out.println(
                "\nNon admin user can delete a new user: " +
                permissionManager.userCanHavePermissionDeleteUser(nonAdminUser)
        );
        assertFalse(permissionManager.userCanHavePermissionDeleteUser(nonAdminUser));

        System.out.println(
                "Admin user can delete a new user: " +
                permissionManager.userCanHavePermissionDeleteUser(adminUser)
        );
        assertTrue(permissionManager.userCanHavePermissionDeleteUser(adminUser));
    }

    @Test
    void testUserCanHavePermissionCreateUserForNotAnAdminUser() {
        System.out.println("\n\n_____________________________________");
        System.out.println("Only admin user can create a new user");
        System.out.println("_____________________________________");
        System.out.println(
                "\nNon admin user can create a new user: " +
                permissionManager.userCanHavePermissionCreateUser(nonAdminUser)
        );
        assertFalse(permissionManager.userCanHavePermissionCreateUser(nonAdminUser));

        System.out.println(
                "Admin user can create a new user: " +
                permissionManager.userCanHavePermissionCreateUser(adminUser)
        );
        assertTrue(permissionManager.userCanHavePermissionCreateUser(adminUser));
    }

    @Test
    void testUserCanHavePermissionEditUserForNotAnAdminUser() {
        assertFalse(permissionManager.userCanHavePermissionEditUser(nonAdminUser));

        assertTrue(permissionManager.userCanHavePermissionEditUser(adminUser));
    }

    @Test
    void testUserCanHavePermissionGetUserInformationByUserID() {
        System.out.println("\n\n_______________________________________________________________________");
        System.out.println("Only admin user can see all information of an user based on it's userID");
        System.out.println("_______________________________________________________________________");
        System.out.println(
                "\nNon admin user can see information of an user: " +
                permissionManager.getGetUserInformationByUserID(nonAdminUser)
        );
        assertFalse(permissionManager.getGetUserInformationByUserID(nonAdminUser));

        System.out.println(
                "Admin user can see information of an user: " +
                permissionManager.getGetUserInformationByUserID(adminUser)
        );
        assertTrue(permissionManager.getGetUserInformationByUserID(adminUser));
    }

    @Test
    void testUserCanModifyAdminStatusOfUser() {
        System.out.println("\n\n__________________________________________________");
        System.out.println("Only admin user can modify admin status of an user");
        System.out.println("__________________________________________________");
        System.out.println(
                "\nNon admin user can modify admin status of an user: " +
                permissionManager.getUserCanModifyAdminStatusOfUser(nonAdminUser)
        );
        assertFalse(permissionManager.getUserCanModifyAdminStatusOfUser(nonAdminUser));

        System.out.println(
                "Admin user can modify admin status of an user: " +
                permissionManager.getUserCanModifyAdminStatusOfUser(adminUser) +
                "\n"
        );
        assertTrue(permissionManager.getUserCanModifyAdminStatusOfUser(adminUser));
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
                        permissionManager.getUserCanModifyAdminStatusOfUser(adminUser) +
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
