package de.eventmanager.core.users.Management;


import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.roles.Role;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.users.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserManagerImplTestDrive {

    UserManagerImpl userManagerImpl = new UserManagerImpl();

    User testUser;
    User testAdminUser;

    PublicEvent publicEvent;
    PrivateEvent privateEvent;

    final static String TEST_USER_EMAIL_ADDRESS = "firstName.lastName@testmail.com";
    final static String TEST_USER_EMAIL_ADDRESS_EDITED = "firstName.lastName@testmailEdited.com";



    //#region CRUD-Operation-Tests
    @BeforeEach
    void setup() {
        testUser = new User("123", "testLastName", "testFirstName", TEST_USER_EMAIL_ADDRESS,
                "password", "+497788866", false);
        testAdminUser = new User("124","Admin","1999/04/05", "firstName.lastName@testmail.com",
                "AdminPassword", "+4977336866", true);
        testAdminUser.setRoleAdmin(true);

        publicEvent = new PublicEvent("TestPublicEvent", "2000/01/01", "2000/01/02", "TestCategory",
                "66115","Saarbrücken", "TestStraße 6", "Turmschule", "This is a cool event", 20);
        privateEvent = new PrivateEvent("TestPrivateEvent", "2000/01/01", "2000/01/02", "TestCategory",
                "66115","Saarbrücken", "TestStraße 6", "Turmschule", "This is a cool event");

    }
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

        String userIDFromUserToEdit = testUser.getUserID();
        String firstName = "Max";
        String lastName = "Mustermann";
        String dateOfBirth = "01/01/2000";
        String email = TEST_USER_EMAIL_ADDRESS_EDITED;
        String password = "password";
        String phoneNumber = "123456";

        userManagerImpl.editUser(userIDFromUserToEdit, firstName, lastName, dateOfBirth, email, password, phoneNumber, testAdminUser);

        assertEquals(email, testUser.getEMailAddress());

    }

    @Test
    @Order(11)
    @DisplayName("DeleteUser Test")
    void deleteUserTest() {

        assertTrue(userManagerImpl.deleteUser(TEST_USER_EMAIL_ADDRESS_EDITED, testAdminUser.getUserID()));

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
                "Test", "12345", "Teststadt", "Teststraße 1", "TestLocation", "TestDescription",testAdminUser).get();

        assertTrue(userManagerImpl.editEvent(privateEventToEdit.getEventID(), "TestEventEdited", "01/01/2021", "01/01/2021",
                "Test1", "12345", "Teststadt","Teststraße 177", "TestLocation1", "TestDescription1", testAdminUser));

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
    @Order(6)
    @DisplayName("Show All Event-Participants")
    void printAllEventParticipants(){
        assertTrue(true);
        System.out.println(userManagerImpl.showEventParticipantList(publicEvent.getEventID()));
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
    @Order(8)
    @DisplayName("Add & Remove User to Event Test")
    void addAndRemoveUserToEventTest() {
        assertFalse(userManagerImpl.addUserToEvent(privateEvent.getEventID(),testUser.getEMailAddress(),testUser.getUserID()));
        assertFalse(userManagerImpl.removeUserFromEvent(privateEvent.getEventID(),testUser.getEMailAddress(), testUser.getUserID()));

        assertTrue(userManagerImpl.addUserToEvent(privateEvent.getEventID(),testUser.getEMailAddress(),testAdminUser.getUserID()));
        assertTrue(userManagerImpl.removeUserFromEvent(privateEvent.getEventID(),testUser.getEMailAddress(), testAdminUser.getUserID()));
    }

    //#region Registration and Authentication Tests
    @Test
    @Order(9)
    @DisplayName("Password-Registration Test")
    void isValidAndIsNotValidRegistrationPasswordTest() {

        String validTestPassword = "eventManager123";
        String inValidTestPassword = "eventManagerÄ";

        assertFalse(userManagerImpl.isValidRegistrationPassword(inValidTestPassword, "eventManagerÄ"));

        assertTrue(userManagerImpl.isValidRegistrationPassword(validTestPassword, "eventManager123"));
    }

    @Test
    @Order(10)
    @DisplayName("Login-System Test")
    void authenticateUserLoginTest() {

        assertTrue(userManagerImpl.authenticationUserLogin("fiot00001@htwsaar.de", "eventManager123"));
    }
    //#endregion Registration and Authentication Tests

}

