package EventManagementCoreTests.UserManagementTests;

import EventManagementCore.DatabaseCommunication.UserManager;
import EventManagementCore.UserManagement.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    User testUser = new UserManager().readUserByID("GwQo2aW6AnTTv4KUe8t0"); //SoONY7IhPtVzCx1e0z18

    User testAdminUser = new UserManager().readUserByID("duuuY5XI4XyPQnzIChVL");

    User system = new User("System", "", "", "", "goodPassword",0,true);


    @Test
    @Order(0)
    @DisplayName("UserCreateUser Test")
    void userWithoutPermissionCreateNewUserTest() {

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
        String email = "max.max@testmail.com";
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

        assertFalse(system.isValidRegistrationPassword(inValidTestPassword, "eventManagerÄ"));

        assertTrue(system.isValidRegistrationPassword(validTestPassword, "eventManager123"));


    }

    @Test
    @Order(4)
    @DisplayName("Login-System Test")
    void authenticateUserLoginTest() {

        assertTrue(testAdminUser.authentificateUserLogin("firstName.lastName@testmail.com", "eventManager123"));

    }

    @Test
    @Order(5)
    @DisplayName("Add&Remove AdminStatus Test")
    void addAndRemoveAdminStatusToUserTest() {

        testAdminUser.addAdminStatusToUser(testUser);
        assertTrue(testUser.isAdmin());

        testAdminUser.removeAdminStatusFromUser(testUser);
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
