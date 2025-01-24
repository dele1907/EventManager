package de.eventmanager.core.users;


import de.eventmanager.core.database.Communication.EventDataBaseConnector;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.roles.Role;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.users.Management.UserManagerImpl;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTestDrive {

    UserManagerImpl userManagerImpl = new UserManagerImpl();

    User testUser = UserDatabaseConnector.readUserByID("GwQo2aW6AnTTv4KUe8t0").get();

    User testAdminUser = UserDatabaseConnector.readUserByID("iwbLeZWwmrg5E0oC8KIs").get();


    PublicEvent publicEvent = EventDataBaseConnector.readPublicEventByID("123").get();
    PrivateEvent privateEvent = EventDataBaseConnector.readPrivateEventByID("124").get();

    final static String TEST_USER_EMAIL_ADDRESS = "firstName.lastName@testmail.com";
    final static String TEST_USER_EMAIL_ADDRESS_EDITED = "firstName.lastName@testmailEdited.com";



    //#region CRUD-Operation-Tests

    @Test
    @Order(0)
    @DisplayName("UserCreateUser Test")
    void userWithoutPermissionCreateNewUserTest() {

        assertFalse(userManagerImpl.createNewUser("test", "User", "dateOfBirth", TEST_USER_EMAIL_ADDRESS,
                "eventManager123", "11223344", false, testUser.getUserID()));

    }

    @Test
    @Order(1)
    @DisplayName("AdminCreatesUser Test")
    void adminUserCreateNewUserTest() {

        assertNotNull(testAdminUser);

        assertTrue(userManagerImpl.createNewUser("new", "User", "dateOfBirth", TEST_USER_EMAIL_ADDRESS,
                "eventManager123", "11223344", false, testAdminUser.getUserID()));


    }

    @Test
    @Order(2)
    @DisplayName("EditUser Test")
    void editUserTest() {

        String userIDFromUserToEdit = userManagerImpl.getUserByEmail(TEST_USER_EMAIL_ADDRESS, testAdminUser).get().getUserID();
        String firstName = "Max";
        String lastName = "Mustermann";
        String dateOfBirth = "01/01/2000";
        String email = TEST_USER_EMAIL_ADDRESS_EDITED;
        String password = "password";
        String phoneNumber = "123456";

        userManagerImpl.editUser(userIDFromUserToEdit, firstName, lastName, dateOfBirth, email, password, phoneNumber, testAdminUser);

        assertEquals(email, userManagerImpl.getUserByID(userIDFromUserToEdit, testAdminUser).get().getEMailAddress());

    }

    @Test
    @Order(8)
    @DisplayName("DeleteUser Test")
    void deleteUserTest() {

        assertTrue(userManagerImpl.deleteUser(TEST_USER_EMAIL_ADDRESS_EDITED, testAdminUser));

    }
    //#endregion CRUD-Operation-Tests

    //#region Permission Tests
    @Test
    @Order(3)
    @DisplayName("Add&Remove AdminStatus Test")
    void addAndRemoveAdminStatusToUserTest() {

        userManagerImpl.addAdminStatusToUserByUserID(testUser.getUserID(), testAdminUser);
        assertTrue(!testUser.getRole().equals(Role.ADMIN));

        userManagerImpl.removeAdminStatusFromUserByUserID(testUser.getUserID(), testAdminUser);
        assertFalse(testUser.getRole().equals(Role.ADMIN));
    }

    //#endregion Permission Tests

    @Test
    @Order(4)
    @DisplayName("Create,Edit & Delete Event Test")
    void createEditDeleteEventTest() {

        PrivateEvent privateEventToEdit = userManagerImpl.createPrivateEvent("privateTestEventToEdit", "01/01/2021", "01/01/2021",
                "Test", "12345", "Teststraße 1", "TestLocation", "TestDescription",testAdminUser).get();

        assertFalse(userManagerImpl.editEvent(privateEventToEdit.getEventID(), "TestEventEdited", "01/01/2021", "01/01/2021",
                "Test1", "12345", "Teststraße 177", "TestLocation1", "TestDescription1", testAdminUser));

        assertTrue(userManagerImpl.deleteEvent(privateEventToEdit.getEventID(), testAdminUser));
        assertFalse(userManagerImpl.deleteEvent(privateEventToEdit.getEventID(), testAdminUser)); //Check if the event is really deleted
    }

    @Test
    @Order(5)
    @DisplayName("Book Event Test")
    void bookEventTest() {
        assertTrue(userManagerImpl.bookEvent(publicEvent.getEventID(), testUser));
        assertTrue(userManagerImpl.bookEvent(publicEvent.getEventID(), testAdminUser));
        assertFalse(userManagerImpl.bookEvent(privateEvent.getEventID(), testUser));
    }

    @Test
    @Order(7)
    @DisplayName("Cancel Event Test")
    void cancelEventTest() {
        String notExistingEventID = "1234";

        assertFalse(userManagerImpl.cancelEvent(notExistingEventID, testUser));
        assertTrue(userManagerImpl.cancelEvent(publicEvent.getEventID(), testAdminUser));
        assertTrue(userManagerImpl.cancelEvent(publicEvent.getEventID(), testUser));

    }

    @Test
    @Order(6)
    @DisplayName("Show All Event-Participants")
    void printAllEventParticipants(){
        assertTrue(true);
       // System.out.println(userManagerImpl.showAllEventParticipants(publicEvent.getEventID(), testAdminUser));
    }



}

