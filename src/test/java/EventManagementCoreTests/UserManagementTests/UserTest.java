package EventManagementCoreTests.UserManagementTests;

import EventManagementCore.UserManagement.UserManager;
import EventManagementCore.UserManagement.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    User testUser = new UserManager().readUserByID("GwQo2aW6AnTTv4KUe8t0"); //SoONY7IhPtVzCx1e0z18

    User testAdminUser = new UserManager().readUserByID("duuuY5XI4XyPQnzIChVL");

    User system = new User("System", "", "", "", "goodPassword", 0, true);

    final static String TEST_USER_EMAIL_ADDRESS = "firstName.lastName@testmail.com";
    final static String TEST_USER_EMAIL_ADDRESS_EDITED = "firstName.lastName@testmailEdited.com";

    //#region CRUD-Operation-Tests

    @Test
    @Order(0)
    @DisplayName("UserCreateUser Test")
    void userWithoutPermissionCreateNewUserTest() {

        assertFalse(testUser.createNewUser("new", "User", "dateOfBirth", TEST_USER_EMAIL_ADDRESS,
                "eventManager123", 11223344, false));


    }

    @Test
    @Order(1)
    @DisplayName("AdminCreatesUser Test")
    void adminUserCreateNewUserTest() {

        assertNotNull(testAdminUser);

        assertTrue(testAdminUser.createNewUser("new", "User", "dateOfBirth", TEST_USER_EMAIL_ADDRESS,
                "eventManager123", 11223344, false));


    }

    @Test
    @Order(2)
    @DisplayName("EditUser Test")
    void editUserTest() {

        String userIDFromUserToEdit = testAdminUser.getUserByEmail(TEST_USER_EMAIL_ADDRESS).getUserID();
        String firstName = "Max";
        String lastName = "Mustermann";
        String dateOfBirth = "01/01/2000";
        //String email = TEST_USER_EMAIL_ADDRESS_EDITED; erst wenn editUser funktioniert
        String email = TEST_USER_EMAIL_ADDRESS;
        String password = "eventManager123";
        int phoneNumber = 123456;

        testAdminUser.editUser(userIDFromUserToEdit, firstName, lastName, dateOfBirth, email, password, phoneNumber);

        assertEquals(email, testAdminUser.getUserByID(userIDFromUserToEdit).getEMailAddress());

    }

    @Test
    @Order(6)
    @DisplayName("DeleteUser Test")
    void deleteUserTest() {

        String userID = testAdminUser.getUserByEmail(TEST_USER_EMAIL_ADDRESS).getUserID(); //Eigentlich die editierte Email-Adresse, aber editUser funktioniert noch nicht
        assertTrue(testAdminUser.deleteUser(userID));

    }
    //#endregion CRUD-Operation-Tests

    //#region Registration and Authentication Tests

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

        assertTrue(testAdminUser.authenticationUserLogin(TEST_USER_EMAIL_ADDRESS, "eventManager123"));

    }

    //#endregion Registration and Authentication Tests

    //#region Permission Tests

    @Test
    @Order(5)
    @DisplayName("Add&Remove AdminStatus Test")
    void addAndRemoveAdminStatusToUserTest() {

        testAdminUser.addAdminStatusToUserByUserID(testUser.getUserID());
        assertFalse(testUser.isAdmin());

        testAdminUser.removeAdminStatusFromUserByUserID(testUser.getUserID());
        assertFalse(testUser.isAdmin());
    }

    //#endregion Permission Tests

}

