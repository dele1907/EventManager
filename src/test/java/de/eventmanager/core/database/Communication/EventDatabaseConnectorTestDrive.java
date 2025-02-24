package de.eventmanager.core.database.Communication;

import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;

import helper.LoggerHelper;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.*;

import static org.jooq.generated.tables.Cities.CITIES;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventDatabaseConnectorTestDrive {

    private PrivateEvent testPrivateEvent, testPrivateEventUpdated;
    private PublicEvent testPublicEvent, testPublicEventUpdated;

    private static final PrivateEvent STANDARD_PRIVATE_EVENT_FOR_READING = new PrivateEvent("standardPrivateReadingTestEventDatabaseConnector", "Standardname", "2099-09-09 12:00", "2099-10-10 12:00", 0, null,
            "Standardevent", true, "#####", "Standardstadt", "Standardstraße 99", "Standardhaus", "privates Standardevent");
    private static final PublicEvent STANDARD_PUBLIC_EVENT_FOR_READING_1 = new PublicEvent("standardPublicReadingTestEventDatabaseConnector1", "Standardname", "2099-09-09 12:00", "2099-10-10 12:00", 0, null,
            "Standardevent", false, "#####", "Standardstadt", "Standardstraße 1", "Standardplatz", "öffentliches Standardevent", 4711, 0);
    private static final PublicEvent STANDARD_PUBLIC_EVENT_FOR_READING_2 = new PublicEvent("standardPublicReadingTestEventDatabaseConnector2", "Standardname", "2099-09-09 12:00", "2099-10-10 12:00", 0, null,
            "Standardevent", false, "#####", "Standardstadt", "Standardstraße 1", "Standardplatz", "öffentliches Standardevent", 4711, 0);
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
        EventDatabaseConnector.createNewEvent(STANDARD_PRIVATE_EVENT_FOR_READING, TEST_CREATOR_FOR_READING);
        EventDatabaseConnector.createNewEvent(STANDARD_PUBLIC_EVENT_FOR_READING_1, TEST_CREATOR_FOR_READING);
        EventDatabaseConnector.createNewEvent(STANDARD_PUBLIC_EVENT_FOR_READING_2, TEST_CREATOR_FOR_READING);

        testDatabaseSetUp();
    }

    static void testDatabaseSetUp() {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            create.insertInto(CITIES)
                    .set(CITIES.POSTALCODE, "#####")
                    .set(CITIES.CITYNAME, "Standardstadt")
                    .execute();

        } catch (Exception e) {
            LoggerHelper.logErrorMessage(EventDatabaseConnectorTestDrive.class, e.getMessage());
        }
    }

    @AfterAll
    static void globalCleanUp() {
        EventDatabaseConnector.deleteEventByID(STANDARD_PRIVATE_EVENT_FOR_READING.getEventID(), TEST_USER_ID);
        EventDatabaseConnector.deleteEventByID(STANDARD_PUBLIC_EVENT_FOR_READING_1.getEventID(), TEST_USER_ID);
        EventDatabaseConnector.deleteEventByID(STANDARD_PUBLIC_EVENT_FOR_READING_2.getEventID(), TEST_USER_ID);

        testDatabaseCleanUp();
    }

    static void testDatabaseCleanUp() {
        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            create.deleteFrom(CITIES)
                    .where(CITIES.POSTALCODE.eq("#####"))
                    .execute();

        } catch (Exception e) {
            LoggerHelper.logErrorMessage(EventDatabaseConnectorTestDrive.class, e.getMessage());
        }
    }

    /**
     * Test creating and reading a private event
     * */
    @Test
    public void testCreateAndReadPrivateEvent() {

        testPrivateEvent = new PrivateEvent("createTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11 12:00", "2025-11-11 12:00", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");

        boolean privateEventCreated = EventDatabaseConnector.createNewEvent(testPrivateEvent, TEST_CREATOR_FOR_EVENTS);
        assertTrue(privateEventCreated, "Private event creation failed but should not.");

        Optional<PrivateEvent> privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID(testPrivateEvent.getEventID());
        assertTrue(privateEventFromDatabase.isPresent(), "Private event not found after creation.");
        assertEquals(testPrivateEvent.getEventID(), privateEventFromDatabase.get().getEventID());
        assertEquals(testPrivateEvent.getEventName(), privateEventFromDatabase.get().getEventName());
        assertEquals(testPrivateEvent.getEventStart(), privateEventFromDatabase.get().getEventStart());
        assertEquals(testPrivateEvent.getEventEnd(), privateEventFromDatabase.get().getEventEnd());
        assertEquals(testPrivateEvent.getNumberOfBookedUsersOnEvent(), privateEventFromDatabase.get().getNumberOfBookedUsersOnEvent());
        assertEquals(testPrivateEvent.getCategory(), privateEventFromDatabase.get().getCategory());
        assertEquals(testPrivateEvent.isPrivateEvent(), privateEventFromDatabase.get().isPrivateEvent());
        assertEquals(testPrivateEvent.getPostalCode(), privateEventFromDatabase.get().getPostalCode());
        assertEquals(testPrivateEvent.getAddress(), privateEventFromDatabase.get().getAddress());
        assertEquals(testPrivateEvent.getEventLocation(), privateEventFromDatabase.get().getEventLocation());
        assertEquals(testPrivateEvent.getDescription(), privateEventFromDatabase.get().getDescription());

        EventDatabaseConnector.deleteEventByID(testPrivateEvent.getEventID(), TEST_CREATOR_FOR_EVENTS);
    }

    /**
     * Test creating and reading a public event
     * */
    @Test
    public void testCreateAndReadPublicEvent() {

        testPublicEvent = new PublicEvent("createTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        boolean publicEventCreated = EventDatabaseConnector.createNewEvent(testPublicEvent, TEST_CREATOR_FOR_EVENTS);
        assertTrue(publicEventCreated, "Public event creation failed but should not.");

        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID(testPublicEvent.getEventID());
        assertTrue(publicEventFromDatabase.isPresent(), "Public event not found after creation.");
        assertEquals(testPublicEvent.getEventID(), publicEventFromDatabase.get().getEventID());
        assertEquals(testPublicEvent.getEventName(), publicEventFromDatabase.get().getEventName());
        assertEquals(testPublicEvent.getEventStart(), publicEventFromDatabase.get().getEventStart());
        assertEquals(testPublicEvent.getEventEnd(), publicEventFromDatabase.get().getEventEnd());
        assertEquals(testPublicEvent.getNumberOfBookedUsersOnEvent(), publicEventFromDatabase.get().getNumberOfBookedUsersOnEvent());
        assertEquals(testPublicEvent.getCategory(), publicEventFromDatabase.get().getCategory());
        assertEquals(testPublicEvent.isPrivateEvent(), publicEventFromDatabase.get().isPrivateEvent());
        assertEquals(testPublicEvent.getPostalCode(), publicEventFromDatabase.get().getPostalCode());
        assertEquals(testPublicEvent.getAddress(), publicEventFromDatabase.get().getAddress());
        assertEquals(testPublicEvent.getEventLocation(), publicEventFromDatabase.get().getEventLocation());
        assertEquals(testPublicEvent.getDescription(), publicEventFromDatabase.get().getDescription());
        assertEquals(testPublicEvent.getMaximumCapacity(), publicEventFromDatabase.get().getMaximumCapacity());

        EventDatabaseConnector.deleteEventByID(testPublicEvent.getEventID(), TEST_CREATOR_FOR_EVENTS);
    }

    /**
     * Test reading a private or public event with the same method
     * */
    @Test
    public void testReadPrivateOrPublicEventByID() {

        testPrivateEvent = new PrivateEvent("readByIDTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11 12:00", "2025-11-11 12:00", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");
        testPublicEvent = new PublicEvent("readByIDTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        EventDatabaseConnector.createNewEvent(testPrivateEvent, TEST_CREATOR_FOR_EVENTS);
        EventDatabaseConnector.createNewEvent(testPublicEvent, TEST_CREATOR_FOR_EVENTS);

        Optional<? extends EventModel> privateEventFromDatabase = EventDatabaseConnector.readEventByID(testPrivateEvent.getEventID());
        assertTrue(privateEventFromDatabase.isPresent(), "Public event not found after creation.");
        assertEquals(testPrivateEvent.getEventID(), privateEventFromDatabase.get().getEventID());
        assertEquals(testPrivateEvent.getEventName(), privateEventFromDatabase.get().getEventName());
        assertEquals(testPrivateEvent.isPrivateEvent(), privateEventFromDatabase.get().isPrivateEvent());

        Optional<? extends EventModel> publicEventFromDatabase = EventDatabaseConnector.readEventByID(testPublicEvent.getEventID());
        assertTrue(publicEventFromDatabase.isPresent(), "Public event not found after creation.");
        assertEquals(testPublicEvent.getEventID(), publicEventFromDatabase.get().getEventID());
        assertEquals(testPublicEvent.getEventName(), publicEventFromDatabase.get().getEventName());
        assertEquals(testPublicEvent.isPrivateEvent(), publicEventFromDatabase.get().isPrivateEvent());

        EventDatabaseConnector.deleteEventByID(testPrivateEvent.getEventID(), TEST_CREATOR_FOR_EVENTS);
        EventDatabaseConnector.deleteEventByID(testPublicEvent.getEventID(), TEST_CREATOR_FOR_EVENTS);
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

        assertEquals(3, events.size(), "Number of events retrieved by creator ID is incorrect.");
        assertTrue(events.stream().anyMatch(event -> event.getEventID().equals(STANDARD_PRIVATE_EVENT_FOR_READING.getEventID())), "Event not found.");
        assertTrue(events.stream().anyMatch(event -> event.getEventID().equals(STANDARD_PUBLIC_EVENT_FOR_READING_1.getEventID())), "Event not found.");
        assertTrue(events.stream().anyMatch(event -> event.getEventID().equals(STANDARD_PUBLIC_EVENT_FOR_READING_2.getEventID())), "Event not found.");
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

        EventDatabaseConnector.createNewEvent(testPrivateEvent, TEST_CREATOR_FOR_EVENTS);

        boolean privateEventUpdated = EventDatabaseConnector.updateEvent(testPrivateEventUpdated);
        assertTrue(privateEventUpdated, "Private event update failed but should not.");

        Optional<PrivateEvent> privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID(testPrivateEventUpdated.getEventID());
        assertTrue(privateEventFromDatabase.isPresent(), "Private event not found after update.");
        assertEquals(testPrivateEventUpdated.getEventID(), privateEventFromDatabase.get().getEventID());
        assertEquals(testPrivateEventUpdated.getEventName(), privateEventFromDatabase.get().getEventName());
        assertEquals(testPrivateEventUpdated.getEventStart(), privateEventFromDatabase.get().getEventStart());
        assertEquals(testPrivateEventUpdated.getEventEnd(), privateEventFromDatabase.get().getEventEnd());
        assertEquals(testPrivateEventUpdated.getNumberOfBookedUsersOnEvent(), privateEventFromDatabase.get().getNumberOfBookedUsersOnEvent());
        assertEquals(testPrivateEventUpdated.getCategory(), privateEventFromDatabase.get().getCategory());
        assertEquals(testPrivateEventUpdated.isPrivateEvent(), privateEventFromDatabase.get().isPrivateEvent());
        assertEquals(testPrivateEventUpdated.getPostalCode(), privateEventFromDatabase.get().getPostalCode());
        assertEquals(testPrivateEventUpdated.getAddress(), privateEventFromDatabase.get().getAddress());
        assertEquals(testPrivateEventUpdated.getEventLocation(), privateEventFromDatabase.get().getEventLocation());
        assertEquals(testPrivateEventUpdated.getDescription(), privateEventFromDatabase.get().getDescription());

        EventDatabaseConnector.deleteEventByID(testPrivateEvent.getEventID(), TEST_CREATOR_FOR_EVENTS);
    }

    /**
     * Test updating and reading a public event
     * */
    @Test
    public void testUpdateAndReadPublicEvent() {

        testPublicEvent = new PublicEvent("updateTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);
        testPublicEventUpdated = new PublicEvent("updateTestPublicEventDatabaseConnector", "Emmes", "2025-06-06 12:00", "2025-06-12 12:00", 0, null,
                "Stadtfest", false, "66740", "Saarlouis", "Großer Markt 1", "Innenstadt", "Essen, Getränke und Musik", 10000,0);

        EventDatabaseConnector.createNewEvent(testPublicEvent, TEST_CREATOR_FOR_EVENTS);

        boolean publicEventUpdated = EventDatabaseConnector.updateEvent(testPublicEventUpdated);
        assertTrue(publicEventUpdated, "Public event update failed but should not.");

        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID(testPublicEventUpdated.getEventID());
        assertTrue(publicEventFromDatabase.isPresent(), "Public event not found after update.");
        assertEquals(testPublicEventUpdated.getEventID(), publicEventFromDatabase.get().getEventID());
        assertEquals(testPublicEventUpdated.getEventName(), publicEventFromDatabase.get().getEventName());
        assertEquals(testPublicEventUpdated.getEventStart(), publicEventFromDatabase.get().getEventStart());
        assertEquals(testPublicEventUpdated.getEventEnd(), publicEventFromDatabase.get().getEventEnd());
        assertEquals(testPublicEventUpdated.getNumberOfBookedUsersOnEvent(), publicEventFromDatabase.get().getNumberOfBookedUsersOnEvent());
        assertEquals(testPublicEventUpdated.getCategory(), publicEventFromDatabase.get().getCategory());
        assertEquals(testPublicEventUpdated.isPrivateEvent(), publicEventFromDatabase.get().isPrivateEvent());
        assertEquals(testPublicEventUpdated.getPostalCode(), publicEventFromDatabase.get().getPostalCode());
        assertEquals(testPublicEventUpdated.getAddress(), publicEventFromDatabase.get().getAddress());
        assertEquals(testPublicEventUpdated.getEventLocation(), publicEventFromDatabase.get().getEventLocation());
        assertEquals(testPublicEventUpdated.getDescription(), publicEventFromDatabase.get().getDescription());
        assertEquals(testPublicEventUpdated.getMaximumCapacity(), publicEventFromDatabase.get().getMaximumCapacity());

        EventDatabaseConnector.deleteEventByID(testPublicEvent.getEventID(), TEST_CREATOR_FOR_EVENTS);
    }

    /**
     * Test deleting a private event by ID
     * */
    @Test
    public void testDeletePrivateEvent() {

        testPrivateEvent = new PrivateEvent("deleteTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11 12:00", "2025-11-11 12:00", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");

        EventDatabaseConnector.createNewEvent(testPrivateEvent, TEST_CREATOR_FOR_EVENTS);

        boolean privateEventDeleted = EventDatabaseConnector.deleteEventByID(testPrivateEvent.getEventID(), TEST_CREATOR_FOR_EVENTS);
        assertTrue(privateEventDeleted, "Private event deletion was successful but should not.");
        Optional<PrivateEvent> privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID(testPrivateEvent.getEventID());
        assertFalse(privateEventFromDatabase.isPresent(), "Private event was found but should not.");
    }

    /**
     * Test deleting a public event by ID
     * */
    @Test
    public void testDeletePublicEvent() {

        testPublicEvent = new PublicEvent("deleteTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        EventDatabaseConnector.createNewEvent(testPublicEvent, TEST_CREATOR_FOR_EVENTS);

        boolean publicEventDeleted = EventDatabaseConnector.deleteEventByID(testPublicEvent.getEventID(), TEST_CREATOR_FOR_EVENTS);
        assertTrue(publicEventDeleted, "Public event deletion was successful but should not.");
        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID(testPublicEvent.getEventID());
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

        EventDatabaseConnector.createNewEvent(testPrivateEvent, TEST_CREATOR_FOR_EVENTS);
        Optional<PrivateEvent> privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID(testPrivateEvent.getEventID());
        assertTrue(privateEventFromDatabase.isPresent(), "Private event not found after first creation.");

        boolean privateEventCreated = EventDatabaseConnector.createNewEvent(testPrivateEvent, TEST_CREATOR_FOR_EVENTS);
        assertFalse(privateEventCreated, "Second private event creation was successful but should not.");

        EventDatabaseConnector.deleteEventByID("createFailTestPrivateEventDatabaseConnector", TEST_CREATOR_FOR_EVENTS);
    }

    /**
     * Test that created public events are unique
     * */
    @Test
    public void testCreatePublicEventFailed() {

        testPublicEvent = new PublicEvent("createFailTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        EventDatabaseConnector.createNewEvent(testPublicEvent, TEST_CREATOR_FOR_EVENTS);
        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID(testPublicEvent.getEventID());
        assertTrue(publicEventFromDatabase.isPresent(), "Public event not found after first creation.");

        boolean publicEventCreated = EventDatabaseConnector.createNewEvent(testPublicEvent, TEST_CREATOR_FOR_EVENTS);
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

        testPrivateEvent = new PrivateEvent("updateFailTestPrivateEventDatabaseConnector", "Weihnachtsfeier", "2025-12-12 12:00", "2025-12-12 12:00", 0, null,
                "Firmenfeier", true, "66763", "Dillingen", "Werderstraße 4", "Lokschuppen", "Eine tolle Weihnachtsfeier von der tollen Firma");

        boolean privateEventUpdated = EventDatabaseConnector.updateEvent(testPrivateEvent);
        assertFalse(privateEventUpdated, "Private event update was successful but should not.");
    }

    /**
     * Test that updating a public event is only possible if there is an entry in the database
     * */
    @Test
    public void testUpdatePublicEventFailed() {

        testPublicEvent = new PublicEvent("updateFailTestPublicEventDatabaseConnector", "Emmes", "2025-06-06 12:00", "2025-06-12 12:00", 0, null,
                "Stadtfest", false, "66740", "Saarlouis", "Großer Markt 1", "Innenstadt", "Essen, Getränke und Musik", 10000, 0);

        boolean publicEventUpdated = EventDatabaseConnector.updateEvent(testPublicEvent);
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
