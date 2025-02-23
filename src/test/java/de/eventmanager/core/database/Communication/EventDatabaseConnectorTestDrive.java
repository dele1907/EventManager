package de.eventmanager.core.database.Communication;

import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventDatabaseConnectorTestDrive {

    private PrivateEvent testPrivateEvent1;
    private PrivateEvent testPrivateEvent2;
    private PublicEvent testPublicEvent1;
    private PublicEvent testPublicEvent2;
    private static final PublicEvent STANDARD_PUBLIC_EVENT_FOR_READING_1 = new PublicEvent("standardReadingTestPublicEventDatabaseConnector1", "Standardname", "2099-09-09 12:00", "2099-10-10 12:00", 0, null,
            "Standardevent", false, "#####", "Standardstadt", "Standardstraße 1", "Standardplatz", "Standard", 4711, 0);
    private static final PublicEvent STANDARD_PUBLIC_EVENT_FOR_READING_2 = new PublicEvent("standardReadingTestPublicEventDatabaseConnector2", "Standardname", "2099-09-09 12:00", "2099-10-10 12:00", 0, null,
            "Standardevent", false, "#####", "Standardstadt", "Standardstraße 1", "Standardplatz", "Standard", 4711, 0);
    private static final String TEST_CREATOR_FOR_EVENTS = "testCreatorIDForEventDatabaseConnector";
    private static final String TEST_CREATOR_FOR_READING = "readingTestCreatorIDForEventDatabaseConnector";
    private static final String TEST_USER_ID = "testUserIDForEventDatabaseConnector";
    private static final String INVALID_EVENT_ID = "invalidEventIDForEventDatabaseConnector";
    private static final String INVALID_CITY = "invalidCityForEventDatabaseConnector";
    private  static final String INVALID_LOCATION = "invalidLocationForEventDatabaseConnector";
    private static final String INVALID_EVENT_NAME = "invalidEventNameForEventDatabaseConnector";

    //#region successful CRUD operations

    @BeforeAll
    static void globalSetUp() {
        EventDatabaseConnector.createNewEvent(STANDARD_PUBLIC_EVENT_FOR_READING_1, TEST_CREATOR_FOR_READING);
        EventDatabaseConnector.createNewEvent(STANDARD_PUBLIC_EVENT_FOR_READING_2, TEST_CREATOR_FOR_READING);

        // TODO: In Testdatenbank "##### Standardstadt" erstellen
    }

    @AfterAll
    static void globalCleanUp() {
        EventDatabaseConnector.deleteEventByID(STANDARD_PUBLIC_EVENT_FOR_READING_1.getEventID(), TEST_USER_ID);
        EventDatabaseConnector.deleteEventByID(STANDARD_PUBLIC_EVENT_FOR_READING_2.getEventID(), TEST_USER_ID);

        // TODO: Aus Testdatenbank "##### Standardstadt" löschen
    }

    /**
     * Test creating and reading a private event
     * */
    @Test
    public void testCreateAndReadPrivateEvent() {

        testPrivateEvent1 = new PrivateEvent("createTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11 12:00", "2025-11-11 12:00", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");

        boolean privateEventCreated = EventDatabaseConnector.createNewEvent(testPrivateEvent1, TEST_CREATOR_FOR_EVENTS);
        assertTrue(privateEventCreated, "Private event creation failed but should not.");

        Optional<PrivateEvent> privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID(testPrivateEvent1.getEventID());
        assertTrue(privateEventFromDatabase.isPresent(), "Private event not found after creation.");
        assertEquals(testPrivateEvent1.getEventID(), privateEventFromDatabase.get().getEventID());
        assertEquals(testPrivateEvent1.getEventName(), privateEventFromDatabase.get().getEventName());
        assertEquals(testPrivateEvent1.getEventStart(), privateEventFromDatabase.get().getEventStart());
        assertEquals(testPrivateEvent1.getEventEnd(), privateEventFromDatabase.get().getEventEnd());
        assertEquals(testPrivateEvent1.getNumberOfBookedUsersOnEvent(), privateEventFromDatabase.get().getNumberOfBookedUsersOnEvent());
        assertEquals(testPrivateEvent1.getCategory(), privateEventFromDatabase.get().getCategory());
        assertEquals(testPrivateEvent1.isPrivateEvent(), privateEventFromDatabase.get().isPrivateEvent());
        assertEquals(testPrivateEvent1.getPostalCode(), privateEventFromDatabase.get().getPostalCode());
        assertEquals(testPrivateEvent1.getAddress(), privateEventFromDatabase.get().getAddress());
        assertEquals(testPrivateEvent1.getEventLocation(), privateEventFromDatabase.get().getEventLocation());
        assertEquals(testPrivateEvent1.getDescription(), privateEventFromDatabase.get().getDescription());

        EventDatabaseConnector.deleteEventByID(testPrivateEvent1.getEventID(), TEST_CREATOR_FOR_EVENTS);
    }

    /**
     * Test creating and reading a public event
     * */
    @Test
    public void testCreateAndReadPublicEvent() {

        testPublicEvent1 = new PublicEvent("createTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        boolean publicEventCreated = EventDatabaseConnector.createNewEvent(testPublicEvent1, TEST_CREATOR_FOR_EVENTS);
        assertTrue(publicEventCreated, "Public event creation failed but should not.");

        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID(testPublicEvent1.getEventID());
        assertTrue(publicEventFromDatabase.isPresent(), "Public event not found after creation.");
        assertEquals(testPublicEvent1.getEventID(), publicEventFromDatabase.get().getEventID());
        assertEquals(testPublicEvent1.getEventName(), publicEventFromDatabase.get().getEventName());
        assertEquals(testPublicEvent1.getEventStart(), publicEventFromDatabase.get().getEventStart());
        assertEquals(testPublicEvent1.getEventEnd(), publicEventFromDatabase.get().getEventEnd());
        assertEquals(testPublicEvent1.getNumberOfBookedUsersOnEvent(), publicEventFromDatabase.get().getNumberOfBookedUsersOnEvent());
        assertEquals(testPublicEvent1.getCategory(), publicEventFromDatabase.get().getCategory());
        assertEquals(testPublicEvent1.isPrivateEvent(), publicEventFromDatabase.get().isPrivateEvent());
        assertEquals(testPublicEvent1.getPostalCode(), publicEventFromDatabase.get().getPostalCode());
        assertEquals(testPublicEvent1.getAddress(), publicEventFromDatabase.get().getAddress());
        assertEquals(testPublicEvent1.getEventLocation(), publicEventFromDatabase.get().getEventLocation());
        assertEquals(testPublicEvent1.getDescription(), publicEventFromDatabase.get().getDescription());
        assertEquals(testPublicEvent1.getMaximumCapacity(), publicEventFromDatabase.get().getMaximumCapacity());

        EventDatabaseConnector.deleteEventByID(testPublicEvent1.getEventID(), TEST_CREATOR_FOR_EVENTS);
    }

    /**
     * Test reading a private or public event with the same method
     * */
    @Test
    public void testReadPrivateOrPublicEventByID() {

        testPrivateEvent1 = new PrivateEvent("readByIDTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11 12:00", "2025-11-11 12:00", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");
        testPublicEvent1 = new PublicEvent("readByIDTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        EventDatabaseConnector.createNewEvent(testPrivateEvent1, TEST_CREATOR_FOR_EVENTS);
        EventDatabaseConnector.createNewEvent(testPublicEvent1, TEST_CREATOR_FOR_EVENTS);

        Optional<? extends EventModel> privateEventFromDatabase = EventDatabaseConnector.readEventByID(testPrivateEvent1.getEventID());
        assertTrue(privateEventFromDatabase.isPresent(), "Public event not found after creation.");
        assertEquals(testPrivateEvent1.getEventID(), privateEventFromDatabase.get().getEventID());
        assertEquals(testPrivateEvent1.getEventName(), privateEventFromDatabase.get().getEventName());
        assertEquals(testPrivateEvent1.isPrivateEvent(), privateEventFromDatabase.get().isPrivateEvent());

        Optional<? extends EventModel> publicEventFromDatabase = EventDatabaseConnector.readEventByID(testPublicEvent1.getEventID());
        assertTrue(publicEventFromDatabase.isPresent(), "Public event not found after creation.");
        assertEquals(testPublicEvent1.getEventID(), publicEventFromDatabase.get().getEventID());
        assertEquals(testPublicEvent1.getEventName(), publicEventFromDatabase.get().getEventName());
        assertEquals(testPublicEvent1.isPrivateEvent(), publicEventFromDatabase.get().isPrivateEvent());

        EventDatabaseConnector.deleteEventByID(testPrivateEvent1.getEventID(), TEST_CREATOR_FOR_EVENTS);
        EventDatabaseConnector.deleteEventByID(testPublicEvent1.getEventID(), TEST_CREATOR_FOR_EVENTS);
    }

    /**
     * Test reading a list of public events by name
     * */
    @Test
    public void testReadPublicEventsByName() {
        
        ArrayList<PublicEvent> publicEventsFromDatabase = EventDatabaseConnector.readPublicEventsByName(STANDARD_PUBLIC_EVENT_FOR_READING_1.getEventName());
        assertEquals(2, publicEventsFromDatabase.size());

        ArrayList<PublicEvent> noPublicEventsFromDatabase = EventDatabaseConnector.readPublicEventsByName(INVALID_EVENT_NAME);
        assertEquals(0, noPublicEventsFromDatabase.size());
    }

    /**
     * Test reading a list of public events by location
     * */
    @Test
    public void testReadPublicEventsByLocation() {

        ArrayList<PublicEvent> publicEventsFromDatabase = EventDatabaseConnector.readPublicEventsByLocation(STANDARD_PUBLIC_EVENT_FOR_READING_1.getEventLocation());
        assertEquals(2, publicEventsFromDatabase.size());

        ArrayList<PublicEvent> noPublicEventsFromDatabase = EventDatabaseConnector.readPublicEventsByLocation(INVALID_LOCATION);
        assertEquals(0, noPublicEventsFromDatabase.size());
    }

    /**
     * Test reading a list of public events by city
     * */
    @Test
    @Disabled
    public void testReadEventsByCity() {

        ArrayList<PublicEvent> publicEventsFromDatabase = EventDatabaseConnector.readPublicEventByCity(STANDARD_PUBLIC_EVENT_FOR_READING_1.getCity());
        assertEquals(2, publicEventsFromDatabase.size());

        ArrayList<PublicEvent> noPublicEventsFromDatabase = EventDatabaseConnector.readPublicEventByCity(INVALID_CITY);
        assertEquals(0, noPublicEventsFromDatabase.size());
    }

    /**
     * Test reading events by creator ID
     */
    @Test
    public void testGetEventsByCreatorID() {

        List<EventModel> events = EventDatabaseConnector.getEventsByCreatorID(TEST_CREATOR_FOR_READING);

        assertEquals(2, events.size(), "Number of events retrieved by creator ID is incorrect.");
        assertTrue(events.stream().anyMatch(event -> event.getEventID().equals(STANDARD_PUBLIC_EVENT_FOR_READING_1.getEventID())), "Event not found.");
        assertTrue(events.stream().anyMatch(event -> event.getEventID().equals(STANDARD_PUBLIC_EVENT_FOR_READING_2.getEventID())), "Event not found.");
    }

    /**
     * Test updating and reading a private event
     * */
    @Test
    public void testUpdateAndReadPrivateEvent() {

        testPrivateEvent1 = new PrivateEvent("updateTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11 12:00", "2025-11-11 12:00", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");
        testPrivateEvent2 = new PrivateEvent("updateTestPrivateEventDatabaseConnector", "Weihnachtsfeier", "2025-12-12 12:00", "2025-12-12 12:00", 0, null,
                "Firmenfeier", true, "66763", "Dillingen", "Werderstraße 4", "Lokschuppen", "Eine tolle Weihnachtsfeier von der tollen Firma");

        EventDatabaseConnector.createNewEvent(testPrivateEvent1, TEST_CREATOR_FOR_EVENTS);

        boolean privateEventUpdated = EventDatabaseConnector.updateEvent(testPrivateEvent2);
        assertTrue(privateEventUpdated, "Private event update failed but should not.");

        Optional<PrivateEvent> privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID(testPrivateEvent2.getEventID());
        assertTrue(privateEventFromDatabase.isPresent(), "Private event not found after update.");
        assertEquals(testPrivateEvent2.getEventID(), privateEventFromDatabase.get().getEventID());
        assertEquals(testPrivateEvent2.getEventName(), privateEventFromDatabase.get().getEventName());
        assertEquals(testPrivateEvent2.getEventStart(), privateEventFromDatabase.get().getEventStart());
        assertEquals(testPrivateEvent2.getEventEnd(), privateEventFromDatabase.get().getEventEnd());
        assertEquals(testPrivateEvent2.getNumberOfBookedUsersOnEvent(), privateEventFromDatabase.get().getNumberOfBookedUsersOnEvent());
        assertEquals(testPrivateEvent2.getCategory(), privateEventFromDatabase.get().getCategory());
        assertEquals(testPrivateEvent2.isPrivateEvent(), privateEventFromDatabase.get().isPrivateEvent());
        assertEquals(testPrivateEvent2.getPostalCode(), privateEventFromDatabase.get().getPostalCode());
        assertEquals(testPrivateEvent2.getAddress(), privateEventFromDatabase.get().getAddress());
        assertEquals(testPrivateEvent2.getEventLocation(), privateEventFromDatabase.get().getEventLocation());
        assertEquals(testPrivateEvent2.getDescription(), privateEventFromDatabase.get().getDescription());

        EventDatabaseConnector.deleteEventByID(testPrivateEvent1.getEventID(), TEST_CREATOR_FOR_EVENTS);
    }

    /**
     * Test updating and reading a public event
     * */
    @Test
    public void testUpdateAndReadPublicEvent() {

        testPublicEvent1 = new PublicEvent("updateTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);
        testPublicEvent2 = new PublicEvent("updateTestPublicEventDatabaseConnector", "Emmes", "2025-06-06 12:00", "2025-06-12 12:00", 0, null,
                "Stadtfest", false, "66740", "Saarlouis", "Großer Markt 1", "Innenstadt", "Essen, Getränke und Musik", 10000,0);

        EventDatabaseConnector.createNewEvent(testPublicEvent1, TEST_CREATOR_FOR_EVENTS);

        boolean publicEventUpdated = EventDatabaseConnector.updateEvent(testPublicEvent2);
        assertTrue(publicEventUpdated, "Public event update failed but should not.");

        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID(testPublicEvent2.getEventID());
        assertTrue(publicEventFromDatabase.isPresent(), "Public event not found after update.");
        assertEquals(testPublicEvent2.getEventID(), publicEventFromDatabase.get().getEventID());
        assertEquals(testPublicEvent2.getEventName(), publicEventFromDatabase.get().getEventName());
        assertEquals(testPublicEvent2.getEventStart(), publicEventFromDatabase.get().getEventStart());
        assertEquals(testPublicEvent2.getEventEnd(), publicEventFromDatabase.get().getEventEnd());
        assertEquals(testPublicEvent2.getNumberOfBookedUsersOnEvent(), publicEventFromDatabase.get().getNumberOfBookedUsersOnEvent());
        assertEquals(testPublicEvent2.getCategory(), publicEventFromDatabase.get().getCategory());
        assertEquals(testPublicEvent2.isPrivateEvent(), publicEventFromDatabase.get().isPrivateEvent());
        assertEquals(testPublicEvent2.getPostalCode(), publicEventFromDatabase.get().getPostalCode());
        assertEquals(testPublicEvent2.getAddress(), publicEventFromDatabase.get().getAddress());
        assertEquals(testPublicEvent2.getEventLocation(), publicEventFromDatabase.get().getEventLocation());
        assertEquals(testPublicEvent2.getDescription(), publicEventFromDatabase.get().getDescription());
        assertEquals(testPublicEvent2.getMaximumCapacity(), publicEventFromDatabase.get().getMaximumCapacity());

        EventDatabaseConnector.deleteEventByID("updateTestPublicEventDatabaseConnector", TEST_CREATOR_FOR_EVENTS);
    }

    /**
     * Test deleting a private event by ID
     * */
    @Test
    public void testDeletePrivateEvent() {

        testPrivateEvent1 = new PrivateEvent("deleteTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11 12:00", "2025-11-11 12:00", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");

        EventDatabaseConnector.createNewEvent(testPrivateEvent1, TEST_CREATOR_FOR_EVENTS);

        boolean privateEventDeleted = EventDatabaseConnector.deleteEventByID(testPrivateEvent1.getEventID(), TEST_CREATOR_FOR_EVENTS);
        assertTrue(privateEventDeleted, "Private event deletion was successful but should not.");
        Optional<PrivateEvent> privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID(testPrivateEvent1.getEventID());
        assertFalse(privateEventFromDatabase.isPresent(), "Private event was found but should not.");
    }

    /**
     * Test deleting a public event by ID
     * */
    @Test
    public void testDeletePublicEvent() {

        testPublicEvent1 = new PublicEvent("deleteTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        EventDatabaseConnector.createNewEvent(testPublicEvent1, TEST_CREATOR_FOR_EVENTS);

        boolean publicEventDeleted = EventDatabaseConnector.deleteEventByID(testPublicEvent1.getEventID(), TEST_CREATOR_FOR_EVENTS);
        assertTrue(publicEventDeleted, "Public event deletion was successful but should not.");
        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID(testPublicEvent1.getEventID());
        assertFalse(publicEventFromDatabase.isPresent(), "Public event was found but should not.");
    }

    //#endregion successful CRUD operations

    //#region failed CRUD operations

    /**
     * Test that created private events are unique
     * */
    @Test
    public void testCreatePrivateEventFailed() {

        testPrivateEvent1 = new PrivateEvent("createFailTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11 12:00", "2025-11-11 12:00", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");

        EventDatabaseConnector.createNewEvent(testPrivateEvent1, TEST_CREATOR_FOR_EVENTS);
        Optional<PrivateEvent> privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID(testPrivateEvent1.getEventID());
        assertTrue(privateEventFromDatabase.isPresent(), "Private event not found after first creation.");

        boolean privateEventCreated = EventDatabaseConnector.createNewEvent(testPrivateEvent1, TEST_CREATOR_FOR_EVENTS);
        assertFalse(privateEventCreated, "Second private event creation was successful but should not.");

        EventDatabaseConnector.deleteEventByID("createFailTestPrivateEventDatabaseConnector", TEST_CREATOR_FOR_EVENTS);
    }

    /**
     * Test that created public events are unique
     * */
    @Test
    public void testCreatePublicEventFailed() {

        testPublicEvent1 = new PublicEvent("createFailTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        EventDatabaseConnector.createNewEvent(testPublicEvent1, TEST_CREATOR_FOR_EVENTS);
        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID(testPublicEvent1.getEventID());
        assertTrue(publicEventFromDatabase.isPresent(), "Public event not found after first creation.");

        boolean publicEventCreated = EventDatabaseConnector.createNewEvent(testPublicEvent1, TEST_CREATOR_FOR_EVENTS);
        assertFalse(publicEventCreated, "Second public event creation was successful but should not.");

        EventDatabaseConnector.deleteEventByID("createFailTestPublicEventDatabaseConnector", TEST_CREATOR_FOR_EVENTS);
    }

    /**
     * Test reading a private Event with invalid ID
     * */
    @Test
    public void testReadPrivateEventByIDFailed() {

        Optional<PrivateEvent> privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID(INVALID_EVENT_ID);
        assertFalse(privateEventFromDatabase.isPresent(), "Private event was found by ID address but should not.");
    }

    /**
     * Test reading a public Event with invalid ID
     * */
    @Test
    public void testReadPublicEventByIDFailed() {

        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID(INVALID_EVENT_ID);
        assertFalse(publicEventFromDatabase.isPresent(), "Public event was found by ID address but should not.");
    }

    /**
     * Test reading a private or public Event with invalid ID
     * */
    @Test
    public void testReadPrivateOrPublicEventByIDFailed() {

        Optional<? extends EventModel> eventFromDatabase = EventDatabaseConnector.readEventByID(INVALID_EVENT_ID);
        assertFalse(eventFromDatabase.isPresent(), "Event was found by ID address but should not.");
    }

    /**
     * Test that updating a private event is only possible if there is an entry in the database
     * */
    @Test
    public void testUpdatePrivateEventFailed() {

        testPrivateEvent1 = new PrivateEvent("updateFailTestPrivateEventDatabaseConnector", "Weihnachtsfeier", "2025-12-12 12:00", "2025-12-12 12:00", 0, null,
                "Firmenfeier", true, "66763", "Dillingen", "Werderstraße 4", "Lokschuppen", "Eine tolle Weihnachtsfeier von der tollen Firma");

        boolean privateEventUpdated = EventDatabaseConnector.updateEvent(testPrivateEvent1);
        assertFalse(privateEventUpdated, "Private event update was successful but should not.");
    }

    /**
     * Test that updating a public event is only possible if there is an entry in the database
     * */
    @Test
    public void testUpdatePublicEventFailed() {

        testPublicEvent1 = new PublicEvent("updateFailTestPublicEventDatabaseConnector", "Emmes", "2025-06-06 12:00", "2025-06-12 12:00", 0, null,
                "Stadtfest", false, "66740", "Saarlouis", "Großer Markt 1", "Innenstadt", "Essen, Getränke und Musik", 10000, 0);

        boolean publicEventUpdated = EventDatabaseConnector.updateEvent(testPublicEvent1);
        assertFalse(publicEventUpdated, "Public event update was successful but should not.");
    }

    /**
     * Test deleting a private event with invalid ID
     * */
    @Test
    public void testDeletePrivateEventFailed() {

        boolean privateEventDeleted = EventDatabaseConnector.deleteEventByID(INVALID_EVENT_ID, TEST_CREATOR_FOR_EVENTS);
        assertFalse(privateEventDeleted, "Private event deletion was successful but should not.");
    }

    /**
     * Test deleting a public event with invalid ID
     * */
    @Test
    public void testDeletePublicEventFailed() {

        boolean publicEventDeleted = EventDatabaseConnector.deleteEventByID(INVALID_EVENT_ID, TEST_CREATOR_FOR_EVENTS);
        assertFalse(publicEventDeleted, "Public event deletion was successful but should not.");
    }

    //#endregion failed CRUD operations

}
