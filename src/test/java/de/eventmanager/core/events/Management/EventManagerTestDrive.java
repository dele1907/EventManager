package de.eventmanager.core.events.Management;

import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventManagerTestDrive {

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

        EventManager.deleteEventByID("testPrivateEventID");
        EventManager.deleteEventByID("testPublicEventID");
    }

    /**
     * Test creating, updating and deleting events
     * */
    @Test
    @Order(0)
    public void testCreateUpdateDeleteEvent() {

        skipCleanUp = true;

        boolean privateEventCreated = EventManager.createNewEvent(testPrivateEvent);
        boolean publicEventCreated = EventManager.createNewEvent(testPublicEvent);

        assertTrue(privateEventCreated, "Event creation failed but should not.");
        assertTrue(publicEventCreated, "Event creation failed but should not.");

        boolean privateEventUpdated = EventManager.updateEvent(testPrivateEventUpdated);
        boolean publicEventUpdated = EventManager.updateEvent(testPublicEventUpdated);

        assertTrue(privateEventUpdated, "Event update failed but should not.");
        assertTrue(publicEventUpdated, "Event update failed but should not.");

        boolean privateEventDeleted = EventManager.deleteEventByID(testPrivateEvent.getEventID());
        boolean publicEventDeleted = EventManager.deleteEventByID(testPublicEvent.getEventID());

        assertTrue(privateEventDeleted, "Event deletion failed but should not.");
        assertTrue(publicEventDeleted, "Event deletion failed but should not.");
    }

    /**
     * Test that created events are unique
     * */
    @Test
    @Order(1)
    public void testCreateEventFailed() {

        EventManager.createNewEvent(testPrivateEvent);
        boolean privateEventCreated = EventManager.createNewEvent(testPrivateEvent);

        EventManager.createNewEvent(testPublicEvent);
        boolean publicEventCreated = EventManager.createNewEvent(testPublicEvent);

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

        boolean privateEventUpdated = EventManager.updateEvent(testPrivateEventUpdated);
        boolean publicEventUpdated = EventManager.updateEvent(testPublicEventUpdated);

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

        boolean eventDeleted = EventManager.deleteEventByID("invalidID");

        assertFalse(eventDeleted, "Event deletion was successful but should not.");
    }

    /**
     * Test reading an event from the database
     * */
    @Test
    @Order(4)
    public void testReadEventByID() {

        EventManager.createNewEvent(testPrivateEvent);
        EventManager.createNewEvent(testPublicEvent);

        PrivateEvent privateEventFromDatabase = EventManager.readPrivateEventByID("testPrivateEventID").get();
        PublicEvent publicEventFromDatabase = EventManager.readPublicEventByID("testPublicEventID").get();

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
    public void testAddAndDeleteUserCreatedEvent() {

        skipSetUp = true;
        skipCleanUp = true;

        boolean testAddCreation = EventManager.addUserCreatedEvent("testEventID", "testCreatorID");

        assertTrue(testAddCreation, "Adding user to created event failed but should not.");

        boolean testDeleteCreation = EventManager.deleteUserCreatedEvent("testEventID", "testCreatorID");

        assertTrue(testDeleteCreation, "Adding user to created event failed but should not.");
    }

}
