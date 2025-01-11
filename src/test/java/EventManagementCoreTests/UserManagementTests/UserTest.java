package EventManagementCoreTests.UserManagementTests;

import EventManagementCore.UserManagement.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    User testUser = new User("1234","firstName", "LastName", "dateOfBirth",
            "firstTestName.lastTestName@testmail.com", "eventManager123", 5566778,false);

    User testAdminUser = new User("1235","admin","trator","dateOfBirth",
            "firstTestName@adminmail.com", "AdminEventManager123", 5566778, true);

    User system = new User("System", "", "", "", "goodPassword",0,true);


    @Test
    @Order(0)
    @DisplayName("UserCreateUser Test")
    void userWithoutPermissionCreateNewUserTest() {

        assertNotNull(testUser);

        assertFalse(testUser.createNewUser("new","User", "dateOfBirth", "firstName.lastName@testmail.com",
                "eventManager123", 11223344, false));

    }

    @Test
    @Order(1)
    @DisplayName("AdminCreatesUser Test")
    void adminUserCreateNewUserTest() {

        assertNotNull(testAdminUser);

        assertTrue(testAdminUser.createNewUser("new","User", "dateOfBirth", "firstName.lastName@testmail.com",
                "eventManager123", 11223344, false));

    }

    @Test
    @Order(2)
    @DisplayName("EditUser Test")
    void editUserTest() {

        String userIDFromUserToEdit = testAdminUser.getUserByEmail("firstName.lastName@testmail.com").getUserID();
        String firstName = "Max";
        String lastName = "Mustermann";
        String dateOfBirth = "01/01/2000";
        String email = "firstName.lastName@testmail.com";
        String password = "eventManager123";
        int phoneNumber = 123456;

        testAdminUser.editUser(userIDFromUserToEdit, firstName, lastName, dateOfBirth, email, password, phoneNumber);

        assertEquals(email, testAdminUser.getUserByID(userIDFromUserToEdit).getEMailAddress());

    }

    @Test
    @Order(3)
    @DisplayName("Password-Registration Test")
    void isValidAndIsNotValidRegistrationPasswordTest() {

        String validTestPassword = "eventManager123";
        String inValidTestPassword = "eventManagerÄ";

        assertFalse(testUser.isValidRegistrationPassword(inValidTestPassword, "eventManagerÄ"));

        assertTrue(testUser.isValidRegistrationPassword(validTestPassword, "eventManager123"));


    }

    @Test
    @Order(4)
    @DisplayName("Login-System Test")
    void authenticateUserLoginTest() {

        assertTrue(system.authentificateUserLogin("firstName.lastName@testmail.com", "eventManager123"));

    }

    @Test
    @Order(5)
    @DisplayName("Add&Remove AdminStatus Test")
    void addAndRemoveAdminStatusToUserTest() {

        testUser.addAdminStatusToUser();
        assertTrue(testUser.isAdmin());

        testUser.removeAdminStatusFromUser();
        assertFalse(testUser.isAdmin());
    }

    @Test
    @Order(6)
    @DisplayName("Cleaning DB after Testing")
    void cleanDBAfterTests() {

        String userID = testAdminUser.getUserByEmail("firstName.lastName@testmail.com").getUserID();
        assertTrue(testAdminUser.deleteUser(userID));
    }
}
