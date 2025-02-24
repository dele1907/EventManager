package de.eventmanager.core.database.Communication;

import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.users.User;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;

public class BookingDatabaseConnectorTestDrive {

    PublicEvent testEventForBooking;

    private static final User TEST_USER_FOR_BOOKING_1 = new User("testUserForBookingDatabaseConnector1", "Uwe", "Buchungstester", "1970-02-02","uwe.bookingtest@testmail.com","Password123", "0815", false);
    private static final User TEST_USER_FOR_BOOKING_2 = new User("testUserForBookingDatabaseConnector2", "Manfred", "Buchungstester", "1960-08-08", "manfred.bookingtest@testmail.com","Password456", "4711", true);
    private static final String TEST_CREATOR_ID = "testCreatorIDForBookingDatabaseConnector";
    private static final String INVALID_USER_ID = "invalidUserIDForBookingDatabaseConnector";
    private static final String INVALID_EVENT_ID = "invalidEventIDForBookingDatabaseConnector";

    @BeforeAll
    static void globalSetUp() {
        UserDatabaseConnector.createNewUser(TEST_USER_FOR_BOOKING_1);
        UserDatabaseConnector.createNewUser(TEST_USER_FOR_BOOKING_2);
    }

    @AfterAll
    static void globalCleanUp() {
        UserDatabaseConnector.deleteUserByID(TEST_USER_FOR_BOOKING_1.getUserID());
        UserDatabaseConnector.deleteUserByID(TEST_USER_FOR_BOOKING_2.getUserID());
    }

    //#region successful CRUD operations

