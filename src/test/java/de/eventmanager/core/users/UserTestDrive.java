package de.eventmanager.core.users;


import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.Management.EventManager;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.roles.Role;
import de.eventmanager.core.users.Management.UserManager;
import jdk.jfr.Event;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTestDrive {

    User testUser = UserManager.readUserByID("GwQo2aW6AnTTv4KUe8t0").get(); //SoONY7IhPtVzCx1e0z18

    User testAdminUser = UserManager.readUserByID("iwbLeZWwmrg5E0oC8KIs").get();





    final static String TEST_USER_EMAIL_ADDRESS = "firstName.lastName@testmail.com";
    final static String TEST_USER_EMAIL_ADDRESS_EDITED = "firstName.lastName@testmailEdited.com";



    //#region CRUD-Operation-Tests

    @Test
    @Order(0)
    @DisplayName("UserCreateUser Test")
    void userWithoutPermissionCreateNewUserTest() {

        assertFalse(testUser.createNewUser("test", "User", "dateOfBirth", TEST_USER_EMAIL_ADDRESS,
                "eventManager123", "11223344", false));

    }

    @Test
    @Order(1)
    @DisplayName("AdminCreatesUser Test")
    void adminUserCreateNewUserTest() {

        assertNotNull(testAdminUser);

        assertTrue(testAdminUser.createNewUser("new", "User", "dateOfBirth", TEST_USER_EMAIL_ADDRESS,
                "eventManager123", "11223344", false));


    }

    @Test
    @Order(2)
    @DisplayName("EditUser Test")
    void editUserTest() {

        String userIDFromUserToEdit = testAdminUser.getUserByEmail(TEST_USER_EMAIL_ADDRESS).get().getUserID();
        String firstName = "Markus";
        String lastName = "Mustermann";
        String dateOfBirth = "01/01/2000";
        String email = TEST_USER_EMAIL_ADDRESS_EDITED;
        String password = "eventManager123";
        String phoneNumber = "123456";

        testAdminUser.editUser(userIDFromUserToEdit, firstName, lastName, dateOfBirth, email, password, phoneNumber);

        assertEquals(email, testAdminUser.getUserByID(userIDFromUserToEdit).get().getEMailAddress());

    }

    @Test
    @Order(7)
    @DisplayName("DeleteUser Test")
    void deleteUserTest() {

        String userID = testAdminUser.getUserByEmail(TEST_USER_EMAIL_ADDRESS_EDITED).get().getUserID(); //Eigentlich die editierte Email-Adresse, aber editUser funktioniert noch nicht
        assertTrue(testAdminUser.deleteUser(userID));

    }
    //#endregion CRUD-Operation-Tests

    //#region Permission Tests
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

    @Test
    @Order(4)
    @DisplayName("Edit Event Test")
    void editEventTest() {

        PrivateEvent privateEvent = testAdminUser.createPrivateEvent("privateTestEvent", "01/01/2021", "01/01/2021",
                "Test", "12345", "Teststraße 1", "TestLocation", "TestDescription").get();


        assertTrue(testAdminUser.editEvent(privateEvent.getEventID(), "TestEvent", "01/01/2021", "01/01/2021", "Test1", "12345", "Teststraße 177", "TestLocation1", "TestDescription1"));

        assertTrue(testAdminUser.deleteEvent(privateEvent.getEventID()));
        assertFalse(testAdminUser.deleteEvent(privateEvent.getEventID())); //Check if the event is really deleted
    }

    @Test
    @Order(5)
    @DisplayName("Book Event Test")
    void bookEventTest() {

        PublicEvent publicEvent = testAdminUser.createPublicEvent("publicTestEvent", "01/01/2021", "01/01/2021", "Test",
                "12345", "Teststraße 1", "TestLocation", "TestDescription", 20).get();
        PrivateEvent privateEvent = testAdminUser.createPrivateEvent("privateTestEvent", "01/01/2021", "01/01/2021",
                "Test", "12345", "Teststraße 1", "TestLocation", "TestDescription").get();

        assertTrue(testUser.bookEvent(publicEvent.getEventID()));
        assertFalse(testUser.bookEvent(privateEvent.getEventID()));
        //DB Clean
        assertTrue(testAdminUser.deleteEvent(privateEvent.getEventID()));
        assertFalse(testAdminUser.deleteEvent(publicEvent.getEventID()));
    }

    @Test
    @Order(6)
    @DisplayName("Cancel Event Test")
    void cancelEventTest() {
        PublicEvent publicEvent = testAdminUser.createPublicEvent("publicTestEvent", "01/01/2021", "01/01/2021", "Test",
                "12345", "Teststraße 1", "TestLocation", "TestDescription", 20).get();
        PrivateEvent privateEvent = testAdminUser.createPrivateEvent("privateTestEvent", "01/01/2021", "01/01/2021",
                "Test", "12345", "Teststraße 1", "TestLocation", "TestDescription").get();

        String notExistingEventID = "1234";

        assertTrue(testUser.cancelEvent(privateEvent.getEventID()));
        assertTrue(testUser.cancelEvent(privateEvent.getEventID()));
        assertFalse(testUser.cancelEvent(notExistingEventID));

        assertTrue(testAdminUser.deleteEvent(privateEvent.getEventID()));
        assertFalse(testAdminUser.deleteEvent(publicEvent.getEventID()));
    }



}

