package de.eventmanager.core.database.communication;

import de.eventmanager.core.database.Communication.EventDataBaseConnector;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.users.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventDataBaseConnectorTestDrive {

    private PrivateEvent testPrivateEvent;
    private PrivateEvent testPrivateEventUpdated;
    private PublicEvent testPublicEvent;
    private PublicEvent testPublicEventUpdated;
    private boolean skipSetUp = false;
    private boolean skipCleanUp = false;

    /**
     * Create two private and two public events before each test
     * */
    @BeforeEach
    public void setUp() {

        if (skipSetUp) {

            return;
        }

        testPrivateEvent = new PrivateEvent("testPrivateEventID", "Geburtstag von Oma", "2025-11-11", "2025-11-11", 0, null,
                "private Feier", true, "66119", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier für meine super tolle TestOma ;)");
        testPrivateEventUpdated = new PrivateEvent("testPrivateEventID", "Weihnachtsfeier", "2025-12-12", "2025-11-11", 0, null,
                "Firmenfeier", true, "66119", "Gutenbergstraße 2", "Firmengebäude - Mensa", "Eine tolle Weihnachtsfeier von der tollen Firma!");

        testPublicEvent = new PublicEvent("testPublicEventID", "Ostermarkt", "2025-04-04", "2025-04-06", 0, null,
                "Markt", false, "66119", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000);
        testPublicEventUpdated = new PublicEvent("testPublicEventID", "Kirmes", "2025-06-06", "2025-06-12", 0, null,
                "Dorffest", false, "66119", "St. Johanner Markt", "Marktplatz", "Kirmes für tolle Menschen", 2000);
    }

    /**
     * Clean up the database after testing
     * */
    @AfterEach
    public void cleanUp() {

        if (skipCleanUp) {

            return;
        }

        EventDataBaseConnector.deleteEventByID("testPrivateEventID");
        EventDataBaseConnector.deleteEventByID("testPublicEventID");
    }

    /**
     * Test creating, updating and deleting events
     * */
    @Test
    @Order(0)
    public void testCreateUpdateDeleteEvent() {

        skipCleanUp = true;

        boolean privateEventCreated = EventDataBaseConnector.createNewEvent(testPrivateEvent);
        boolean publicEventCreated = EventDataBaseConnector.createNewEvent(testPublicEvent);

        assertTrue(privateEventCreated, "Event creation failed but should not.");
        assertTrue(publicEventCreated, "Event creation failed but should not.");

        boolean privateEventUpdated = EventDataBaseConnector.updateEvent(testPrivateEventUpdated);
        boolean publicEventUpdated = EventDataBaseConnector.updateEvent(testPublicEventUpdated);

        assertTrue(privateEventUpdated, "Event update failed but should not.");
        assertTrue(publicEventUpdated, "Event update failed but should not.");

        boolean privateEventDeleted = EventDataBaseConnector.deleteEventByID(testPrivateEvent.getEventID());
        boolean publicEventDeleted = EventDataBaseConnector.deleteEventByID(testPublicEvent.getEventID());

        assertTrue(privateEventDeleted, "Event deletion failed but should not.");
        assertTrue(publicEventDeleted, "Event deletion failed but should not.");
    }

    /**
     * Test that created events are unique
     * */
    @Test
    @Order(1)
    public void testCreateEventFailed() {

        EventDataBaseConnector.createNewEvent(testPrivateEvent);
        boolean privateEventCreated = EventDataBaseConnector.createNewEvent(testPrivateEvent);

        EventDataBaseConnector.createNewEvent(testPublicEvent);
        boolean publicEventCreated = EventDataBaseConnector.createNewEvent(testPublicEvent);

        assertFalse(privateEventCreated, "Event creation was successful but should not.");
        assertFalse(publicEventCreated, "Event creation was successful but should not.");
    }

    /**
     * Test that updating is only possible if there is an entry in the database
     * */
    @Test
    @Order(2)
    public void testUpdateEventFailed() {

        skipCleanUp = true;

        boolean privateEventUpdated = EventDataBaseConnector.updateEvent(testPrivateEventUpdated);
        boolean publicEventUpdated = EventDataBaseConnector.updateEvent(testPublicEventUpdated);

        assertFalse(privateEventUpdated, "Event update was successful but should not.");
        assertFalse(publicEventUpdated, "Event update was successful but should not.");
    }

    /**
     * Test that deleting is only possible if there is an entry in the database
     * */
    @Test
    @Order(3)
    public void testDeleteEventFailed() {

        skipSetUp = true;
        skipCleanUp = true;

        boolean eventDeleted = EventDataBaseConnector.deleteEventByID("invalidID");

        assertFalse(eventDeleted, "Event deletion was successful but should not.");
    }

    /**
     * Test reading an event from the database
     * */
    @Test
    @Order(4)
    public void testReadEventByID() {

        EventDataBaseConnector.createNewEvent(testPrivateEvent);
        EventDataBaseConnector.createNewEvent(testPublicEvent);

        PrivateEvent privateEventFromDatabase = EventDataBaseConnector.readPrivateEventByID("testPrivateEventID").get();
        PublicEvent publicEventFromDatabase = EventDataBaseConnector.readPublicEventByID("testPublicEventID").get();

        assertEquals("testPrivateEventID", privateEventFromDatabase.getEventID());
        assertEquals("Geburtstag von Oma", privateEventFromDatabase.getEventName());
        assertEquals("2025-11-11", privateEventFromDatabase.getEventStart());
        assertEquals("2025-11-11", privateEventFromDatabase.getEventEnd());
        assertEquals(0, privateEventFromDatabase.getNumberOfBookedUsersOnEvent());
        assertEquals("private Feier", privateEventFromDatabase.getCategory());
        assertEquals(true, privateEventFromDatabase.isPrivateEvent());
        assertEquals("66119", privateEventFromDatabase.getPostalCode());
        assertEquals("Gutenbergstraße 2", privateEventFromDatabase.getAddress());
        assertEquals("Omas Haus", privateEventFromDatabase.getEventLocation());
        assertEquals("Geburtstagsfeier für meine super tolle TestOma ;)", privateEventFromDatabase.getDescription());

        assertEquals("testPublicEventID", publicEventFromDatabase.getEventID());
        assertEquals("Ostermarkt", publicEventFromDatabase.getEventName());
        assertEquals("2025-04-04", publicEventFromDatabase.getEventStart());
        assertEquals("2025-04-06", publicEventFromDatabase.getEventEnd());
        assertEquals(0, publicEventFromDatabase.getNumberOfBookedUsersOnEvent());
        assertEquals("Markt", publicEventFromDatabase.getCategory());
        assertEquals(false, publicEventFromDatabase.isPrivateEvent());
        assertEquals("66119", publicEventFromDatabase.getPostalCode());
        assertEquals("St. Johanner Markt", publicEventFromDatabase.getAddress());
        assertEquals("Marktplatz", publicEventFromDatabase.getEventLocation());
        assertEquals("Ostermarkt für tolle Menschen", publicEventFromDatabase.getDescription());
        assertEquals(2000, publicEventFromDatabase.getMaximumCapacity());
    }

    /**
     * Test relation between event and creator (user)
     * */
    @Test
    @Order(5)
    public void testAddAndRemoveUserCreatedEvent() {

        skipSetUp = true;
        skipCleanUp = true;

        boolean testAddCreation = EventDataBaseConnector.addUserCreatedEvent("testEventID", "testCreatorID");

        assertTrue(testAddCreation, "Adding user to created event failed but should not.");

        boolean testDeleteCreation = EventDataBaseConnector.removeUserCreatedEvent("testEventID", "testCreatorID");

        assertTrue(testDeleteCreation, "Adding user to created event failed but should not.");
    }

    /**
     * Test relation on booking
     * */
    @Test
    @Order(6)
    public void testAddAndDeleteBooking() {

        EventDataBaseConnector.createNewEvent(testPrivateEvent);
        EventDataBaseConnector.createNewEvent(testPublicEvent);

        boolean testAddBooking = EventDataBaseConnector.addBooking("testPublicEventID", "testCreatorID");

        assertTrue(testAddBooking, "Adding user to booked event failed but should not.");

        boolean testDeleteBooking = EventDataBaseConnector.deleteBooking("testPublicEventID", "testCreatorID");

        assertTrue(testDeleteBooking, "Adding user to booked event failed but should not.");
    }

    /**
     * Test relation on booking
     * */
    @Test
    @Order(7)
    public void getBookedUsersOnEvent() {

        skipSetUp = true;
        skipCleanUp = true;

        User testUser1 = new User("testBookingUserID1", "Peter", "Bookman", "2000-02-02","peter.bookman@testmail.com","Password123", "0815", false);
        User testUser2 = new User("testBookingUserID2", "Herbert", "Bookson", "1980-08-08", "herbert.bookson@testmail.com","Password456", "4711", true);

        UserDatabaseConnector.createNewUser(testUser1);
        UserDatabaseConnector.createNewUser(testUser2);

        EventDataBaseConnector.createNewEvent(testPrivateEvent);
        EventDataBaseConnector.createNewEvent(testPublicEvent);

        EventDataBaseConnector.addBooking("testPublicEventID", "testBookingUserID1");
        EventDataBaseConnector.addBooking("testPublicEventID", "testBookingUserID2");

        ArrayList<String> bookedTestUsers = EventDataBaseConnector.getBookedUsersOnEvent("testPublicEventID");
        ArrayList<String> expectedBookedTestUsers = new ArrayList<>();
        expectedBookedTestUsers.add("peter.bookman@testmail.com");
        expectedBookedTestUsers.add("herbert.bookson@testmail.com");

        // test of user list
        assertEquals(expectedBookedTestUsers, bookedTestUsers);

        int numberOfBookedUsers = EventDataBaseConnector.readPublicEventByID("testPublicEventID").get().getNumberOfBookedUsersOnEvent();

        // test of user number after booking
        assertEquals(2, numberOfBookedUsers);

        EventDataBaseConnector.deleteBooking("testPublicEventID", "testBookingUserID1");
        EventDataBaseConnector.deleteBooking("testPublicEventID", "testBookingUserID2");

        int newNumberOfBookedUsers = EventDataBaseConnector.readPublicEventByID("testPublicEventID").get().getNumberOfBookedUsersOnEvent();

        // test of user number after delete booking
        assertEquals(0, newNumberOfBookedUsers);

        EventDataBaseConnector.deleteEventByID("testPrivateEventID");
        EventDataBaseConnector.deleteEventByID("testPublicEventID");

        UserDatabaseConnector.deleteUserByID("testBookingUserID1");
        UserDatabaseConnector.deleteUserByID("testBookingUserID2");
    }

}
