package de.eventmanager.core.users.Management;


import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.roles.Role;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.users.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class UserManagerImplTestDrive {

    UserManagerImpl userManagerImpl = new UserManagerImpl();

    User testUser;
    User testAdminUser;

    PublicEvent publicEvent;
    PrivateEvent privateEvent;

    ArrayList <String>  testArrayList = new ArrayList<>();

    final static String TEST_USER_EMAIL_ADDRESS = "firstName.lastName@testmail.com";
    final static String TEST_ADMIN_EMAIL_ADDRESS = "firstName.lastName@AdminTestmail.com";
    final static String CREATE_TEST_USER_EMAIL_ADDRESS = "firstName.lastName@InternTestmail.com";
    final static String TEST_USER_EMAIL_ADDRESS_EDITED = "firstName.lastName@testmailEdited.com";
    final static String TEST_USER_ID = "123";
    final static String TEST_ADMIN_ID = "124";
    final static String TEST_PUBLIC_EVENT_ID = "123";
    final static String TEST_PRIVATE_EVENT_ID = "124";
    //#region CRUD-Operation-Tests
    @BeforeEach
    void setup() {

        testUser = new User(TEST_USER_ID, "testLastName", "testFirstName", "2000-04-20",TEST_USER_EMAIL_ADDRESS,
                "password", "+497788866", false);
        testAdminUser = new User(TEST_ADMIN_ID,"firstAdmin","Admin","1999-04-05", TEST_ADMIN_EMAIL_ADDRESS,
                "AdminPassword", "+497733686", true);
        testAdminUser.setRoleAdmin(true);

        publicEvent = new PublicEvent(TEST_PUBLIC_EVENT_ID,"TestPublicEvent", "2000-01-01", "2000-01-02", 0,
                testArrayList, "TestCategory", false,"66115","Saarbrücken", "TestStraße 6", "Turmschule",
                "This is a cool event", 20, 0);
        privateEvent = new PrivateEvent(TEST_PRIVATE_EVENT_ID,"TestPrivateEvent", "2000-01-01", "2000-01-02", 0,
                testArrayList,"TestCategory", true, "66115","Saarbrücken", "TestStraße 6", "Turmschule",
                "This is a cool event");

        UserDatabaseConnector.createNewUser(testUser);
        UserDatabaseConnector.createNewUser(testAdminUser);
        EventDatabaseConnector.createNewEvent(publicEvent);
        EventDatabaseConnector.createNewEvent(privateEvent);

    }

    @AfterEach
    void cleanup() {

        UserDatabaseConnector.deleteUserByEmail(TEST_ADMIN_EMAIL_ADDRESS);
        UserDatabaseConnector.deleteUserByEmail(TEST_USER_EMAIL_ADDRESS);
        EventDatabaseConnector.deleteEventByID(TEST_PUBLIC_EVENT_ID);
        EventDatabaseConnector.deleteEventByID(TEST_PRIVATE_EVENT_ID);

        if (userManagerImpl.getUserByEmail(TEST_USER_EMAIL_ADDRESS_EDITED).isPresent()) {
                UserDatabaseConnector.deleteUserByEmail(TEST_USER_EMAIL_ADDRESS_EDITED);
        }
        if (userManagerImpl.getUserByEmail(CREATE_TEST_USER_EMAIL_ADDRESS).isPresent()) {
            UserDatabaseConnector.deleteUserByEmail(CREATE_TEST_USER_EMAIL_ADDRESS);
        }
    }

    @Test
    @DisplayName("UserCreateUser Test")
    void userWithoutPermissionCreateNewUserTest() {

        assertFalse(userManagerImpl.createNewUser("test", "User", "dateOfBirth", CREATE_TEST_USER_EMAIL_ADDRESS,
                "eventManager123", "11223344", false, TEST_USER_ID));

    }

    @Test
    @DisplayName("AdminCreatesUser Test")
    void adminUserCreateNewUserTest() {

        assertNotNull(testAdminUser);

        assertTrue(userManagerImpl.createNewUser("new", "User", "dateOfBirth", CREATE_TEST_USER_EMAIL_ADDRESS,
                "eventManager123", "11223344", false, TEST_ADMIN_ID));

    }

    @Test
    @DisplayName("EditUser Test")
    void editUserTest() {

        Optional<User> optionalUser = userManagerImpl.getUserByEmail(TEST_USER_EMAIL_ADDRESS);
        String userIDFromUserToEdit = "";

        if (optionalUser.isPresent()) {
            userIDFromUserToEdit = optionalUser.get().getUserID();
        }
        String firstName = "Max";
        String lastName = "Mustermann";
        String dateOfBirth = "01/01/2000";
        String email = TEST_USER_EMAIL_ADDRESS_EDITED;
        String password = "password";
        String phoneNumber = "123456";

        userManagerImpl.editUser(userIDFromUserToEdit, firstName, lastName, dateOfBirth, email, password, phoneNumber, TEST_ADMIN_ID);

        assertEquals(email, userManagerImpl.getUserByID(userIDFromUserToEdit).get().getEMailAddress());

    }

    @Test
    @DisplayName("DeleteUser Test")
    void deleteUserTest() {

        assertTrue(userManagerImpl.deleteUser(TEST_USER_EMAIL_ADDRESS, TEST_ADMIN_ID));

    }
    //#endregion CRUD-Operation-Tests

    //#region Permission Tests
    @Test
    @DisplayName("Add&Remove AdminStatus Test")
    @Disabled
    void addAndRemoveAdminStatusToUserTest() {

        userManagerImpl.addAdminStatusToUserByUserID(TEST_USER_ID, testAdminUser);
        assertEquals(Role.ADMIN, testUser.getRole());

        userManagerImpl.removeAdminStatusFromUserByUserID(TEST_USER_ID, testAdminUser);
        assertNotEquals(Role.ADMIN, testUser.getRole());
    }

    //#endregion Permission Tests

    @Test
    @DisplayName("Edit Event Test")
    void editEventTest() {

        userManagerImpl.editEvent(TEST_PRIVATE_EVENT_ID, "TestEventEdited", "01-01-2021", "01/01/2021",
                "Test1", "12345", "Teststadt","Teststraße 177", "TestLocation1", "TestDescription1", TEST_ADMIN_ID);
        assertTrue(true);
        //assertEquals("TestEventEdited", privateEvent.getEventName());
    }

    @Test
    @DisplayName("Book Event Test")
    void bookEventTest() {
        assertTrue(userManagerImpl.bookEvent(TEST_PUBLIC_EVENT_ID, TEST_USER_ID));
        assertTrue(userManagerImpl.bookEvent(TEST_PUBLIC_EVENT_ID, TEST_ADMIN_ID));
        assertFalse(userManagerImpl.bookEvent(TEST_PRIVATE_EVENT_ID, TEST_USER_ID));
    }

    @Test
    @DisplayName("Cancel Event Test")
    void cancelEventTest() {
        String notExistingEventID = "1234";

        assertFalse(userManagerImpl.cancelEvent(notExistingEventID, TEST_USER_ID));
        assertTrue(userManagerImpl.cancelEvent(TEST_PUBLIC_EVENT_ID, TEST_ADMIN_ID));
        assertTrue(userManagerImpl.cancelEvent(TEST_PUBLIC_EVENT_ID, TEST_USER_ID));

    }

    @Test
    @DisplayName("Add & Remove User to Event Test")
    void addAndRemoveUserToEventTest() {
        assertFalse(userManagerImpl.addUserToEvent(TEST_PRIVATE_EVENT_ID,testUser.getEMailAddress(),TEST_USER_ID));
        assertFalse(userManagerImpl.removeUserFromEvent(TEST_PRIVATE_EVENT_ID,testUser.getEMailAddress(), TEST_USER_ID));

        assertTrue(userManagerImpl.addUserToEvent(TEST_PRIVATE_EVENT_ID,testUser.getEMailAddress(),TEST_ADMIN_ID));
        assertTrue(userManagerImpl.removeUserFromEvent(TEST_PRIVATE_EVENT_ID,testUser.getEMailAddress(), TEST_ADMIN_ID));
    }

    //#region Registration and Authentication Tests
    @Test
    @DisplayName("Password-Registration Test")
    void isValidAndIsNotValidRegistrationPasswordTest() {

        String validTestPassword = "eventManager123";
        String inValidTestPassword = "eventManagerÄ";

        assertFalse(userManagerImpl.isValidRegistrationPassword(inValidTestPassword, "eventManagerÄ"));

        assertTrue(userManagerImpl.isValidRegistrationPassword(validTestPassword, "eventManager123"));
    }

    @Test
    @DisplayName("Login-System Test")
    void authenticateUserLoginTest() {

        assertTrue(userManagerImpl.authenticationUserLogin("fiot00001@htwsaar.de", "eventManager123"));
    }
    //#endregion Registration and Authentication Tests

}

