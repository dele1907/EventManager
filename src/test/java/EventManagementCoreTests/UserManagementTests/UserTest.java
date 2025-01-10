package EventManagementCoreTests.UserManagementTests;

import EventManagementCore.UserManagement.User;
import Helper.IDGenerationHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    User testUser = new User("1234","firstName", "LastName", "dateOfBirth",
            "firstTestName.lastTestName@testmail.com", "eventManager123", 5566778,false);
    User testAdminUser = new User("1235","admin","trator","dateOfBirth",
            "firstTestName@adminmail.com", "AdminEventManager123", 5566778, true);
    User system = new User("System", "", "", "", "goodPassword",0,true);



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
    void isValidAndIsNotValidRegistrationPasswordTest() {
        assertTrue(testUser.isValidRegistrationPassword(validTestPassword, "eventManager123"));

        assertFalse(testUser.isValidRegistrationPassword(inValidTestPassword, "eventManager123"));
    }

    @Test
    void checkExistingEmailAdressForUserTest() {
        assertTrue(system.comparingEmailAddress("firstName.lastName@testmail.com"));
    }

    @Test
    @DisplayName("LoginSystem")
    void authenticateUserTest() {
        assertTrue(system.authentificateUserLogin("firstName.lastName@testmail.com", "eventManager123"));
    }


    @Test
    @DisplayName("AddAndRemoveAdminPermissionToUser Test")
    void addAndRemoveAdminPermissionToUserTest() {
        testUser.addAdminStatusToUser();
        assertTrue(testUser.isAdmin());
        testUser.removeAdminStatusFromUser();
        assertFalse(testUser.isAdmin());
    }

    @Test
    @DisplayName("Cleaning DB after Testing")
    void cleanDBAfterTests() {
        String userID = testAdminUser.getUserByEmail("firstName.lastName@testmail.com").getUserID();
        assertTrue(testAdminUser.deleteUser(userID));
    }
}