    /**
     * Test adding a booking
     * */
    @Test
    public void testAddBooking() {

        testEventForBooking = new PublicEvent("testEventToBook", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        EventDatabaseConnector.createNewEvent(testEventForBooking, TEST_CREATOR_ID);

        boolean userBookedEvent = BookingDatabaseConnector.addBooking(testEventForBooking.getEventID(), TEST_USER_FOR_BOOKING_1.getUserID());
        assertTrue(userBookedEvent, "Booking failed but should not.");

        BookingDatabaseConnector.removeBooking(testEventForBooking.getEventID(), TEST_USER_FOR_BOOKING_1.getUserID());
        EventDatabaseConnector.deleteEventByID(testEventForBooking.getEventID(), TEST_CREATOR_ID);
    }

    /**
     * Test getting a booking list of an event
     * */
    @Test
    public void testGetBooking() {

        testEventForBooking = new PublicEvent("testEventToGetBooking", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        EventDatabaseConnector.createNewEvent(testEventForBooking, TEST_CREATOR_ID);

        BookingDatabaseConnector.addBooking(testEventForBooking.getEventID(), TEST_USER_FOR_BOOKING_1.getUserID());
        BookingDatabaseConnector.addBooking(testEventForBooking.getEventID(), TEST_USER_FOR_BOOKING_2.getUserID());

        ArrayList<Optional<? extends EventModel>> bookedTestEvents = BookingDatabaseConnector.getEventsBookedByUser(TEST_USER_FOR_BOOKING_1.getUserID());
        ArrayList<Optional<? extends EventModel>> expectedBookedTestEvents = new ArrayList<>();
        expectedBookedTestEvents.add(Optional.ofNullable(testEventForBooking));

        // test of event list for user 1
        assertEquals(expectedBookedTestEvents, bookedTestEvents);

        ArrayList<String> bookedTestUsers = BookingDatabaseConnector.getBookedUsersInformationOnEvent(testEventForBooking.getEventID());
        ArrayList<String> expectedBookedTestUsers = new ArrayList<>();
        expectedBookedTestUsers.add(TEST_USER_FOR_BOOKING_1.getEMailAddress());
        expectedBookedTestUsers.add(TEST_USER_FOR_BOOKING_2.getEMailAddress());

        // test of user list
        assertEquals(expectedBookedTestUsers, bookedTestUsers);

        // test of user number after booking
        assertEquals(2, EventDatabaseConnector.readPublicEventByID(testEventForBooking.getEventID()).get().getNumberOfBookedUsersOnEvent());

        BookingDatabaseConnector.removeBooking(testEventForBooking.getEventID(), TEST_USER_FOR_BOOKING_1.getUserID());
        BookingDatabaseConnector.removeBooking(testEventForBooking.getEventID(), TEST_USER_FOR_BOOKING_2.getUserID());

        // test of user number after cancelling
        assertEquals(0, EventDatabaseConnector.readPublicEventByID(testEventForBooking.getEventID()).get().getNumberOfBookedUsersOnEvent());

        EventDatabaseConnector.deleteEventByID(testEventForBooking.getEventID(), TEST_CREATOR_ID);
        UserDatabaseConnector.deleteUserByID(TEST_USER_FOR_BOOKING_1.getUserID());
        UserDatabaseConnector.deleteUserByID(TEST_USER_FOR_BOOKING_2.getUserID());
    }

    /**
     * Test removing a booking
     * */
    @Test
    public void testRemoveBooking() {

        testEventForBooking = new PublicEvent("testEventToCancel", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        EventDatabaseConnector.createNewEvent(testEventForBooking, TEST_CREATOR_ID);

        BookingDatabaseConnector.addBooking(testEventForBooking.getEventID(), TEST_USER_FOR_BOOKING_1.getUserID());

        boolean userCanceledEvent = BookingDatabaseConnector.removeBooking(testEventForBooking.getEventID(), TEST_USER_FOR_BOOKING_1.getUserID());
        assertTrue(userCanceledEvent, "Remove booking failed but should not.");

        EventDatabaseConnector.deleteEventByID(testEventForBooking.getEventID(), TEST_CREATOR_ID);
    }

    //#endregion successful CRUD operations

    //#region failed CRUD operations

    /**
     * Test that bookings are unique
     * */
    @Test
    public void testAddBookingFailed() {

        testEventForBooking = new PublicEvent("testEventToFailBooking", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        EventDatabaseConnector.createNewEvent(testEventForBooking, TEST_CREATOR_ID);
        BookingDatabaseConnector.addBooking(testEventForBooking.getEventID(), TEST_USER_FOR_BOOKING_1.getUserID());

        boolean userBookedEvent = BookingDatabaseConnector.addBooking(testEventForBooking.getEventID(), TEST_USER_FOR_BOOKING_1.getUserID());
        assertFalse(userBookedEvent, "Booking an event was successful but should not.");

        BookingDatabaseConnector.removeBooking(testEventForBooking.getEventID(), TEST_USER_FOR_BOOKING_1.getUserID());
        EventDatabaseConnector.deleteEventByID(testEventForBooking.getEventID(), TEST_CREATOR_ID);
    }

    /**
     * Test getting a user list of a nonexistent event
     * */
    @Test
    public void testGettingBookedUsersOnEventFailed() {

        ArrayList<String> bookedTestUsers = BookingDatabaseConnector.getBookedUsersInformationOnEvent(INVALID_EVENT_ID);
        assertTrue(bookedTestUsers.isEmpty(), "Getting a list of booked users was successful but should not.");
    }

    /**
     * Test getting an event list of a nonexistent user
     * */
    @Test
    public void testGettingEventsBookedByUserFailed() {

        ArrayList<Optional<? extends EventModel>> bookedTestEvents = BookingDatabaseConnector.getEventsBookedByUser(INVALID_USER_ID);
        assertTrue(bookedTestEvents.isEmpty(), "Getting a list of booked events was successful but should not.");
    }

    /**
     * Test removing a booking which doesn't exist
     * */
    @Test
    public void testRemoveBookingFailed() {

        boolean userCancelledEvent = BookingDatabaseConnector.removeBooking(INVALID_EVENT_ID, INVALID_USER_ID);
        assertFalse(userCancelledEvent, "Cancelling an event was successful but should not.");
    }

    //#endregion failed CRUD operations

}
