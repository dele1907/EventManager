package EventManagementCoreTests.UserManagementTests;

import EventManagementCore.UserManagement.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    User testUser = new User("1234","firstName", "LastName", "dateOfBirth",
            "firstTestName.lastTestName@testmail.com", "eventManager123", 5566778,false);
    User testAdminUser = new User("1235","admin","trator","dateOfBirth",
            "firstTestName@adminmail.com", "AdminEventManager123", 5566778, true);
    User system = new User("System", "", "", "", "goodPassword",0,true);

    String validTestPassword = "eventManager123";
    String inValidTestPassword = "eventManagerÄ";
    String incorrectPassword = "eventManager124";

    @Test
    @Order(1)
    @DisplayName("UserCreateUser Test")
    void userWithoutPermissionCreateNewUserTest() {


        assertFalse(testUser.createNewUser("new","User", "dateOfBirth", "firstName.lastName@testmail.com",
                "eventManager123", 11223344, false));

    }
    @Test
    @Order(2)
    @DisplayName("AdminCreatesUser Test")
    void adminUserCreateNewUserTest() {

        assertTrue(testAdminUser.createNewUser("new","User", "dateOfBirth", "firstName.lastName@testmail.com",
                "eventManager123", 11223344, false));

    }



    @Test
    @Order(3)
    @DisplayName("Password-Registration Test")
    void isValidAndIsNotValidRegistrationPasswordTest() {

        assertTrue(testUser.isValidRegistrationPassword(validTestPassword, "eventManager123"));

        assertFalse(testUser.isValidRegistrationPassword(inValidTestPassword, "eventManager123"));


    }

    @Test
    @Order(4)
    @DisplayName("Passwort-Login-Test")
    void isValidPasswordForLogin() {

        assertFalse(testUser.isValidRegistrationPassword(incorrectPassword, "eventManager123"));

    }

    @Test
    @Order(5)
    @DisplayName("Existing-Email Test")
    void checkExistingEmailAddressForUserTest() {

        assertTrue(system.comparingEmailAddress("firstName.lastName@testmail.com"));
    }

    @Test
    @Order(6)
    @DisplayName("Login-System Test")
    void authenticateUserLoginTest() {
        assertTrue(system.authentificateUserLogin("firstName.lastName@testmail.com", "eventManager123"));
    }


    @Test
    @Order(7)
    @DisplayName("Add&Remove AdminStatus Test")
    void addAndRemoveAdminPermissionToUserTest() {
        testUser.addAdminStatusToUser();
        assertTrue(testUser.isAdmin());
        testUser.removeAdminStatusFromUser();
        assertFalse(testUser.isAdmin());
    }

    @Test
    @Order(8)
    @DisplayName("Cleaning DB after Testing")
    void cleanDBAfterTests() {
        String userID = testAdminUser.getUserByEmail("firstName.lastName@testmail.com").getUserID();
        assertTrue(testAdminUser.deleteUser(userID));
    }
}
