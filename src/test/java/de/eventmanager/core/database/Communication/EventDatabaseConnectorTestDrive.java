package de.eventmanager.core.database.Communication;

import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.users.User;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.*;

import javax.swing.text.html.Option;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import static org.jooq.generated.tables.Events.EVENTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventDatabaseConnectorTestDrive {

    private PrivateEvent testPrivateEvent;
    private PrivateEvent testPrivateEventUpdated;
    private PublicEvent testPublicEvent;
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

        testPrivateEvent = new PrivateEvent("createTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11", "2025-11-11", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");

        boolean privateEventCreated = EventDatabaseConnector.createNewEvent(testPrivateEvent);
        assertTrue(privateEventCreated, "Private event creation failed but should not.");

        Optional<PrivateEvent> privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID("createTestPrivateEventDatabaseConnector");
        assertTrue(privateEventFromDatabase.isPresent(), "Private event not found after creation.");
        assertEquals("createTestPrivateEventDatabaseConnector", privateEventFromDatabase.get().getEventID());
        assertEquals("Geburtstag von Oma", privateEventFromDatabase.get().getEventName());
        assertEquals("2025-11-11", privateEventFromDatabase.get().getEventStart());
        assertEquals("2025-11-11", privateEventFromDatabase.get().getEventEnd());
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

        testPublicEvent = new PublicEvent("createTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04", "2025-04-06", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000);

        boolean publicEventCreated = EventDatabaseConnector.createNewEvent(testPublicEvent);
        assertTrue(publicEventCreated, "Public event creation failed but should not.");

        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID("createTestPublicEventDatabaseConnector");
        assertTrue(publicEventFromDatabase.isPresent(), "Public event not found after creation.");
        assertEquals("createTestPublicEventDatabaseConnector", publicEventFromDatabase.get().getEventID());
        assertEquals("Ostermarkt", publicEventFromDatabase.get().getEventName());
        assertEquals("2025-04-04", publicEventFromDatabase.get().getEventStart());
        assertEquals("2025-04-06", publicEventFromDatabase.get().getEventEnd());
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

        testPrivateEvent = new PrivateEvent("readTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11", "2025-11-11", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");
        testPublicEvent = new PublicEvent("readTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04", "2025-04-06", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000);

        EventDatabaseConnector.createNewEvent(testPrivateEvent);
        EventDatabaseConnector.createNewEvent(testPublicEvent);

        Optional<? extends EventModel> privateEventFromDatabase = EventDatabaseConnector.readEventByID("readTestPrivateEventDatabaseConnector");
        assertTrue(privateEventFromDatabase.isPresent(), "Public event not found after creation.");
        assertEquals("readTestPrivateEventDatabaseConnector", privateEventFromDatabase.get().getEventID());
        assertEquals("Geburtstag von Oma", privateEventFromDatabase.get().getEventName());
        assertEquals(true, privateEventFromDatabase.get().isPrivateEvent());

        Optional<? extends EventModel> publicEventFromDatabase = EventDatabaseConnector.readEventByID("readTestPublicEventDatabaseConnector");
        assertTrue(publicEventFromDatabase.isPresent(), "Public event not found after creation.");
        assertEquals("readTestPublicEventDatabaseConnector", publicEventFromDatabase.get().getEventID());
        assertEquals("Ostermarkt", publicEventFromDatabase.get().getEventName());
        assertEquals(false, publicEventFromDatabase.get().isPrivateEvent());

        EventDatabaseConnector.deleteEventByID("readTestPrivateEventDatabaseConnector");
        EventDatabaseConnector.deleteEventByID("readTestPublicEventDatabaseConnector");
    }

    // TODO: testReadPublicEventsByName, testReadPublicEventsByLocation, testReadPublicEventsByCity/PostalCode

    /**
     * Test updating and reading a private event
     * */
    @Test
    public void testUpdateAndReadPrivateEvent() {

        testPrivateEvent = new PrivateEvent("updateTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11", "2025-11-11", 0, null,
                "private Feier", true, "66119", "Saarbrücken", "Gutenbergstraße 2", "Omas Haus", "Geburtstagsfeier von meiner super tollen Test-Oma");
        testPrivateEventUpdated = new PrivateEvent("updateTestPrivateEventDatabaseConnector", "Weihnachtsfeier", "2025-12-12", "2025-12-12", 0, null,
                "Firmenfeier", true, "66763", "Dillingen", "Werderstraße 4", "Lokschuppen", "Eine tolle Weihnachtsfeier von der tollen Firma");

        EventDatabaseConnector.createNewEvent(testPrivateEvent);

        boolean privateEventUpdated = EventDatabaseConnector.updateEvent(testPrivateEventUpdated);
        assertTrue(privateEventUpdated, "Private event update failed but should not.");

        Optional<PrivateEvent> privateEventFromDatabase = EventDatabaseConnector.readPrivateEventByID("updateTestPrivateEventDatabaseConnector");
        assertTrue(privateEventFromDatabase.isPresent(), "Private event not found after update.");
        assertEquals("updateTestPrivateEventDatabaseConnector", privateEventFromDatabase.get().getEventID());
        assertEquals("Weihnachtsfeier", privateEventFromDatabase.get().getEventName());
        assertEquals("2025-12-12", privateEventFromDatabase.get().getEventStart());
        assertEquals("2025-12-12", privateEventFromDatabase.get().getEventEnd());
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

        testPublicEvent = new PublicEvent("updateTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04", "2025-04-06", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000);
        testPublicEventUpdated = new PublicEvent("updateTestPublicEventDatabaseConnector", "Emmes", "2025-06-06", "2025-06-12", 0, null,
                "Stadtfest", false, "66740", "Saarlouis", "Großer Markt 1", "Innenstadt", "Essen, Getränke und Musik", 10000);

        EventDatabaseConnector.createNewEvent(testPublicEvent);

        boolean publicEventUpdated = EventDatabaseConnector.updateEvent(testPublicEventUpdated);
        assertTrue(publicEventUpdated, "Public event update failed but should not.");

        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID("updateTestPublicEventDatabaseConnector");
        assertTrue(publicEventFromDatabase.isPresent(), "Public event not found after update.");
        assertEquals("updateTestPublicEventDatabaseConnector", publicEventFromDatabase.get().getEventID());
        assertEquals("Emmes", publicEventFromDatabase.get().getEventName());
        assertEquals("2025-06-06", publicEventFromDatabase.get().getEventStart());
        assertEquals("2025-06-12", publicEventFromDatabase.get().getEventEnd());
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

        testPrivateEvent = new PrivateEvent("deleteTestPrivateEventDatabaseConnector", "Geburtstag von Oma", "2025-11-11", "2025-11-11", 0, null,
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

        testPublicEvent = new PublicEvent("deleteTestPublicEventDatabaseConnector", "Ostermarkt", "2025-04-04", "2025-04-06", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000);

        EventDatabaseConnector.createNewEvent(testPublicEvent);

        boolean publicEventDeleted = EventDatabaseConnector.deleteEventByID("deleteTestPublicEventDatabaseConnector");
        assertTrue(publicEventDeleted, "Public event deletion was successful but should not.");
        Optional<PublicEvent> publicEventFromDatabase = EventDatabaseConnector.readPublicEventByID("deleteTestPublicEventDatabaseConnector");
        assertFalse(publicEventFromDatabase.isPresent(), "Public event was found but should not.");
    }

    //#endregion successful CRUD operations

    //#region failed CRUD operations

    // TODO: testCreatePrivateEventFailed, testCreatePublicEventFailed, testReadPrivateEventByIDFailed, testReadPublicEventByIDFailed, testReadPrivateOrPublicEventByIDFailed,
    //  testReadPublicEventsByNameFailed, testReadPublicEventsByLocationFailed, testReadPublicEventsByCity/PostalCodeFailed,
    //  testUpdatePrivateEventFailed, testUpdatePublicEventFailed, testDeletePrivateEventFailed, testDeletePublicEventFailed

    //#endregion failed CRUD operations

}
