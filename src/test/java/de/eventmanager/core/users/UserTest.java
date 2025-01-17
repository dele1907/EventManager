package de.eventmanager.core.users;


import de.eventmanager.core.users.Management.UserManager;
import de.eventmanager.core.users.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    User testUser = UserManager.readUserByID("GwQo2aW6AnTTv4KUe8t0").get(); //SoONY7IhPtVzCx1e0z18

    User testAdminUser = UserManager.readUserByID("iwbLeZWwmrg5E0oC8KIs").get();

    User system = new User("System", "", "", "", "goodPassword", 0, true);

    final static String TEST_USER_EMAIL_ADDRESS = "firstName.lastName@testmail.com";
    final static String TEST_USER_EMAIL_ADDRESS_EDITED = "firstName.lastName@testmailEdited.com";

    //#region CRUD-Operation-Tests

    @Test
    @Order(0)
    @DisplayName("UserCreateUser Test")
    void userWithoutPermissionCreateNewUserTest() {

        assertFalse(testUser.createNewUser("test", "User", "dateOfBirth", TEST_USER_EMAIL_ADDRESS,
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

        String userIDFromUserToEdit = testAdminUser.getUserByEmail(TEST_USER_EMAIL_ADDRESS).get().getUserID();
        String firstName = "Markus";
        String lastName = "Mustermann";
        String dateOfBirth = "01/01/2000";
        String email = TEST_USER_EMAIL_ADDRESS_EDITED; //erst wenn editUser funktioniert
        //String email = TEST_USER_EMAIL_ADDRESS;
        String password = "eventManager123";
        int phoneNumber = 123456;

        testAdminUser.editUser(userIDFromUserToEdit, firstName, lastName, dateOfBirth, email, password, phoneNumber);

        assertEquals(email, testAdminUser.getUserByID(userIDFromUserToEdit).get().getEMailAddress());

    }

    @Test
    @Order(4)
    @DisplayName("DeleteUser Test")
    void deleteUserTest() {

        String userID = testAdminUser.getUserByEmail(TEST_USER_EMAIL_ADDRESS_EDITED).get().getUserID(); //Eigentlich die editierte Email-Adresse, aber editUser funktioniert noch nicht
        assertTrue(testAdminUser.deleteUser(userID));

    }
    //#endregion CRUD-Operation-Tests

    //#region Permission Tests
    //Todo @Finn @Timo ! entfernen sobald nicht mehr mit DummyUsern gearbeitet/getestet wird
    @Test
    @Order(3)
    @DisplayName("Add&Remove AdminStatus Test")
    void addAndRemoveAdminStatusToUserTest() {

        testAdminUser.addAdminStatusToUserByUserID(testUser.getUserID());
        assertTrue(!testUser.isAdmin());

        testAdminUser.removeAdminStatusFromUserByUserID(testUser.getUserID());
        assertFalse(testUser.isAdmin());
    }

    //#endregion Permission Tests

}

