package de.eventmanager.core.database.Communication;

import de.eventmanager.core.notifications.Notification;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

@Tag("parallelDatabaseTests")
public class NotificationDatabaseConnectorTestDrive {

    Notification testNotification1;
    Notification testNotification2;
    Notification testNotificationUpdated;

    //#region successful CRUD operations

    /**
     * Test adding a notification
     * */
    @Test
    public void testAddNotification() {

        testNotification1 = new Notification("testNotificationToAdd", "testUserForNotificationAdding", "This is a test notification!", false);

        boolean notificationToAdd = NotificationDatabaseConnector.addNotification(testNotification1);
        assertTrue(notificationToAdd, "Notification adding failed but should not.");

        NotificationDatabaseConnector.deleteNotification("testNotificationToAdd");
    }

    /**
     * Test reading a list of notifications by user ID
     */
    @Test
    public void testReadNotifications() {

        testNotification1 = new Notification("testNotificationToRead1", "testUserWhoReadsNotifications", "This is a test notification!", false);
        testNotification2 = new Notification("testNotificationToRead2", "testUserWhoReadsNotifications", "This is another test notification!", true);

        NotificationDatabaseConnector.addNotification(testNotification1);
        NotificationDatabaseConnector.addNotification(testNotification2);

        ArrayList<Notification> notificationList = NotificationDatabaseConnector.readNotificationsByUserID("testUserWhoReadsNotifications");
        ArrayList<Notification> expectedNotificationList = new ArrayList<>();
        expectedNotificationList.add(new Notification("testNotificationToRead1", "testUserWhoReadsNotifications", "This is a test notification!", false));
        expectedNotificationList.add(new Notification("testNotificationToRead2", "testUserWhoReadsNotifications", "This is another test notification!", true));

        assertEquals(expectedNotificationList, notificationList);

        NotificationDatabaseConnector.deleteNotification("testNotificationToRead1");
        NotificationDatabaseConnector.deleteNotification("testNotificationToRead2");
    }

    /**
     * Test updating a notification to mark it as read
     * */
    @Test
    public void testUpdateNotification() {

        testNotification1 = new Notification("testNotificationToUpdate", "testUserForNotificationUpdating", "This is a test notification!", false);
        testNotificationUpdated = new Notification("testNotificationToUpdate", "testUserForNotificationUpdating", "This is a test notification!", true);

        NotificationDatabaseConnector.addNotification(testNotification1);

        boolean notificationToUpdate = NotificationDatabaseConnector.updateNotification(testNotificationUpdated);
        assertTrue(notificationToUpdate, "Notification updated failed but should not.");

        NotificationDatabaseConnector.deleteNotification("testNotificationToUpdate");
    }

    /**
     * Test deleting a notification
     * */
    @Test
    public void testDeleteNotification() {

        testNotification1 = new Notification("testNotificationToDelete", "testUserForNotificationDeleting", "This is a test notification!", true);

        NotificationDatabaseConnector.addNotification(testNotification1);

        boolean notificationToDelete = NotificationDatabaseConnector.deleteNotification("testNotificationToDelete");
        assertTrue(notificationToDelete, "Deleting a notification failed but should not.");
    }

    //#endregion successful CRUD operations

    //#region failed CRUD operations

    /**
     * Test that notifications are unique
     * */
    @Test
    public void testAddNotificationFailed() {

        testNotification1 = new Notification("testNotificationToFailAdding", "testUserWhoAddsNotificationsWrong", "This is a test notification!", false);

        NotificationDatabaseConnector.addNotification(testNotification1);

        boolean notificationToAdd = NotificationDatabaseConnector.addNotification(testNotification1);
        assertFalse(notificationToAdd, "Notification adding was successful but should not.");

        NotificationDatabaseConnector.deleteNotification("testNotificationToFailAdding");
    }

    /**
     * Test getting a notification list for a nonexistent user
     * */
    @Test
    public void testReadingNotificationsFailed() {

        ArrayList<Notification> notificationList = NotificationDatabaseConnector.readNotificationsByUserID("userWithoutNotifications");
        assertTrue(notificationList.isEmpty(), "Getting a notification list successful but should not.");
    }

    /**
     * Test that updating a notification is only possible if there is an entry in the database
     * */
    @Test
    public void testUpdateNotificationFailed() {

        testNotificationUpdated = new Notification("testNotificationToFailUpdating", "testUserWhoUpdatesWrong", "This is a test notification!", true);

        boolean notificationToUpdate = NotificationDatabaseConnector.updateNotification(testNotificationUpdated);
        assertFalse(notificationToUpdate, "Notification update was successful but should not.");
    }

    /**
     * Test deleting a notification with invalid ID
     * */
    @Test
    public void testDeleteNotificationFailed() {

        boolean notificationToDelete = NotificationDatabaseConnector.deleteNotification("invalidNotificationIDToDelete");
        assertFalse(notificationToDelete, "Notification deletion was successful but should not.");
    }

    //#endregion failed CRUD operations

}
