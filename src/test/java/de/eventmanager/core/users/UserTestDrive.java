package de.eventmanager.core.users;


import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.Management.EventManager;
import de.eventmanager.core.roles.Role;
import de.eventmanager.core.users.Management.UserManager;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTestDrive {

    User testUser = UserManager.readUserByID("f8cac5f6-107a-4487-96f8-add26c5b8579").get(); //SoONY7IhPtVzCx1e0z18

    User testAdminUser = UserManager.readUserByID("10b4841b-c5e0-4e13-a861-6f65f6c4086b").get();

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
        assertTrue(!testUser.getRole().equals(Role.ADMIN));

        testAdminUser.removeAdminStatusFromUserByUserID(testUser.getUserID());
        assertFalse(testUser.getRole().equals(Role.ADMIN));
    }
    //#endregion Permission Tests

}

