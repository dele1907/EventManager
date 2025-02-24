package de.eventmanager.core.observer;

import de.eventmanager.core.database.Communication.*;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.notifications.Notification;
import de.eventmanager.core.users.Management.UserManagerImpl;
import de.eventmanager.core.users.User;
import helper.LoggerHelper;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.jooq.generated.tables.Cities.CITIES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObserverTestDrive {

    private PublicEvent testEvent;

    private static final UserManagerImpl USER_MANAGER = new UserManagerImpl();
    private static final User TEST_ADMIN = new User("testObserverAdmin", "Thaddäus", "Tentakel", "1975-10-23", "adminToEditEvent@testmail.com", "Password1234", "1234", true);
    private static final User TEST_USER = new User("testObserverUser", "Spongebob", "Schwammkopf", "1986-07-14", "observer@testmail.com", "Password0815", "0815", false);
    private static final String STANDARD_EVENT_ID = "testEventToObserve";

    @BeforeAll
    static void globalSetUp() {
        UserDatabaseConnector.createNewUser(TEST_ADMIN);
        UserDatabaseConnector.createNewUser(TEST_USER);
    }

    @AfterAll
    static void globalCleanUp() {
        BookingDatabaseConnector.removeBooking(STANDARD_EVENT_ID, TEST_USER.getUserID());
        EventDatabaseConnector.deleteEventByID(STANDARD_EVENT_ID, TEST_ADMIN.getUserID());
        UserDatabaseConnector.deleteUserByID(TEST_USER.getUserID());
        UserDatabaseConnector.deleteUserByID(TEST_ADMIN.getUserID());

        testDatabaseCleanUp();
    }

    static void testDatabaseCleanUp() {
        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            create.deleteFrom(CITIES)
                    .where(CITIES.POSTALCODE.eq("O-25596"))
                    .execute();

        } catch (Exception e) {
            LoggerHelper.logErrorMessage(ObserverTestDrive.class, e.getMessage());
        }
    }

    /**
     * Test that notification via observer pattern is working
     */
    @Test
    public void testObserverPattern() {

        // create event to observe
        testEvent = new PublicEvent(STANDARD_EVENT_ID, "Wacken Open Air", "2025-07-29", "2025-08-01", 0, null, "Festival", false,
                "O-25596", "Wacken", "Norderstraße", "Festivalgelände", "Metal-Festival in Deutschland", 90000, 16);

        EventDatabaseConnector.createNewEvent(testEvent, TEST_ADMIN.getUserID());

        // book event to add user as observer and edit event to notify user
        USER_MANAGER.bookEvent(testEvent.getEventID(), TEST_USER.getUserID());
        USER_MANAGER.editEvent(testEvent.getEventID(), testEvent.getEventID(), testEvent.getEventStart(), testEvent.getEventEnd(), "Festival",
                "O-25596", "Norderstraße", "Festivalgelände", "Metal-Festival in Deutschland", TEST_ADMIN.getUserID());

        // check if notification is correct and delete it from database
        ArrayList<Notification> notificationList = NotificationDatabaseConnector.readNotificationsByUserID(TEST_USER.getUserID());
        assertEquals(1, notificationList.size());
        assertEquals(TEST_USER.getUserID(), notificationList.get(0).getUserID());
        for (Notification notification : notificationList) {
            NotificationDatabaseConnector.deleteNotification(notification.getNotificationID());
        }

        // cancel event to remove user as observer and edit event again
        USER_MANAGER.cancelEvent(testEvent.getEventID(), TEST_USER.getUserID());
        USER_MANAGER.editEvent(testEvent.getEventID(), STANDARD_EVENT_ID, "2025-07-30", "2025-08-02", "Festival",
                "O-25596", "Norderstraße", "Festivalgelände", "Metal-Festival in Deutschland", TEST_ADMIN.getUserID());

        // check if user isn't notified anymore
        ArrayList<Notification> newNotificationList = NotificationDatabaseConnector.readNotificationsByUserID(TEST_USER.getUserID());
        assertTrue(newNotificationList.isEmpty(), "Notification list has data but should not.");
    }

}