package de.eventmanager.core.users.Management;

import de.eventmanager.core.database.Communication.*;
import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.notifications.Notification;
import de.eventmanager.core.roles.Role;
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
    static User testEventCreaotr;
    static EventModel publicEvent;
    static EventModel privateEvent;
    static EventModel eventToDelete;
    static ArrayList<String> testArrayList = new ArrayList<>();

    final static String TEST_USER_EMAIL_ADDRESS = "firstName.lastName@testmail.com";
    final static String TEST_ADMIN_EMAIL_ADDRESS = "firstName.lastName@AdminTestmail.com";
    final static String TEST_EVENT_CREATOR_EMAIL_ADDRESS = "event.creator@testmail.com";
    final static String CREATE_TEST_USER_EMAIL_ADDRESS = "firstName.lastName@InternTestmail.com";
    final static String TEST_USER_EMAIL_ADDRESS_EDITED = "firstName.lastName@testmailEdited.com";
    final static String TEST_USER_ID = "UserID";
    final static String TEST_ADMIN_ID = "AdminID";
    final static String TEST_EVENT_CREATOR_ID = "EventCreatorID";
    final static String TEST_PUBLIC_EVENT_ID = "PublicEventID";
    final static String TEST_PRIVATE_EVENT_ID = "PrivateEventID";
    final static String TEST_EVENT_ID_TO_DELETE = "125";

    @BeforeAll
    static void globalSetup() {
        testUser = new User(TEST_USER_ID, "testLastName", "testFirstName", "2000-04-20",
                TEST_USER_EMAIL_ADDRESS, "password", "+497788866", false);
        testAdminUser = new User(TEST_ADMIN_ID, "firstAdmin", "Admin", "1999-04-05",
                TEST_ADMIN_EMAIL_ADDRESS, "AdminPassword", "+497733686", true);
        testEventCreaotr = new User(TEST_EVENT_CREATOR_ID, "Event", "Creator", "2000-04-20",
                TEST_EVENT_CREATOR_EMAIL_ADDRESS, "password", "+497789866", false);

        publicEvent = new PublicEvent(TEST_PUBLIC_EVENT_ID, "TestPublicEvent", "2025-01-01 12:30:00",
                "2025-01-02 12:30:00", 0, testArrayList, "TestCategory",
                false, "66115", "Saarbrücken", "TestStraße 6", "Turmschule",
                "This is a cool event", 20, 0);
        privateEvent = new PrivateEvent(TEST_PRIVATE_EVENT_ID, "TestPrivateEvent", "2025-01-01 12:30:00",
                "2025-01-02 12:30:00", 0, testArrayList, "TestCategory",
                true, "66115", "Saarbrücken", "TestStraße 6", "Turmschule",
                "This is a cool event");

        UserDatabaseConnector.createNewUser(testUser);
        UserDatabaseConnector.createNewUser(testAdminUser);
        UserDatabaseConnector.createNewUser(testEventCreaotr);
        EventDatabaseConnector.createNewEvent(publicEvent, TEST_EVENT_CREATOR_ID);
        EventDatabaseConnector.createNewEvent(privateEvent, TEST_EVENT_CREATOR_ID);
    }

    @AfterAll
    static void globalCleanup() {
        UserManagerImpl userManagerImpl = new UserManagerImpl();
        EventDatabaseConnector.deleteEventByID(TEST_PUBLIC_EVENT_ID, TEST_ADMIN_ID);
        EventDatabaseConnector.deleteEventByID(TEST_PRIVATE_EVENT_ID, TEST_ADMIN_ID);

        UserDatabaseConnector.deleteUserByEmail(TEST_ADMIN_EMAIL_ADDRESS);
        UserDatabaseConnector.deleteUserByEmail(TEST_USER_EMAIL_ADDRESS);
        UserDatabaseConnector.deleteUserByEmail(TEST_EVENT_CREATOR_EMAIL_ADDRESS);


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
    @DisplayName("EditUser Test Email")
    void editUserTest() {
        Optional<User> optionalUser = userManagerImpl.getUserByEmail(TEST_USER_EMAIL_ADDRESS);
        if (optionalUser.isEmpty()) {
            fail("User not found");
        }

        User userToEdit = optionalUser.get();
        String userIDFromUserToEdit = userToEdit.getUserID();

        String firstName = userToEdit.getFirstName();
        String lastName = userToEdit.getLastName();
        String dateOfBirth = userToEdit.getDateOfBirth();
        String password = userToEdit.getPassword();
        String phoneNumber = userToEdit.getPhoneNumber();


        userManagerImpl.editUser(userIDFromUserToEdit, firstName, lastName, dateOfBirth, TEST_USER_EMAIL_ADDRESS_EDITED, password, phoneNumber, TEST_ADMIN_ID);

        assertEquals(TEST_USER_EMAIL_ADDRESS_EDITED, userManagerImpl.getUserByID(userIDFromUserToEdit).get().getEMailAddress());

        userManagerImpl.editUser(userIDFromUserToEdit, firstName, lastName, dateOfBirth, TEST_USER_EMAIL_ADDRESS, password, phoneNumber, TEST_ADMIN_ID); //Setting email back to standard
    }

    @Test
    @DisplayName("EditUser Test - Missing Permission")
    void editUserTestFailedByMissingPermission() {
        Optional<User> optionalUser = userManagerImpl.getUserByEmail(TEST_USER_EMAIL_ADDRESS);
        if (optionalUser.isEmpty()) {
            assertFalse(false);
        } else {
            User userToEdit = optionalUser.get();
            String userIDFromUserToEdit = userToEdit.getUserID();

            String firstName = userToEdit.getFirstName();
            String lastName = userToEdit.getLastName();
            String dateOfBirth = userToEdit.getDateOfBirth();
            String password = userToEdit.getPassword();
            String phoneNumber = userToEdit.getPhoneNumber();


            userManagerImpl.editUser(userIDFromUserToEdit, firstName, lastName, dateOfBirth, TEST_USER_EMAIL_ADDRESS_EDITED, password, phoneNumber, TEST_USER_ID);

            assertNotEquals(TEST_USER_EMAIL_ADDRESS_EDITED, userManagerImpl.getUserByID(userIDFromUserToEdit).get().getEMailAddress());
        }
    }

    @Test
    @DisplayName("EditUser Test - Wrong Email")
    void editUserTestFailedByWrongUserID() {
        Optional<User> optionalUser = userManagerImpl.getUserByEmail("wrong@email.com");
        if (optionalUser.isEmpty()) {
            assertFalse(false);
        } else {
            User userToEdit = optionalUser.get();
            String userIDFromUserToEdit = userToEdit.getUserID();

            String firstName = userToEdit.getFirstName();
            String lastName = userToEdit.getLastName();
            String dateOfBirth = userToEdit.getDateOfBirth();
            String password = userToEdit.getPassword();
            String phoneNumber = userToEdit.getPhoneNumber();

            userManagerImpl.editUser(userIDFromUserToEdit, firstName, lastName, dateOfBirth, TEST_USER_EMAIL_ADDRESS_EDITED, password, phoneNumber, TEST_USER_ID);

            assertNotEquals(TEST_USER_EMAIL_ADDRESS_EDITED, userManagerImpl.getUserByID(userIDFromUserToEdit).get().getEMailAddress());
        }
    }

    @Test
    @DisplayName("DeleteUser Test")
    void deleteUserTest() {
        assertTrue(userManagerImpl.deleteUser(CREATE_TEST_USER_EMAIL_ADDRESS, TEST_ADMIN_ID));
    }
    //#endregion CRUD-Operation-Tests for User

    //#region Crud-Operation-Tests for Event#
    //Todo: wieder aktivieren sobald readPublicEventsByName funktioniert
    @Test
    @DisplayName("Create Public-Event Test")
    @Disabled
    void createPublicEventTest() {
        assertTrue(userManagerImpl.createNewEvent("TestPublicEventIntern", "2000-01-01",
                "2000-01-02", "TestCategory", "66115",
                "TestStraße 6", "Turmschule", "This is a cool event", 20,
                0,false, TEST_USER_ID));

        String puplicEventID = EventDatabaseConnector.readPublicEventsByName("TestPublicEventIntern").get(0).getEventID();
        //userManagerImpl.deleteEvent(EventDatabaseConnector.readPublicEventsByName("TestPublicEventIntern").get(0).getEventID(), TEST_ADMIN_ID);
        EventDatabaseConnector.deleteEventByID(puplicEventID, TEST_ADMIN_ID);
    }

    @Test
    @DisplayName("Create Private Event Test")
    @Disabled
    void createPrivateEventTest() {

        userManagerImpl.createNewEvent("localPrivateEvent", "2025-03-02 15:00:00",
            "2025-03-02 15:00", "TestCategory",
            "66119", "Teststraße 6", "Test Cafee",
            "Description", 20, -1,true, TEST_ADMIN_ID);

        String eventId = getEventIdByName("localPrivateEvent", TEST_ADMIN_ID);

        assertTrue(EventDatabaseConnector.readEventByID(eventId).isPresent());
        EventDatabaseConnector.deleteEventByID(eventId, TEST_ADMIN_ID);
        assertFalse(EventDatabaseConnector.readEventByID(eventId).isPresent());
    }

    private String getEventIdByName(String eventName, String creatorId) {
        var usersEvents = EventDatabaseConnector.getEventsByCreatorID(creatorId);
        return usersEvents.stream()
                .filter(event -> event.getEventName().equals(eventName))
                .findFirst()
                .map(EventModel::getEventID)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
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
    void deleteEventTest() {
        eventToDelete = new PrivateEvent(TEST_EVENT_ID_TO_DELETE, "EventToDelete", "2025-01-01 12:30:00",
                "2025-01-02 12:30:00", 0, testArrayList, "TestCategory",
                true, "66115", "Saarbrücken", "TestStraße 6", "Turmschule",
                "This is a cool event");

        EventDatabaseConnector.createNewEvent(eventToDelete, TEST_ADMIN_ID);

        assertTrue(userManagerImpl.deleteEvent(TEST_EVENT_ID_TO_DELETE, TEST_ADMIN_ID));
    }
    //#endregion Crud-Operation-Tests for Event

    //#region Event-Operations
    @Test
    @DisplayName("Book, Export and Cancel Event Test")
    void exportAndExportAndCancelEventTest() {
        bookEventTests();
        exportTests();
        cancelEventTest();
    }

    void bookEventTests() {
        if (userManagerImpl.getUserByID(TEST_USER_ID).isPresent()) {
            System.out.println("Testuser existiert in DB");
        }else {
            System.out.println("Testuser existiert nicht in DB");
        }

        tryToBookAnPrivateEventTest();
        bookEventAsUserTest();
        bookEventAsEventCreatorTest();

        System.out.println(userManagerImpl.showEventParticipantList(TEST_PUBLIC_EVENT_ID));
    }

    //#region Booking-Cases
    void bookEventAsEventCreatorTest() {
        assertTrue(userManagerImpl.bookEvent(TEST_PUBLIC_EVENT_ID, TEST_ADMIN_ID));
    }

    void bookEventAsUserTest() {
        assertTrue(userManagerImpl.bookEvent(TEST_PUBLIC_EVENT_ID, TEST_USER_ID));
    }

    void tryToBookAnPrivateEventTest() {
        assertFalse(userManagerImpl.bookEvent(TEST_PRIVATE_EVENT_ID, TEST_USER_ID));
    }
    //#endregion Booking-Cases

    void exportTests() {
        successfulExportTest();
        wrongUserIDExportTest();
    }

    //#region Export-Test-Cases
    void successfulExportTest() {
        assertTrue(userManagerImpl.exportAllBookedEvents(TEST_USER_ID));
    }

    void wrongUserIDExportTest() {
        //assertFalse(userManagerImpl.exportAllBookedEvents("998"));
    }
    //#endregion Export-Test-Cases

    void cancelEventTest() {
        String notExistingEventID = "1234";

        assertFalse(userManagerImpl.cancelEvent(notExistingEventID, TEST_USER_ID));
        assertTrue(userManagerImpl.cancelEvent(TEST_PUBLIC_EVENT_ID, TEST_ADMIN_ID));
        assertTrue(userManagerImpl.cancelEvent(TEST_PUBLIC_EVENT_ID, TEST_USER_ID));
    }

    @Test
    @DisplayName("Add & Remove User to Event Test")
    @Disabled
    void addAndRemoveUserToEventTest() {
        addUserToEventAsUser();
        addUserToEventAsAdmin();
        removeUserFromEventAsUser();
        removeUserFromEventAsAdmin();
        addUserToEventAsEventCreator();
        removeUserFromEventAsEventCreator();

    }

    void addUserToEventAsUser() {
        assertFalse(userManagerImpl.addUserToEvent(TEST_PRIVATE_EVENT_ID,TEST_USER_EMAIL_ADDRESS,TEST_USER_ID));
    }

    void addUserToEventAsAdmin() {
        if (EventDatabaseConnector.readPrivateEventByID(TEST_PRIVATE_EVENT_ID).isPresent()) {
            System.out.println("Private Event exists in DB");
        }
        if (UserDatabaseConnector.readUserByEMail(TEST_USER_EMAIL_ADDRESS).isPresent()) {
            System.out.println("User exists in DB");
        }
        if (UserDatabaseConnector.readUserByID(TEST_ADMIN_ID).isPresent()) {
            System.out.println("Admin-User exists in DB");
        }

        assertTrue(userManagerImpl.addUserToEvent(TEST_PRIVATE_EVENT_ID,TEST_USER_EMAIL_ADDRESS,TEST_ADMIN_ID));
    }

    void addUserToEventAsEventCreator() {
        assertTrue(userManagerImpl.addUserToEvent(TEST_PRIVATE_EVENT_ID,TEST_USER_EMAIL_ADDRESS,TEST_EVENT_CREATOR_ID));
    }

    void removeUserFromEventAsUser() {
        assertFalse(userManagerImpl.removeUserFromEvent(TEST_PRIVATE_EVENT_ID,TEST_USER_EMAIL_ADDRESS, TEST_USER_ID));
    }

    void removeUserFromEventAsAdmin() {
        assertTrue(userManagerImpl.removeUserFromEvent(TEST_PRIVATE_EVENT_ID,TEST_USER_EMAIL_ADDRESS, TEST_ADMIN_ID));
    }

    void removeUserFromEventAsEventCreator() {
        assertTrue(userManagerImpl.removeUserFromEvent(TEST_PRIVATE_EVENT_ID,TEST_USER_EMAIL_ADDRESS, TEST_EVENT_CREATOR_ID));
    }
    //#endregion Event-Operations

    //#region Permission Tests
    @Test
    @DisplayName("Add&Remove AdminStatus Test")
    void addAndRemoveAdminStatusToUserTest() {
        addAdminStatusToUser();
        removeAdminStatusFromUser();
    }

    void addAdminStatusToUser() {
        userManagerImpl.addAdminStatusToUserByUserID(TEST_USER_ID, testAdminUser);
        assertEquals(Role.ADMIN, userManagerImpl.getUserByID(testUser.getUserID()).get().getRole());
    }

    void removeAdminStatusFromUser() {
        userManagerImpl.removeAdminStatusFromUserByUserID(TEST_USER_ID, testAdminUser);
        assertNotEquals(Role.ADMIN, userManagerImpl.getUserByID(testUser.getUserID()).get().getRole());
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


}

