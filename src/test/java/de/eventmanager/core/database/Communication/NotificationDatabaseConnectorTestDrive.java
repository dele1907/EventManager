package de.eventmanager.core.database.Communication;

import de.eventmanager.core.notifications.Notification;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.jooq.generated.tables.Notifications.NOTIFICATIONS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotificationDatabaseConnectorTestDrive {

    Notification testNotification1;
    Notification testNotification2;
    Notification testNotificationUpdated;

    /**
     * Clean up the database after testing
     * */
    /*@AfterAll
    static void cleanUp() throws SQLException {

        try (Connection cleanupConnection = DatabaseConnector.connect()) {
           DSLContext cleanupDsl = DSL.using(cleanupConnection, SQLDialect.SQLITE);
            cleanupDsl.deleteFrom(NOTIFICATIONS).execute();
        }
    }*/

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

    // TODO: adding tests for failed methods

    //#endregion failed CRUD operations

}
