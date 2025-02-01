package de.eventmanager.core.database.Communication;

import de.eventmanager.core.users.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingDatabaseConnectorTestDrive {

    // TODO: rework tests

    /**
     * Test relation on booking
     * */
    /*@Test
    public void testAddAndDeleteBooking() {

        EventDatabaseConnector.createNewEvent(testPrivateEvent);
        EventDatabaseConnector.createNewEvent(testPublicEvent);

        boolean testAddBooking = BookingDatabaseConnector.addBooking("testPublicEventID", "testCreatorID");

        assertTrue(testAddBooking, "Adding user to booked event failed but should not.");

        boolean testRemoveBooking = BookingDatabaseConnector.removeBooking("testPublicEventID", "testCreatorID");

        assertTrue(testRemoveBooking, "Adding user to booked event failed but should not.");
    }*/

    /**
     * Test relation on booking
     * */
    /*@Test
    public void getBookedUsersOnEvent() {

        skipSetUp = true;
        skipCleanUp = true;

        User testUser1 = new User("testBookingUserID1", "Peter", "Bookman", "2000-02-02","peter.bookman@testmail.com","Password123", "0815", false);
        User testUser2 = new User("testBookingUserID2", "Herbert", "Bookson", "1980-08-08", "herbert.bookson@testmail.com","Password456", "4711", true);

        UserDatabaseConnector.createNewUser(testUser1);
        UserDatabaseConnector.createNewUser(testUser2);

        EventDatabaseConnector.createNewEvent(testPrivateEvent);
        EventDatabaseConnector.createNewEvent(testPublicEvent);

        BookingDatabaseConnector.addBooking("testPublicEventID", "testBookingUserID1");
        BookingDatabaseConnector.addBooking("testPublicEventID", "testBookingUserID2");

        ArrayList<String> bookedTestUsers = BookingDatabaseConnector.getBookedUsersOnEvent("testPublicEventID");
        ArrayList<String> expectedBookedTestUsers = new ArrayList<>();
        expectedBookedTestUsers.add("peter.bookman@testmail.com");
        expectedBookedTestUsers.add("herbert.bookson@testmail.com");

        // test of user list
        assertEquals(expectedBookedTestUsers, bookedTestUsers);

        int numberOfBookedUsers = EventDatabaseConnector.readPublicEventByID("testPublicEventID").get().getNumberOfBookedUsersOnEvent();

        // test of user number after booking
        assertEquals(2, numberOfBookedUsers);

        BookingDatabaseConnector.removeBooking("testPublicEventID", "testBookingUserID1");
        BookingDatabaseConnector.removeBooking("testPublicEventID", "testBookingUserID2");

        int newNumberOfBookedUsers = EventDatabaseConnector.readPublicEventByID("testPublicEventID").get().getNumberOfBookedUsersOnEvent();

        // test of user number after delete booking
        assertEquals(0, newNumberOfBookedUsers);

        EventDatabaseConnector.deleteEventByID("testPrivateEventID");
        EventDatabaseConnector.deleteEventByID("testPublicEventID");

        UserDatabaseConnector.deleteUserByID("testBookingUserID1");
        UserDatabaseConnector.deleteUserByID("testBookingUserID2");
    }*/

}
