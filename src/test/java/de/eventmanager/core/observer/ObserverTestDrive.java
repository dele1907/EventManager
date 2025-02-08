package de.eventmanager.core.observer;

import de.eventmanager.core.database.Communication.BookingDatabaseConnector;
import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import de.eventmanager.core.database.Communication.NotificationDatabaseConnector;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.notifications.Notification;
import de.eventmanager.core.users.Management.UserManagerImpl;
import de.eventmanager.core.users.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObserverTestDrive {

    User testAdmin;
    User testUser;
    PublicEvent testEvent;

    /**
     * Test that notification via observer pattern is working
     */
    @Test
    public void testObserverPattern() {

        testAdmin = new User("testObserverAdmin", "Thaddäus", "Tentakel", "1975-10-23", "adminToEditEvent@testmail.com", "Password1234", "1234", true);
        testUser = new User("testObserverUser", "Spongebob", "Schwammkopf", "1986-07-14", "observer@testmail.com", "Password0815", "0815", false);

        testEvent = new PublicEvent("testEventToObserve", "Wacken Open Air", "2025-07-29", "2025-08-01", 0, null, "Festival", false,
                "25596", "Wacken", "Norderstraße", "Festivalgelände", "Metal-Festival in Deutschland", 90000, 16);

        UserManagerImpl userManager = new UserManagerImpl();

        // create admin, regular user and event
        UserDatabaseConnector.createNewUser(testAdmin);
        UserDatabaseConnector.createNewUser(testUser);
        EventDatabaseConnector.createNewEvent(testEvent);

        // book event to add user as observer and edit event to notify user
        userManager.bookEvent("testEventToObserve", "testObserverUser");
        userManager.editEvent("testEventToObserve", "Wacken Open Air", "2025-07-31", "2025-08-03", "Festival",
                "25596", "Norderstraße", "Festivalgelände", "Metal-Festival in Deutschland", "testObserverAdmin");

        // check if notification is correct and delete it from database
        ArrayList<Notification> notificationList = NotificationDatabaseConnector.readNotificationsByUserID("testObserverUser");
        assertEquals(1, notificationList.size());
        assertEquals("testObserverUser", notificationList.get(0).getUserID());
        for (Notification notification : notificationList) {
            NotificationDatabaseConnector.deleteNotification(notification.getNotificationID());
        }

        // cancel event to remove user as observer and edit event again
        userManager.cancelEvent("testEventToObserve", "testObserverUser");
        userManager.editEvent("testEventToObserve", "Wacken Open Air", "2025-07-30", "2025-08-02", "Festival",
                "25596", "Norderstraße", "Festivalgelände", "Metal-Festival in Deutschland", "testObserverAdmin");

        // check if user isn't notified anymore
        ArrayList<Notification> newNotificationList = NotificationDatabaseConnector.readNotificationsByUserID("testObserverUser");
        assertTrue(newNotificationList.isEmpty(), "Notification list has data but should not.");

        // remove test data from database
        BookingDatabaseConnector.removeBooking("testEventToObserve", "testObserverUser");
        EventDatabaseConnector.deleteEventByID("testEventToObserve");
        UserDatabaseConnector.deleteUserByID("testObserverAdmin");
        UserDatabaseConnector.deleteUserByID("testObserverUser");
    }

}