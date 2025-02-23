package de.eventmanager.core.database.Communication;

import de.eventmanager.core.notifications.Notification;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class NotificationDatabaseConnectorTestDrive {

    private Notification testNotification1;
    private Notification testNotification2;
    private static final String TEST_MESSAGE = "This is a test notification!";
    private static final String TEST_USER_ID = "testUserIDForNotificationDatabaseConnector";
    private static final String TEST_USER_ID_FOR_READING = "testUserWhoReadsNotifications";
    private static final String INVALID_USER_ID = "invalidUserIDForNotificationDatabaseConnector";
    private static final String INVALID_NOTIFICATION_ID = "invalidNotificationIDForNotificationDatabaseConnector";

    //#region successful CRUD operations

    /**
     * Test adding a notification
     * */
    @Test
    public void testAddNotification() {

        testNotification1 = new Notification("testNotificationToAdd", TEST_USER_ID, TEST_MESSAGE, false);

        boolean notificationToAdd = NotificationDatabaseConnector.addNotification(testNotification1);
        assertTrue(notificationToAdd, "Notification adding failed but should not.");

        NotificationDatabaseConnector.deleteNotification(testNotification1.getNotificationID());
    }

    /**
     * Test reading a list of notifications by user ID
     */
    @Test
    public void testReadNotifications() {

        testNotification1 = new Notification("testNotificationToRead1", TEST_USER_ID_FOR_READING, TEST_MESSAGE, false);
        testNotification2 = new Notification("testNotificationToRead2", TEST_USER_ID_FOR_READING, TEST_MESSAGE, true);

        NotificationDatabaseConnector.addNotification(testNotification1);
        NotificationDatabaseConnector.addNotification(testNotification2);

        ArrayList<Notification> notificationList = NotificationDatabaseConnector.readNotificationsByUserID(TEST_USER_ID_FOR_READING);
        ArrayList<Notification> expectedNotificationList = new ArrayList<>();
        expectedNotificationList.add(testNotification1);
        expectedNotificationList.add(testNotification2);

        assertEquals(expectedNotificationList, notificationList);

        NotificationDatabaseConnector.deleteNotification(testNotification1.getNotificationID());
        NotificationDatabaseConnector.deleteNotification(testNotification2.getNotificationID());
    }

    /**
     * Test updating a notification to mark it as read
     * */
    @Test
    public void testUpdateNotification() {

        testNotification1 = new Notification("testNotificationToUpdate", TEST_USER_ID, TEST_MESSAGE, false);
        testNotification2 = new Notification("testNotificationToUpdate", TEST_USER_ID, TEST_MESSAGE, true);

        NotificationDatabaseConnector.addNotification(testNotification1);

        boolean notificationToUpdate = NotificationDatabaseConnector.updateNotification(testNotification2);
        assertTrue(notificationToUpdate, "Notification updated failed but should not.");

        NotificationDatabaseConnector.deleteNotification(testNotification1.getNotificationID());
    }

    /**
     * Test deleting a notification
     * */
    @Test
    public void testDeleteNotification() {

        testNotification1 = new Notification("testNotificationToDelete", TEST_USER_ID, TEST_MESSAGE, true);

        NotificationDatabaseConnector.addNotification(testNotification1);

        boolean notificationToDelete = NotificationDatabaseConnector.deleteNotification(testNotification1.getNotificationID());
        assertTrue(notificationToDelete, "Deleting a notification failed but should not.");
    }

    //#endregion successful CRUD operations

    //#region failed CRUD operations

    /**
     * Test that notifications are unique
     * */
    @Test
    public void testAddNotificationFailed() {

        testNotification1 = new Notification("testNotificationToFailAdding", TEST_USER_ID, TEST_MESSAGE, false);

        NotificationDatabaseConnector.addNotification(testNotification1);

        boolean notificationToAdd = NotificationDatabaseConnector.addNotification(testNotification1);
        assertFalse(notificationToAdd, "Notification adding was successful but should not.");

        NotificationDatabaseConnector.deleteNotification(testNotification1.getNotificationID());
    }

    /**
     * Test getting a notification list for a nonexistent user
     * */
    @Test
    public void testReadingNotificationsFailed() {

        ArrayList<Notification> notificationList = NotificationDatabaseConnector.readNotificationsByUserID(INVALID_USER_ID);
        assertTrue(notificationList.isEmpty(), "Getting a notification list successful but should not.");
    }

    /**
     * Test that updating a notification is only possible if there is an entry in the database
     * */
    @Test
    public void testUpdateNotificationFailed() {

        testNotification1 = new Notification(INVALID_NOTIFICATION_ID, INVALID_USER_ID, TEST_MESSAGE, true);

        boolean notificationToUpdate = NotificationDatabaseConnector.updateNotification(testNotification1);
        assertFalse(notificationToUpdate, "Notification update was successful but should not.");
    }

    /**
     * Test deleting a notification with invalid ID
     * */
    @Test
    public void testDeleteNotificationFailed() {

        boolean notificationToDelete = NotificationDatabaseConnector.deleteNotification(INVALID_NOTIFICATION_ID);
        assertFalse(notificationToDelete, "Notification deletion was successful but should not.");
    }

    //#endregion failed CRUD operations

}
