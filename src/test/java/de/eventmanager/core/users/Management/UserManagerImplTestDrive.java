package de.eventmanager.core.users.Management;


import de.eventmanager.core.database.Communication.CreatorDatabaseConnector;
import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import de.eventmanager.core.database.Communication.NotificationDatabaseConnector;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.notifications.Notification;
import de.eventmanager.core.roles.Role;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.users.User;
import org.junit.jupiter.api.*;


import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserManagerImplTestDrive {

    UserManagerImpl userManagerImpl = new UserManagerImpl();

    static User testUser;
    static User testAdminUser;
    static PublicEvent publicEvent;
    static PrivateEvent privateEvent;
    static ArrayList<String> testArrayList = new ArrayList<>();

    final static String TEST_USER_EMAIL_ADDRESS = "firstName.lastName@testmail.com";
    final static String TEST_ADMIN_EMAIL_ADDRESS = "firstName.lastName@AdminTestmail.com";
    final static String CREATE_TEST_USER_EMAIL_ADDRESS = "firstName.lastName@InternTestmail.com";
    final static String TEST_USER_EMAIL_ADDRESS_EDITED = "firstName.lastName@testmailEdited.com";
    final static String TEST_USER_ID = "123";
    final static String TEST_ADMIN_ID = "124";
    final static String TEST_PUBLIC_EVENT_ID = "123";
    final static String TEST_PRIVATE_EVENT_ID = "124";

    @BeforeAll
    static void globalSetup() {
        testUser = new User(TEST_USER_ID, "testLastName", "testFirstName", "2000-04-20",
                TEST_USER_EMAIL_ADDRESS, "password", "+497788866", false);
        testAdminUser = new User(TEST_ADMIN_ID, "firstAdmin", "Admin", "1999-04-05",
                TEST_ADMIN_EMAIL_ADDRESS, "AdminPassword", "+497733686", true);
        testAdminUser.setRoleAdmin(true);

        publicEvent = new PublicEvent(TEST_PUBLIC_EVENT_ID, "TestPublicEvent", "2000-01-01",
                "2000-01-02", 0, testArrayList, "TestCategory",
                false, "66115", "Saarbrücken", "TestStraße 6", "Turmschule",
                "This is a cool event", 20, 0);
        privateEvent = new PrivateEvent(TEST_PRIVATE_EVENT_ID, "TestPrivateEvent", "2000-01-01",
                "2000-01-02", 0, testArrayList, "TestCategory",
                true, "66115", "Saarbrücken", "TestStraße 6", "Turmschule",
                "This is a cool event");


        UserDatabaseConnector.createNewUser(testUser);
        UserDatabaseConnector.createNewUser(testAdminUser);
        EventDatabaseConnector.createNewEvent(publicEvent);
        EventDatabaseConnector.createNewEvent(privateEvent);
        CreatorDatabaseConnector.assignUserAsEventCreator(TEST_PRIVATE_EVENT_ID, TEST_ADMIN_ID);
        CreatorDatabaseConnector.assignUserAsEventCreator(TEST_PUBLIC_EVENT_ID, TEST_ADMIN_ID);

    }

    @AfterAll
    static void globalCleanup() {
        UserManagerImpl userManagerImpl = new UserManagerImpl();

        CreatorDatabaseConnector.removeUserAsEventCreator(TEST_PUBLIC_EVENT_ID, TEST_ADMIN_ID);
        CreatorDatabaseConnector.removeUserAsEventCreator(TEST_PRIVATE_EVENT_ID, TEST_ADMIN_ID);
        UserDatabaseConnector.deleteUserByEmail(TEST_ADMIN_EMAIL_ADDRESS);
        UserDatabaseConnector.deleteUserByEmail(TEST_USER_EMAIL_ADDRESS);
        EventDatabaseConnector.deleteEventByID(TEST_PUBLIC_EVENT_ID);
        EventDatabaseConnector.deleteEventByID(TEST_PRIVATE_EVENT_ID);

        ArrayList<Notification> notificationList = NotificationDatabaseConnector.readNotificationsByUserID(TEST_USER_ID);
        for (Notification notification : notificationList) {
            NotificationDatabaseConnector.deleteNotification(notification.getNotificationID());
        }

        if (userManagerImpl.getUserByEmail(TEST_USER_EMAIL_ADDRESS_EDITED).isPresent()) {
                UserDatabaseConnector.deleteUserByEmail(TEST_USER_EMAIL_ADDRESS_EDITED);
        }
        if (userManagerImpl.getUserByEmail(CREATE_TEST_USER_EMAIL_ADDRESS).isPresent()) {
            UserDatabaseConnector.deleteUserByEmail(CREATE_TEST_USER_EMAIL_ADDRESS);
        }
    }

    //#region CRUD-Operation-Tests for User
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

        String firstName = userManagerImpl.getUserByID(userIDFromUserToEdit).get().getFirstName();
        String lastName = userManagerImpl.getUserByID(userIDFromUserToEdit).get().getLastName();
        String dateOfBirth = userManagerImpl.getUserByID(userIDFromUserToEdit).get().getDateOfBirth();
        String password = userManagerImpl.getUserByID(userIDFromUserToEdit).get().getPassword();
        String phoneNumber = userManagerImpl.getUserByID(userIDFromUserToEdit).get().getPhoneNumber();


        userManagerImpl.editUser(userIDFromUserToEdit, firstName, lastName, dateOfBirth, TEST_USER_EMAIL_ADDRESS_EDITED, password, phoneNumber, TEST_ADMIN_ID);

        assertEquals(TEST_USER_EMAIL_ADDRESS_EDITED, userManagerImpl.getUserByID(userIDFromUserToEdit).get().getEMailAddress());

    }

    @Test
    @DisplayName("DeleteUser Test")
    void deleteUserTest() {
        assertTrue(userManagerImpl.deleteUser(TEST_USER_EMAIL_ADDRESS_EDITED, TEST_ADMIN_ID));
    }
    //#endregion CRUD-Operation-Tests for User

    //#region Crud-Operation-Tests for Event
    @Test
    @DisplayName("Create Public-Event Test")
    @Disabled
    void createPublicEventTest() {

        assertTrue(userManagerImpl.createNewEvent("TestPublicEventIntern", "2000-01-01",
                "2000-01-02", "TestCategory", "66115",
                "TestStraße 6", "Turmschule", "This is a cool event", 20,
                false, TEST_ADMIN_ID));
    }

    @Test
    @DisplayName("Create Private Event Test")
    @Disabled
    void createPrivateEventTest() {

        assertTrue(userManagerImpl.createNewEvent("TestPrivateEventIntern", "2000-01-01",
                "2000-01-02", "TestCategory", "66115",
                "TestStraße 6", "Turmschule", "This is a cool event", 20,
                true, TEST_ADMIN_ID));
    }

    @Test
    @DisplayName("Edit Event Test")
    void editEventTest() {

        userManagerImpl.editEvent(TEST_PRIVATE_EVENT_ID, "TestEventEdited", "01-01-2021",
                "01/01/2021", "Test1", "66578","Teststraße 177",
                "TestLocation1", "TestDescription1", TEST_ADMIN_ID);

        assertEquals("TestEventEdited", userManagerImpl.getEventByID(TEST_PRIVATE_EVENT_ID).get().getEventName());
    }

    @Test
    @DisplayName("Delete Event Test")
    @Disabled
    void deleteEventTest() {
        //Todo: if unique event-parameter excepting the id exist
        userManagerImpl.deleteEvent(EventDatabaseConnector.readPublicEventsByName("TestPublicEventIntern").get(0).getEventID(), TEST_ADMIN_ID);
        //userManagerImpl.deleteEvent(EventDatabaseConnector.readPr("TestPrivateEventIntern").get(0).getEventID(), TEST_ADMIN_ID);
    }
    //#endregion Crud-Operation-Tests for Event

    //#region Event-Operations
    @Test
    @Order(0)
    @DisplayName("Book Event Test")
    void bookEventTest() {
        assertTrue(userManagerImpl.bookEvent(TEST_PUBLIC_EVENT_ID, TEST_USER_ID));
        assertTrue(userManagerImpl.bookEvent(TEST_PUBLIC_EVENT_ID, TEST_ADMIN_ID));
        assertFalse(userManagerImpl.bookEvent(TEST_PRIVATE_EVENT_ID, TEST_USER_ID));

        System.out.println(userManagerImpl.showEventParticipantList(TEST_PUBLIC_EVENT_ID));
    }

    @Test
    @Order(1)
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
        assertFalse(userManagerImpl.addUserToEvent(TEST_PRIVATE_EVENT_ID,TEST_USER_EMAIL_ADDRESS_EDITED,TEST_USER_ID));
        assertFalse(userManagerImpl.removeUserFromEvent(TEST_PRIVATE_EVENT_ID,TEST_USER_EMAIL_ADDRESS_EDITED, TEST_USER_ID));

        assertTrue(userManagerImpl.addUserToEvent(TEST_PRIVATE_EVENT_ID,TEST_USER_EMAIL_ADDRESS_EDITED,TEST_ADMIN_ID));
        assertTrue(userManagerImpl.removeUserFromEvent(TEST_PRIVATE_EVENT_ID,TEST_USER_EMAIL_ADDRESS_EDITED, TEST_ADMIN_ID));
    }
    //#endregion Event-Operations

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

    @Test
    @DisplayName("ExportEvents-Test")
    @Disabled
    void exportTest() {
        assertTrue(userManagerImpl.exportEvents());
    }

}

