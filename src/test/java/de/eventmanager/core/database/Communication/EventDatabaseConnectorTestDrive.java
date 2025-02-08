package de.eventmanager.core.database.Communication;

import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.generated.tables.Events;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.jooq.generated.tables.Events.EVENTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventDatabaseConnectorTestDrive {

    private PrivateEvent testPrivateEvent;
    private PrivateEvent testPrivateEventUpdated;
    private PublicEvent testPublicEvent1;
    private PublicEvent testPublicEvent2;
    private PublicEvent testPublicEventUpdated;

    /**
     * Clean up the database after testing
     * */
    /*@AfterAll
    static void cleanUp() throws SQLException {

        try (Connection cleanupConnection = DatabaseConnector.connect()) {
            DSLContext cleanupDsl = DSL.using(cleanupConnection, SQLDialect.SQLITE);
            cleanupDsl.deleteFrom(EVENTS).execute();
        }
    }*/

    //#region successful CRUD operations

    /**
     * Test creating and reading a private event
     * */
    @Test
    public void testCreateAndReadPrivateEvent() {

        testPrivateEvent = new PrivateEvent("createTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11 12:00", "2025-11-11 12:00", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");

        boolean privateEventCreated = EventDatabaseConnector.createNewEvent(testPrivateEvent);
        assertTrue(privateEventCreated, "Private event creation failed but should not.");

        Optional<PrivateEvent> privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID("createTestPrivateEventDatabaseConnector");
        assertTrue(privateEventFromDatabase.isPresent(), "Private event not found after creation.");
        assertEquals("createTestPrivateEventDatabaseConnector", privateEventFromDatabase.get().getEventID());
        assertEquals("Geburtstag von Oma", privateEventFromDatabase.get().getEventName());
        assertEquals("2025-11-11 12:00", privateEventFromDatabase.get().getEventStart());
        assertEquals("2025-11-11 12:00", privateEventFromDatabase.get().getEventEnd());
        assertEquals(0, privateEventFromDatabase.get().getNumberOfBookedUsersOnEvent());
        assertEquals("private Feier", privateEventFromDatabase.get().getCategory());
        assertEquals(true, privateEventFromDatabase.get().isPrivateEvent());
        assertEquals("66119", privateEventFromDatabase.get().getPostalCode());
        assertEquals("Gutenbergstraße 2", privateEventFromDatabase.get().getAddress());
        assertEquals("Omas Haus", privateEventFromDatabase.get().getEventLocation());
        assertEquals("Geburtstagsfeier von meiner super tollen Test-Oma", privateEventFromDatabase.get().getDescription());

        EventDatabaseConnector.deleteEventByID("createTestPrivateEventDatabaseConnector");
    }

    /**
     * Test creating and reading a public event
     * */
    @Test
    public void testCreateAndReadPublicEvent() {

        testPublicEvent1 = new PublicEvent("createTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        boolean publicEventCreated = EventDatabaseConnector.createNewEvent(testPublicEvent1);
        assertTrue(publicEventCreated, "Public event creation failed but should not.");

        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID("createTestPublicEventDatabaseConnector");
        assertTrue(publicEventFromDatabase.isPresent(), "Public event not found after creation.");
        assertEquals("createTestPublicEventDatabaseConnector", publicEventFromDatabase.get().getEventID());
        assertEquals("Ostermarkt", publicEventFromDatabase.get().getEventName());
        assertEquals("2025-04-04 12:00", publicEventFromDatabase.get().getEventStart());
        assertEquals("2025-04-06 12:00", publicEventFromDatabase.get().getEventEnd());
        assertEquals(0, publicEventFromDatabase.get().getNumberOfBookedUsersOnEvent());
        assertEquals("Markt", publicEventFromDatabase.get().getCategory());
        assertEquals(false, publicEventFromDatabase.get().isPrivateEvent());
        assertEquals("66119", publicEventFromDatabase.get().getPostalCode());
        assertEquals("St. Johanner Markt", publicEventFromDatabase.get().getAddress());
        assertEquals("Marktplatz", publicEventFromDatabase.get().getEventLocation());
        assertEquals("Ostermarkt für tolle Menschen", publicEventFromDatabase.get().getDescription());
        assertEquals(2000, publicEventFromDatabase.get().getMaximumCapacity());

        EventDatabaseConnector.deleteEventByID("createTestPublicEventDatabaseConnector");
    }

    /**
     * Test reading a private or public event with the same method
     * */
    @Test
    public void testReadPrivateOrPublicEventByID() {

        testPrivateEvent = new PrivateEvent("readByIDTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11 12:00", "2025-11-11 12:00", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");
        testPublicEvent1 = new PublicEvent("readByIDTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        EventDatabaseConnector.createNewEvent(testPrivateEvent);
        EventDatabaseConnector.createNewEvent(testPublicEvent1);

        Optional<? extends EventModel> privateEventFromDatabase = EventDatabaseConnector.readEventByID("readByIDTestPrivateEventDatabaseConnector");
        assertTrue(privateEventFromDatabase.isPresent(), "Public event not found after creation.");
        assertEquals("readByIDTestPrivateEventDatabaseConnector", privateEventFromDatabase.get().getEventID());
        assertEquals("Geburtstag von Oma", privateEventFromDatabase.get().getEventName());
        assertEquals(true, privateEventFromDatabase.get().isPrivateEvent());

        Optional<? extends EventModel> publicEventFromDatabase = EventDatabaseConnector.readEventByID("readByIDTestPublicEventDatabaseConnector");
        assertTrue(publicEventFromDatabase.isPresent(), "Public event not found after creation.");
        assertEquals("readByIDTestPublicEventDatabaseConnector", publicEventFromDatabase.get().getEventID());
        assertEquals("Ostermarkt", publicEventFromDatabase.get().getEventName());
        assertEquals(false, publicEventFromDatabase.get().isPrivateEvent());

        EventDatabaseConnector.deleteEventByID("readByIDTestPrivateEventDatabaseConnector");
        EventDatabaseConnector.deleteEventByID("readByIDTestPublicEventDatabaseConnector");
    }

    /**
     * Test reading a list of public events by name
     * */
    @Test
    public void testReadPublicEventsByName() {

        testPublicEvent1 = new PublicEvent("readByNameTestPublicEventDatabaseConnector1", "ReadByName-Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);
        testPublicEvent2 = new PublicEvent("readByNameTestPublicEventDatabaseConnector2", "ReadByName-Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66740", "Saarlouis", "Großer Markt 1", "Marktplatz", "Ostermarkt für super Menschen", 3000, 0);

        EventDatabaseConnector.createNewEvent(testPublicEvent1);
        EventDatabaseConnector.createNewEvent(testPublicEvent2);

        ArrayList<PublicEvent> publicEventsFromDatabase = EventDatabaseConnector.readPublicEventsByName("ReadByName-Ostermarkt");
        assertEquals(2, publicEventsFromDatabase.size());

        ArrayList<PublicEvent> noPublicEventsFromDatabase = EventDatabaseConnector.readPublicEventsByName("ReadInvalidEventName");
        assertEquals(0, noPublicEventsFromDatabase.size());

        EventDatabaseConnector.deleteEventByID("readByNameTestPublicEventDatabaseConnector1");
        EventDatabaseConnector.deleteEventByID("readByNameTestPublicEventDatabaseConnector2");
    }

    /**
     * Test reading a list of public events by location
     * */
    @Test
    public void testReadPublicEventsByLocation() {

        testPublicEvent1 = new PublicEvent("readByLocationTestPublicEventDatabaseConnector1", "Emmes", "2025-06-06 12:00", "2025-06-12 12:00", 0, null,
                "Stadtfest", false, "66740", "Saarlouis", "Großer Markt 1", "ReadByLocation-Innenstadt", "Essen, Getränke und Musik", 10000, 0);
        testPublicEvent2 = new PublicEvent("readByLocationTestPublicEventDatabaseConnector2", "Altstadtfest", "2025-07-07 12:00", "2025-07-13 12:00", 0, null,
                "Stadtfest", false, "66740", "Saarlouis", "Großer Markt 1", "ReadByLocation-Innenstadt", "Traditionelles Stadtfest", 10000, 0);

        EventDatabaseConnector.createNewEvent(testPublicEvent1);
        EventDatabaseConnector.createNewEvent(testPublicEvent2);

        ArrayList<PublicEvent> publicEventsFromDatabase = EventDatabaseConnector.readPublicEventsByLocation("ReadByLocation-Innenstadt");
        assertEquals(2, publicEventsFromDatabase.size());

        ArrayList<PublicEvent> noPublicEventsFromDatabase = EventDatabaseConnector.readPublicEventsByLocation("ReadInvalidEventLocation");
        assertEquals(0, noPublicEventsFromDatabase.size());

        EventDatabaseConnector.deleteEventByID("readByLocationTestPublicEventDatabaseConnector1");
        EventDatabaseConnector.deleteEventByID("readByLocationTestPublicEventDatabaseConnector2");
    }

    /**
     * Test reading a list of public events by city
     * */
    @Test
    public void testReadEventsByCity() {

        testPublicEvent1 = new PublicEvent("readByCityTestPublicEventDatabaseConnector1", "Emmes", "2025-06-06 12:00", "2025-06-12 12:00", 0, null,
                "Stadtfest", false, "#####", "ReadByCity-Teststadt", "Großer Markt 1", "Innenstadt", "Essen, Getränke und Musik", 10000, 0);
        testPublicEvent2 = new PublicEvent("readByCityTestPublicEventDatabaseConnector2", "Altstadtfest", "2025-07-07 12:00", "2025-07-13 12:00", 0, null,
                "Stadtfest", false, "#####", "ReadByCity-Teststadt", "Großer Markt 1", "Innenstadt", "Traditionelles Stadtfest", 10000, 0);

        EventDatabaseConnector.createNewEvent(testPublicEvent1);
        EventDatabaseConnector.createNewEvent(testPublicEvent2);

        ArrayList<PublicEvent> publicEventsFromDatabase = EventDatabaseConnector.readPublicEventByCity("ReadByCity-Teststadt");
        assertEquals(2, publicEventsFromDatabase.size());

        ArrayList<PublicEvent> noPublicEventsFromDatabase = EventDatabaseConnector.readPublicEventByCity("ReadInvalidEventCity");
        assertEquals(0, noPublicEventsFromDatabase.size());

        EventDatabaseConnector.deleteEventByID("readByCityTestPublicEventDatabaseConnector1");
        EventDatabaseConnector.deleteEventByID("readByCityTestPublicEventDatabaseConnector2");
    }

    /**
     * Test reading events by creator ID
     */
    @Test
    public void testGetEventsByCreatorID() {

        String creatorID = "testCreatorToGetEventsFrom";

        // Create test events
        PrivateEvent testPrivateEvent = new PrivateEvent("getByCreatorTestPrivateEvent", "Private Event", "2025-11-11 12:00", "2025-11-11 12:00", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");
        PublicEvent testPublicEvent = new PublicEvent("getByCreatorTestPublicEvent", "Public Event", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        // Insert test events
        EventDatabaseConnector.createNewEvent(testPrivateEvent);
        EventDatabaseConnector.createNewEvent(testPublicEvent);

        // Link events to creator
        CreatorDatabaseConnector.assignUserAsEventCreator("getByCreatorTestPrivateEvent", creatorID);
        CreatorDatabaseConnector.assignUserAsEventCreator("getByCreatorTestPublicEvent", creatorID);

        // Retrieve events by creator ID
        List<EventModel> events = EventDatabaseConnector.getEventsByCreatorID(creatorID);

        // Verify the events
        assertEquals(2, events.size(), "Number of events retrieved by creator ID is incorrect.");
        assertTrue(events.stream().anyMatch(event -> event.getEventID().equals("getByCreatorTestPrivateEvent")), "Private event not found.");
        assertTrue(events.stream().anyMatch(event -> event.getEventID().equals("getByCreatorTestPublicEvent")), "Public event not found.");

        // Clean up
        EventDatabaseConnector.deleteEventByID("getByCreatorTestPrivateEvent");
        EventDatabaseConnector.deleteEventByID("getByCreatorTestPublicEvent");
        CreatorDatabaseConnector.removeUserAsEventCreator("getByCreatorTestPrivateEvent", "testCreatorToGetEventsFrom");
        CreatorDatabaseConnector.removeUserAsEventCreator("getByCreatorTestPublicEvent", "testCreatorToGetEventsFrom");
    }

    /**
     * Test updating and reading a private event
     * */
    @Test
    public void testUpdateAndReadPrivateEvent() {

        testPrivateEvent = new PrivateEvent("updateTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11 12:00", "2025-11-11 12:00", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");
        testPrivateEventUpdated = new PrivateEvent("updateTestPrivateEventDatabaseConnector", "Weihnachtsfeier", "2025-12-12 12:00", "2025-12-12 12:00", 0, null,
                "Firmenfeier", true, "66763", "Dillingen", "Werderstraße 4", "Lokschuppen", "Eine tolle Weihnachtsfeier von der tollen Firma");

        EventDatabaseConnector.createNewEvent(testPrivateEvent);

        boolean privateEventUpdated = EventDatabaseConnector.updateEvent(testPrivateEventUpdated);
        assertTrue(privateEventUpdated, "Private event update failed but should not.");

        Optional<PrivateEvent> privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID("updateTestPrivateEventDatabaseConnector");
        assertTrue(privateEventFromDatabase.isPresent(), "Private event not found after update.");
        assertEquals("updateTestPrivateEventDatabaseConnector", privateEventFromDatabase.get().getEventID());
        assertEquals("Weihnachtsfeier", privateEventFromDatabase.get().getEventName());
        assertEquals("2025-12-12 12:00", privateEventFromDatabase.get().getEventStart());
        assertEquals("2025-12-12 12:00", privateEventFromDatabase.get().getEventEnd());
        assertEquals(0, privateEventFromDatabase.get().getNumberOfBookedUsersOnEvent());
        assertEquals("Firmenfeier", privateEventFromDatabase.get().getCategory());
        assertEquals(true, privateEventFromDatabase.get().isPrivateEvent());
        assertEquals("66763", privateEventFromDatabase.get().getPostalCode());
        assertEquals("Werderstraße 4", privateEventFromDatabase.get().getAddress());
        assertEquals("Lokschuppen", privateEventFromDatabase.get().getEventLocation());
        assertEquals("Eine tolle Weihnachtsfeier von der tollen Firma", privateEventFromDatabase.get().getDescription());

        EventDatabaseConnector.deleteEventByID("updateTestPrivateEventDatabaseConnector");
    }

    /**
     * Test updating and reading a public event
     * */
    @Test
    public void testUpdateAndReadPublicEvent() {

        testPublicEvent1 = new PublicEvent("updateTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);
        testPublicEventUpdated = new PublicEvent("updateTestPublicEventDatabaseConnector", "Emmes", "2025-06-06 12:00", "2025-06-12 12:00", 0, null,
                "Stadtfest", false, "66740", "Saarlouis", "Großer Markt 1", "Innenstadt", "Essen, Getränke und Musik", 10000,0);

        EventDatabaseConnector.createNewEvent(testPublicEvent1);

        boolean publicEventUpdated = EventDatabaseConnector.updateEvent(testPublicEventUpdated);
        assertTrue(publicEventUpdated, "Public event update failed but should not.");

        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID("updateTestPublicEventDatabaseConnector");
        assertTrue(publicEventFromDatabase.isPresent(), "Public event not found after update.");
        assertEquals("updateTestPublicEventDatabaseConnector", publicEventFromDatabase.get().getEventID());
        assertEquals("Emmes", publicEventFromDatabase.get().getEventName());
        assertEquals("2025-06-06 12:00", publicEventFromDatabase.get().getEventStart());
        assertEquals("2025-06-12 12:00", publicEventFromDatabase.get().getEventEnd());
        assertEquals(0, publicEventFromDatabase.get().getNumberOfBookedUsersOnEvent());
        assertEquals("Stadtfest", publicEventFromDatabase.get().getCategory());
        assertEquals(false, publicEventFromDatabase.get().isPrivateEvent());
        assertEquals("66740", publicEventFromDatabase.get().getPostalCode());
        assertEquals("Großer Markt 1", publicEventFromDatabase.get().getAddress());
        assertEquals("Innenstadt", publicEventFromDatabase.get().getEventLocation());
        assertEquals("Essen, Getränke und Musik", publicEventFromDatabase.get().getDescription());
        assertEquals(10000, publicEventFromDatabase.get().getMaximumCapacity());

        EventDatabaseConnector.deleteEventByID("updateTestPublicEventDatabaseConnector");
    }

    /**
     * Test deleting a private event by ID
     * */
    @Test
    public void testDeletePrivateEvent() {

        testPrivateEvent = new PrivateEvent("deleteTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11 12:00", "2025-11-11 12:00", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");

        EventDatabaseConnector.createNewEvent(testPrivateEvent);

        boolean privateEventDeleted = EventDatabaseConnector.deleteEventByID("deleteTestPrivateEventDatabaseConnector");
        assertTrue(privateEventDeleted, "Private event deletion was successful but should not.");
        Optional<PrivateEvent> privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID("deleteTestPrivateEventDatabaseConnector");
        assertFalse(privateEventFromDatabase.isPresent(), "Private event was found but should not.");
    }

    /**
     * Test deleting a public event by ID
     * */
    @Test
    public void testDeletePublicEvent() {

        testPublicEvent1 = new PublicEvent("deleteTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        EventDatabaseConnector.createNewEvent(testPublicEvent1);

        boolean publicEventDeleted = EventDatabaseConnector.deleteEventByID("deleteTestPublicEventDatabaseConnector");
        assertTrue(publicEventDeleted, "Public event deletion was successful but should not.");
        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID("deleteTestPublicEventDatabaseConnector");
        assertFalse(publicEventFromDatabase.isPresent(), "Public event was found but should not.");
    }

    //#endregion successful CRUD operations

    //#region failed CRUD operations

    /**
     * Test that created private events are unique
     * */
    @Test
    public void testCreatePrivateEventFailed() {

        testPrivateEvent = new PrivateEvent("createFailTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11 12:00", "2025-11-11 12:00", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");

        EventDatabaseConnector.createNewEvent(testPrivateEvent);
        Optional<PrivateEvent> privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID("createFailTestPrivateEventDatabaseConnector");
        assertTrue(privateEventFromDatabase.isPresent(), "Private event not found after first creation.");

        boolean privateEventCreated = EventDatabaseConnector.createNewEvent(testPrivateEvent);
        assertFalse(privateEventCreated, "Second private event creation was successful but should not.");

        EventDatabaseConnector.deleteEventByID("createFailTestPrivateEventDatabaseConnector");
    }

    /**
     * Test that created public events are unique
     * */
    @Test
    public void testCreatePublicEventFailed() {

        testPublicEvent1 = new PublicEvent("createFailTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        EventDatabaseConnector.createNewEvent(testPublicEvent1);
        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID("createFailTestPublicEventDatabaseConnector");
        assertTrue(publicEventFromDatabase.isPresent(), "Public event not found after first creation.");

        boolean publicEventCreated = EventDatabaseConnector.createNewEvent(testPublicEvent1);
        assertFalse(publicEventCreated, "Second public event creation was successful but should not.");

        EventDatabaseConnector.deleteEventByID("createFailTestPublicEventDatabaseConnector");
    }

    /**
     * Test reading a private Event with invalid ID
     * */
    @Test
    public void testReadPrivateEventByIDFailed() {

        Optional<PrivateEvent> privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID("invalidPrivateEventIDToRead");
        assertFalse(privateEventFromDatabase.isPresent(), "Private event was found by ID address but should not.");
    }

    /**
     * Test reading a public Event with invalid ID
     * */
    @Test
    public void testReadPublicEventByIDFailed() {

        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID("invalidPublicEventIDToRead");
        assertFalse(publicEventFromDatabase.isPresent(), "Public event was found by ID address but should not.");
    }

    /**
     * Test reading a private or public Event with invalid ID
     * */
    @Test
    public void testReadPrivateOrPublicEventByIDFailed() {

        Optional<? extends EventModel> eventFromDatabase = EventDatabaseConnector.readEventByID("invalidEventIDToRead");
        assertFalse(eventFromDatabase.isPresent(), "Event was found by ID address but should not.");
    }

    /**
     * Test that updating a private event is only possible if there is an entry in the database
     * */
    @Test
    public void testUpdatePrivateEventFailed() {

        testPrivateEventUpdated = new PrivateEvent("updateFailTestPrivateEventDatabaseConnector", "Weihnachtsfeier", "2025-12-12 12:00", "2025-12-12 12:00", 0, null,
                "Firmenfeier", true, "66763", "Dillingen", "Werderstraße 4", "Lokschuppen", "Eine tolle Weihnachtsfeier von der tollen Firma");

        boolean privateEventUpdated = EventDatabaseConnector.updateEvent(testPrivateEventUpdated);
        assertFalse(privateEventUpdated, "Private event update was successful but should not.");
    }

    /**
     * Test that updating a public event is only possible if there is an entry in the database
     * */
    @Test
    public void testUpdatePublicEventFailed() {

        testPublicEventUpdated = new PublicEvent("updateFailTestPublicEventDatabaseConnector", "Emmes", "2025-06-06 12:00", "2025-06-12 12:00", 0, null,
                "Stadtfest", false, "66740", "Saarlouis", "Großer Markt 1", "Innenstadt", "Essen, Getränke und Musik", 10000, 0);

        boolean publicEventUpdated = EventDatabaseConnector.updateEvent(testPublicEventUpdated);
        assertFalse(publicEventUpdated, "Public event update was successful but should not.");
    }

    /**
     * Test deleting a private event with invalid ID
     * */
    @Test
    public void testDeletePrivateEventFailed() {

        boolean privateEventDeleted = EventDatabaseConnector.deleteEventByID("invalidPrivateEventIDToDelete");
        assertFalse(privateEventDeleted, "Private event deletion was successful but should not.");
    }

    /**
     * Test deleting a public event with invalid ID
     * */
    @Test
    public void testDeletePublicEventFailed() {

        boolean publicEventDeleted = EventDatabaseConnector.deleteEventByID("invalidPublicEventIDToDelete");
        assertFalse(publicEventDeleted, "Public event deletion was successful but should not.");
    }

    //#endregion failed CRUD operations

}
