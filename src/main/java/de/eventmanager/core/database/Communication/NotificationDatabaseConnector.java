package de.eventmanager.core.database.Communication;

import de.eventmanager.core.notifications.Notification;
import helper.LoggerHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.util.ArrayList;

import static org.jooq.generated.tables.Notifications.NOTIFICATIONS;

public class NotificationDatabaseConnector {

    //#region constants

    private static final String NOTIFICATION_ADDED = "Notification added successfully";
    private static final String NOTIFICATION_NOT_ADDED = "Error adding notification: ";
    private static final String NOTIFICATION_NOT_READ = "Error reading notification: ";
    private static final String NOTIFICATION_UPDATED = "Notification updated successfully";
    private static final String NOTIFICATION_NOT_UPDATED = "Error updating notification: ";
    private static final String NOTIFICATION_NOT_FOUND = "No notification found with the given ID";
    private static final String NOTIFICATION_DELETED = "Notification deleted successfully";
    private static final String NOTIFICATION_NOT_DELETED = "Error deleting notification: ";

    //#endregion constants

    //#region CRUD operations

    /**
     * CREATE a notification
     * */
    public static boolean addNotification(Notification notification) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            int rowsAffected = create.insertInto(NOTIFICATIONS,
                            NOTIFICATIONS.NOTIFICATIONID,
                            NOTIFICATIONS.USERID,
                            NOTIFICATIONS.MESSAGE,
                            NOTIFICATIONS.MARKEDASREAD)
                    .values(
                            notification.getNotificationID(),
                            notification.getUserID(),
                            notification.getMessage(),
                            notification.isMarkedAsRead())
                    .execute();

            if (rowsAffected > 0) {
                LoggerHelper.logInfoMessage(UserDatabaseConnector.class, NOTIFICATION_ADDED);

                return true;
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(UserDatabaseConnector.class, NOTIFICATION_NOT_ADDED + exception.getMessage());
        }

        return false;
    }

    /**
     * READ a list of notifications by userID
     * */
    public static ArrayList<Notification> readNotificationsByUserID(String userID) {
        ArrayList<Notification> notifications = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record> records = create.select()
                    .from(NOTIFICATIONS)
                    .where(NOTIFICATIONS.USERID.eq(userID))
                    .fetch();

            for (var record : records) {
                Notification notification = new Notification(
                        record.get(NOTIFICATIONS.NOTIFICATIONID),
                        record.get(NOTIFICATIONS.USERID),
                        record.get(NOTIFICATIONS.MESSAGE),
                        record.get(NOTIFICATIONS.MARKEDASREAD)
                );

                notifications.add(notification);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NOTIFICATION_NOT_READ + exception.getMessage());
        }

        return notifications;
    }

    /**
     * UPDATE a notification
     * */
    public static boolean updateNotification(Notification notification) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);
            int rowsUpdated = create.update(NOTIFICATIONS)
                    .set(NOTIFICATIONS.MARKEDASREAD, notification.isMarkedAsRead())
                    .where(NOTIFICATIONS.NOTIFICATIONID.eq(notification.getNotificationID()))
                    .execute();

            if (rowsUpdated > 0) {
                LoggerHelper.logInfoMessage(UserDatabaseConnector.class, NOTIFICATION_UPDATED);

                return true;
            } else {
                LoggerHelper.logInfoMessage(UserDatabaseConnector.class, NOTIFICATION_NOT_FOUND);

                return false;
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(UserDatabaseConnector.class, NOTIFICATION_NOT_UPDATED + exception.getMessage());

            return false;
        }
    }

    /**
     * DELETE a notification by ID
     * */
    public static boolean deleteNotification(String notificationID) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            int rowsAffected = create.deleteFrom(NOTIFICATIONS)
                    .where(NOTIFICATIONS.NOTIFICATIONID.eq(notificationID))
                    .execute();

            if (rowsAffected > 0) {
                LoggerHelper.logInfoMessage(UserDatabaseConnector.class, NOTIFICATION_DELETED);
            } else {
                LoggerHelper.logInfoMessage(UserDatabaseConnector.class, NOTIFICATION_NOT_FOUND);
            }

            return rowsAffected > 0;

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(UserDatabaseConnector.class, NOTIFICATION_NOT_DELETED + exception.getMessage());

            return false;
        }
    }

    //#endregion CRUD operations

}
