package de.eventmanager.core.database.Communication;

import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.users.User;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.jooq.generated.tables.Booked.BOOKED;
import static org.jooq.generated.tables.Events.EVENTS;
import static org.jooq.generated.tables.User.USER;
import static org.junit.jupiter.api.Assertions.*;

public class BookingDatabaseConnectorTestDrive {

    User testUserForBooking1;
    User testUserForBooking2;
    PublicEvent testEventForBooking;

    /**
     * Clean up the database after testing
     * */
    /*@AfterAll
    static void cleanUp() throws SQLException {

        try (Connection cleanupConnection = DatabaseConnector.connect()) {
           DSLContext cleanupDsl = DSL.using(cleanupConnection, SQLDialect.SQLITE);
            cleanupDsl.deleteFrom(USER).execute();
            cleanupDsl.deleteFrom(EVENTS).execute();
            cleanupDsl.deleteFrom(BOOKED).execute();
        }
    }*/

    //#region successful CRUD operations

    /**
     * Test adding a booking
     * */
    @Test
    public void testAddBooking() {

        testEventForBooking = new PublicEvent("testEventToBook", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        EventDatabaseConnector.createNewEvent(testEventForBooking);

        boolean userBookedEvent = BookingDatabaseConnector.addBooking("testEventToBook", "testUserWhoBooks");
        assertTrue(userBookedEvent, "Booking failed but should not.");

        BookingDatabaseConnector.removeBooking("testEventToBook", "testUserWhoBooks");
        EventDatabaseConnector.deleteEventByID("testEventToBook");
    }

    /**
     * Test getting a booking list of an event
     * */
    @Test
    public void testGetBookedUsersOnEvent() {

        testUserForBooking1 = new User("userTestBookingDatabaseConnector1", "Uwe", "Buchungstester", "1970-02-02","uwe.bookingtest@testmail.com","Password123", "0815", false);
        testUserForBooking2 = new User("userTestBookingDatabaseConnector2", "Manfred", "Buchungstester", "1960-08-08", "manfred.bookingtest@testmail.com","Password456", "4711", true);
        testEventForBooking = new PublicEvent("eventTestBookingDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        UserDatabaseConnector.createNewUser(testUserForBooking1);
        UserDatabaseConnector.createNewUser(testUserForBooking2);
        EventDatabaseConnector.createNewEvent(testEventForBooking);

        BookingDatabaseConnector.addBooking("eventTestBookingDatabaseConnector", "userTestBookingDatabaseConnector1");
        BookingDatabaseConnector.addBooking("eventTestBookingDatabaseConnector", "userTestBookingDatabaseConnector2");

        ArrayList<String> bookedTestUsers = BookingDatabaseConnector.getBookedUsersOnEvent("eventTestBookingDatabaseConnector");
        ArrayList<String> expectedBookedTestUsers = new ArrayList<>();
        expectedBookedTestUsers.add("uwe.bookingtest@testmail.com");
        expectedBookedTestUsers.add("manfred.bookingtest@testmail.com");

        // test of user list
        assertEquals(expectedBookedTestUsers, bookedTestUsers);

        int numberOfUsersAfterBooking = EventDatabaseConnector.readPublicEventByID("eventTestBookingDatabaseConnector").get().getNumberOfBookedUsersOnEvent();

        // test of user number after booking
        assertEquals(2, numberOfUsersAfterBooking);

        BookingDatabaseConnector.removeBooking("eventTestBookingDatabaseConnector", "userTestBookingDatabaseConnector1");
        BookingDatabaseConnector.removeBooking("eventTestBookingDatabaseConnector", "userTestBookingDatabaseConnector2");

        int numberOfUsersAfterCancelling = EventDatabaseConnector.readPublicEventByID("eventTestBookingDatabaseConnector").get().getNumberOfBookedUsersOnEvent();

        // test of user number after cancelling
        assertEquals(0, numberOfUsersAfterCancelling);

        EventDatabaseConnector.deleteEventByID("eventTestBookingDatabaseConnector");
        UserDatabaseConnector.deleteUserByID("userTestBookingDatabaseConnector1");
        UserDatabaseConnector.deleteUserByID("userTestBookingDatabaseConnector2");
    }

    /**
     * Test removing a booking
     * */
    @Test
    public void testRemoveBooking() {

        testEventForBooking = new PublicEvent("testEventToCancel", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
                "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

        EventDatabaseConnector.createNewEvent(testEventForBooking);

        BookingDatabaseConnector.addBooking("testEventToCancel", "testUserWhoCancels");

        boolean userCanceledEvent = BookingDatabaseConnector.removeBooking("testEventToCancel", "testUserWhoCancels");
        assertTrue(userCanceledEvent, "Remove booking failed but should not.");

        EventDatabaseConnector.deleteEventByID("testEventToCancel");
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

        EventDatabaseConnector.createNewEvent(testEventForBooking);
        BookingDatabaseConnector.addBooking("testEventToFailBooking", "testUserWhoBooksWrong");

        boolean userBookedEvent = BookingDatabaseConnector.addBooking("testEventToFailBooking", "testUserWhoBooksWrong");
        assertFalse(userBookedEvent, "Booking an event was successful but should not.");

        BookingDatabaseConnector.removeBooking("testEventToFailBooking", "testUserWhoBooksWrong");
        EventDatabaseConnector.deleteEventByID("testEventToFailBooking");
    }

    /**
     * Test getting a booking list of a nonexistent event
     * */
    @Test
    public void testGettingBookedUsersOnEventFailed() {

        ArrayList<String> bookedTestUsers = BookingDatabaseConnector.getBookedUsersOnEvent("invalidEventIDToGetBookedUsers");
        assertTrue(bookedTestUsers.isEmpty(), "Getting a list of booked users was successful but should not.");
    }

    /**
     * Test removing a booking which doesn't exist
     * */
    @Test
    public void testRemoveBookingFailed() {

        boolean userCancelledEvent = BookingDatabaseConnector.removeBooking("testEventToFailCancelling", "testUserWhoCancelsWrong");
        assertFalse(userCancelledEvent, "Cancelling an event was successful but should not.");
    }

    //#endregion failed CRUD operations

}
