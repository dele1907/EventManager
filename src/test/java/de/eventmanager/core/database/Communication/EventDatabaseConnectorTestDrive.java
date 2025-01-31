package de.eventmanager.core.database.Communication;

import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.users.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventDatabaseConnectorTestDrive {

    private PrivateEvent testPrivateEvent;
    private PrivateEvent testPrivateEventUpdated;
    private PublicEvent testPublicEvent;
    private PublicEvent testPublicEventUpdated;

    // TODO: rework tests

    /**
     * Create two private and two public events before each test
     * */
    /*@BeforeEach
    public void setUp() {

        testPrivateEvent = new PrivateEvent("testPrivateEventID", "Geburtstag von Oma", "2025-11-11", "2025-11-11", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier für meine super tolle TestOma ;)");
        testPrivateEventUpdated = new PrivateEvent("testPrivateEventID", "Weihnachtsfeier", "2025-12-12", "2025-11-11", 0, null,
                "Firmenfeier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Firmengebäude - Mensa", "Eine tolle Weihnachtsfeier von der tollen Firma!");

        testPublicEvent = new PublicEvent("testPublicEventID", "Ostermarkt", "2025-04-04", "2025-04-06", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000);
        testPublicEventUpdated = new PublicEvent("testPublicEventID", "Kirmes", "2025-06-06", "2025-06-12", 0, null,
                "Dorffest", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Kirmes für tolle Menschen", 2000);
    }*/

    /**
     * Clean up the database after testing
     * */
    /*@AfterEach
    public void cleanUp() {

        EventDatabaseConnector.deleteEventByID("testPrivateEventID");
        EventDatabaseConnector.deleteEventByID("testPublicEventID");
    }*/

    /**
     * Test creating, updating and deleting events
     * */
    /*@Test
    public void testCreateUpdateDeleteEvent() {

        boolean privateEventCreated = EventDatabaseConnector.createNewEvent(testPrivateEvent);
        boolean publicEventCreated = EventDatabaseConnector.createNewEvent(testPublicEvent);

        assertTrue(privateEventCreated, "Event creation failed but should not.");
        assertTrue(publicEventCreated, "Event creation failed but should not.");

        boolean privateEventUpdated = EventDatabaseConnector.updateEvent(testPrivateEventUpdated);
        boolean publicEventUpdated = EventDatabaseConnector.updateEvent(testPublicEventUpdated);

        assertTrue(privateEventUpdated, "Event update failed but should not.");
        assertTrue(publicEventUpdated, "Event update failed but should not.");

        boolean privateEventDeleted = EventDatabaseConnector.deleteEventByID(testPrivateEvent.getEventID());
        boolean publicEventDeleted = EventDatabaseConnector.deleteEventByID(testPublicEvent.getEventID());

        assertTrue(privateEventDeleted, "Event deletion failed but should not.");
        assertTrue(publicEventDeleted, "Event deletion failed but should not.");
    }*/

    /**
     * Test that created events are unique
     * */
    /*@Test
    public void testCreateEventFailed() {

        EventDatabaseConnector.createNewEvent(testPrivateEvent);
        boolean privateEventCreated = EventDatabaseConnector.createNewEvent(testPrivateEvent);

        EventDatabaseConnector.createNewEvent(testPublicEvent);
        boolean publicEventCreated = EventDatabaseConnector.createNewEvent(testPublicEvent);

        assertFalse(privateEventCreated, "Event creation was successful but should not.");
        assertFalse(publicEventCreated, "Event creation was successful but should not.");
    }*/

    /**
     * Test that updating is only possible if there is an entry in the database
     * */
    /*@Test
    public void testUpdateEventFailed() {

        boolean privateEventUpdated = EventDatabaseConnector.updateEvent(testPrivateEventUpdated);
        boolean publicEventUpdated = EventDatabaseConnector.updateEvent(testPublicEventUpdated);

        assertFalse(privateEventUpdated, "Event update was successful but should not.");
        assertFalse(publicEventUpdated, "Event update was successful but should not.");
    }*/

    /**
     * Test that deleting is only possible if there is an entry in the database
     * */
    /*@Test
    public void testDeleteEventFailed() {

        boolean eventDeleted = EventDatabaseConnector.deleteEventByID("invalidID");

        assertFalse(eventDeleted, "Event deletion was successful but should not.");
    }*/

    /**
     * Test reading an event from the database
     * */
    /*@Test
    public void testReadEventByID() {

        EventDatabaseConnector.createNewEvent(testPrivateEvent);
        EventDatabaseConnector.createNewEvent(testPublicEvent);

        PrivateEvent privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID("testPrivateEventID").get();
        PublicEvent publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID("testPublicEventID").get();

        assertEquals("testPrivateEventID", privateEventFromDatabase.getEventID());
        assertEquals("Geburtstag von Oma", privateEventFromDatabase.getEventName());
        assertEquals("2025-11-11", privateEventFromDatabase.getEventStart());
        assertEquals("2025-11-11", privateEventFromDatabase.getEventEnd());
        assertEquals(0, privateEventFromDatabase.getNumberOfBookedUsersOnEvent());
        assertEquals("private Feier", privateEventFromDatabase.getCategory());
        assertEquals(true, privateEventFromDatabase.isPrivateEvent());
        assertEquals("66119", privateEventFromDatabase.getPostalCode());
        assertEquals("Saarbrücken", privateEventFromDatabase.getCity());
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
        assertEquals("Saarbrücken", privateEventFromDatabase.getCity());
        assertEquals("St. Johanner Markt", publicEventFromDatabase.getAddress());
        assertEquals("Marktplatz", publicEventFromDatabase.getEventLocation());
        assertEquals("Ostermarkt für tolle Menschen", publicEventFromDatabase.getDescription());
        assertEquals(2000, publicEventFromDatabase.getMaximumCapacity());
    }*/

}
