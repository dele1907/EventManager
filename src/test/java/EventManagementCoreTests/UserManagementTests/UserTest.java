package EventManagementCoreTests.UserManagementTests;

import EventManagementCore.UserManagement.User;
import Helper.IDGenerationHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    User testUser = new User("firstName", "LastName", "dateOfBirth",
            "firstName.lastName@testmail.com", "eventManager123", 5566778);
    User testAdminUser = new User("admin","trator","dateOfBirth",
            "firstName@adminmail.com", "AdminEventManager123", 5566778, true);



    @Test
    @DisplayName("UserCreateUser Test")
    void userWithoutPermissionCreateNewUserTest() {

        assertFalse(testUser.createNewUser("new","User", "dateOfBirth", "firstName.lastName@testmail.com",
                "eventManager123", 11223344, false));

    }
    @Test
    @DisplayName("AdminCreatesUser Test")
    void adminUserCreateNewUserTest() {

        assertTrue(testAdminUser.createNewUser("new","User", "dateOfBirth", "firstName.lastName@testmail.com",
                "eventManager123", 11223344, false));

    }

    String validTestPassword = "eventManager123";
    String inValidTestPassword = "eventManager124";

    @Test
    void isValidRegistrationPasswordTest() {
        assertTrue(testUser.isValidRegistrationPassword(validTestPassword, "eventManager123"));

        assertFalse(testUser.isValidRegistrationPassword(inValidTestPassword, "eventManager123"));
    }

    @Test
    @DisplayName("isUserAdminAndIsUserNoAdmin Test")
    void isUserAdminAndIsNoAdminTest() {
        testUser.addAdminStatusToUser();
        assertTrue(testUser.isAdmin());
        testUser.removeAdminStatusFromUser();
        assertFalse(testUser.isAdmin());
    }
}
