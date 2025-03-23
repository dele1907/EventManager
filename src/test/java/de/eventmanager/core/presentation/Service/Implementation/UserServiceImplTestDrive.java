package de.eventmanager.core.presentation.Service.Implementation;

import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationDataPayload;
import de.eventmanager.core.users.User;
import helper.PasswordHelper;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTestDrive {
    private static User adminUser = null;
    private static User nonAdminUser = null;
    private UserServiceImpl userServiceImpl = new UserServiceImpl();
    private static String PASSWORD = "12345678";
    private static String TEST_USER_EMAIL = "TestUser@muster.com";

    @BeforeAll
    static void setup() {
        UserDatabaseConnector.createNewUser(new User("Dis",
                "Appear",
                "2000-01-01",
                "disappear@muster.com",
                PASSWORD,
                "123456",
                false)
        );

        UserDatabaseConnector.createNewUser(new User("DisAdmin",
                "Appear",
                "01.01.2000",
                "disappearAdmin@muster.com",
                PASSWORD,
                "123456",
                true)
        );

        var optionalAdminUser = UserDatabaseConnector.readUserByEMail("disappearAdmin@muster.com");
        var optionalNonAdminUser = UserDatabaseConnector.readUserByEMail("disappear@muster.com");

        if (optionalAdminUser.isPresent()) {
            adminUser = optionalAdminUser.get();
        }

        if (optionalNonAdminUser.isPresent()) {
            nonAdminUser = optionalNonAdminUser.get();
        }
    }

    @Test
    void registerUser() {
        boolean isRegistered = userServiceImpl.registerUser(
                new UserRegistrationDataPayload(
                "Test",
                "User",
                "2000-01-01",
                TEST_USER_EMAIL,
                "12345678",
                PASSWORD,
                PASSWORD
                ), false, adminUser.getUserID()

        );

        assertTrue(isRegistered, "User could not be registered!");
    }

    @Test
    void loginUser() {
        assertFalse(userServiceImpl.loginUser(adminUser.getEMailAddress(), PASSWORD).isEmpty(),
                "User could not be logged in!"
        );
    }

    @Test
    void updateUsersInformation() {
        userServiceImpl.editUser(TEST_USER_EMAIL, adminUser.getUserID(),
                "NewName", "NewName", TEST_USER_EMAIL, "123"
        );

        if (UserDatabaseConnector.readUserByEMail(TEST_USER_EMAIL).isEmpty()) {
            fail("User could not be found!");
        }

        assertTrue(UserDatabaseConnector.readUserByEMail(TEST_USER_EMAIL).get().getFirstName().equals("NewName"),
                "User information could not be updated!"
        );
    }

    @Test
    void deleteUser() {
        if (UserDatabaseConnector.readUserByEMail(TEST_USER_EMAIL).isEmpty()) {
            fail("User could not be found!");
        }

        assertTrue(userServiceImpl.deleteUser(TEST_USER_EMAIL, adminUser.getUserID()),
                "User could not be removed!"
        );
    }

    @Test
    void canGetUserInformationByEmail() {
        if (UserDatabaseConnector.readUserByEMail(TEST_USER_EMAIL).isEmpty()) {
            fail("User could not be found!");
        }

        assertTrue(!userServiceImpl.getUserInformationByEmail(TEST_USER_EMAIL).isEmpty(),
                "User information could not be found!"
        );
    }

    @Test
    void systemHasAdminUser() {
        assertTrue(userServiceImpl.getAdminUserIsPresentInDatabase(), "Admin user could not be found!");
    }

    @Test
    void adminUserHasAdminRoleByEMailAddress() {
        assertTrue(userServiceImpl.getUserIsAdminUserByEmail(adminUser.getEMailAddress()),
                "User is not an admin!"
        );
    }

    @Test
    void adminUserHasAdminRoleByID() {
        assertTrue(userServiceImpl.getUserIsAdminUserByID(adminUser.getUserID()), "User is not an admin!");
    }

    @Test
    void adminNameEqualsLoggedInUserName() {
        assertEquals(adminUser.getFirstName(), userServiceImpl.getLoggedInUserName(adminUser.getUserID()),
                "User is not logged in!"
        );
    }

    @Test
    void adminRightHandling() {
        grantAdminRightsToNonAdminUser();
        takeAdminRightsFromAnAdmin();
    }

    private void grantAdminRightsToNonAdminUser() {
        assertFalse(userServiceImpl.getUserIsAdminUserByEmail(nonAdminUser.getEMailAddress()),
                "User is already an admin!"
        );

        userServiceImpl.grantAdminRightsToUser(nonAdminUser.getEMailAddress());

        assertTrue(userServiceImpl.getUserIsAdminUserByEmail(nonAdminUser.getEMailAddress()),
                "User could not be granted admin rights!"
        );
    }

    private void takeAdminRightsFromAnAdmin() {
        assertTrue(userServiceImpl.getUserIsAdminUserByEmail(nonAdminUser.getEMailAddress()),
                "User is not an admin!"
        );

        userServiceImpl.removeAdminRightsFromUser(nonAdminUser.getEMailAddress());

        assertFalse(userServiceImpl.getUserIsAdminUserByEmail(nonAdminUser.getEMailAddress()),
                "User could not be removed from admin role!"
        );
    }

    @AfterAll
    static void tearDown() {
        if (adminUser != null) {
            UserDatabaseConnector.deleteUserByID(adminUser.getUserID());
        }

        if (nonAdminUser != null) {
            UserDatabaseConnector.deleteUserByID(nonAdminUser.getUserID());
        }

        if (!UserDatabaseConnector.readUserByEMail(TEST_USER_EMAIL).isEmpty()) {
            UserDatabaseConnector.deleteUserByEmail(TEST_USER_EMAIL);
        }
    }
}
