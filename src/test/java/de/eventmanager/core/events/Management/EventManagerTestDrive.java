package de.eventmanager.core.events.Management;

import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import org.junit.jupiter.api.*;

import java.util.Optional;

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

    // Vor jedem Test je zwei private und öffentliche Test-Events erstellen:
    @BeforeEach
    public void setUp() {

        if (skipSetUp) {

            return;
        }

        testPrivateEvent = new PrivateEvent("testPrivateEventID", "Geburtstag von Oma", "2025-11-11",
                "2025-11-11", 0, "private Feier", true, "66119", "Gutenbergstraße 2",
                "Omas Haus", "Geburtstagsfeier für meine super tolle TestOma ;)");
        testPrivateEventUpdated = new PrivateEvent("testPrivateEventID", "Weihnachtsfeier", "2025-12-12", "2025-11-11", 0, "Firmenfeier", true, "66119", "Gutenbergstraße 2",
                "Firmengebäude - Mensa", "Eine tolle Weihnachtsfeier für die tolle Firma!");

        testPublicEvent = new PublicEvent("testPublicEventID", "Ostermarkt", "2025-04-04", "2025-04-06",
                "66119", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 0,
                "Markt", false, 2000);
        testPublicEventUpdated = new PublicEvent("testPublicEventID", "Kirmes", "2025-06-06", "2025-06-12",
                "66119", "St. Johanner Markt", "Marktplatz", "Kirmes für tolle Menschen", 0,
                "Dorffest", false, 5000);
    }


    // Nach jedem Test die Datenbank bereinigen:
    @AfterEach
    public void cleanUp() {

        if (skipCleanUp) {

            return;
        }

        EventManager.deleteEventByID("testPrivateEventID");
        EventManager.deleteEventByID("testPublicEventID");
    }

    // Testen von Erstellen, Updaten und Löschen eines Events in der Datenbank:
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

    // Testen, ob ein Event nicht mehrmals erstellt werden kann:
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

    // Testen, ob ein Update nur möglich ist, wenn der entsprechende Datensatz vorliegt:
    @Test
    @Order(2)
    public void testUpdateEventFailed() {

        skipCleanUp = true;

        boolean privateEventUpdated = EventManager.updateEvent(testPrivateEventUpdated);
        boolean publicEventUpdated = EventManager.updateEvent(testPublicEventUpdated);

        assertFalse(privateEventUpdated, "Event update was successful but should not.");
        assertFalse(publicEventUpdated, "Event update was successful but should not.");
    }

    // Testen, ob Löschen nur möglich ist, wenn der entsprechende Datensatz vorliegt:
    @Test
    @Order(3)
    public void testDeleteEventFailed() {

        skipSetUp = true;
        skipCleanUp = true;

        boolean eventDeleted = EventManager.deleteEventByID("invalidID");

        assertFalse(eventDeleted, "Event deletion was successful but should not.");
    }

    // Testen, ob ein Event anhand der ID korrekt ausgelesen werden kann:
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

        assertEquals("testPublicEventID", publicEventFromDatabase.getEventID());
        assertEquals("Ostermarkt", publicEventFromDatabase.getEventName());
        assertEquals("2025-04-04", publicEventFromDatabase.getEventStart());
        assertEquals("2025-04-06", publicEventFromDatabase.getEventEnd());
        assertEquals(0, publicEventFromDatabase.getNumberOfBookedUsersOnEvent());
        assertEquals("Markt", publicEventFromDatabase.getCategory());
        assertEquals(false, publicEventFromDatabase.isPrivateEvent());
        assertEquals(2000, publicEventFromDatabase.getMaximumCapacity());
    }
}
