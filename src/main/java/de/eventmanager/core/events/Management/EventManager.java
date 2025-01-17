package de.eventmanager.core.events.Management;

import java.sql.Connection;

import de.eventmanager.core.database.Communication.DatabaseConnector;
import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import helper.LoggerHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;

// import static org.jooq.generated.tables...

public class EventManager {

    private static final String EVENT_ADDED = "Event added successfully";
    private static final String EVENT_NOT_ADDED = "Error adding event: ";
    private static final String EVENT_NOT_READ = "Error reading event: ";
    private static final String EVENT_UPDATED = "Event updated successfully";
    private static final String EVENT_NOT_UPDATED = "Error updating event: ";
    private static final String EVENT_NOT_FOUND = "No event found with the given ID";
    private static final String EVENT_DELETED = "Event deleted successfully";
    private static final String EVENT_NOT_DELETED = "Error deleting event: ";

    public static boolean createEvent(EventModel event) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            int rowsAffected = 0;

            // TODO: Event in Datenbank anlegen

            if (rowsAffected > 0) {
                LoggerHelper.logInfoMessage(EventManager.class, EVENT_ADDED);

                return true;
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventManager.class, EVENT_NOT_ADDED + exception.getMessage());
        }

        return false;
    }

    // Event laden (READ) anhand der ID
    public static EventModel readEventByID(String userID) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Record record = null;

            if (record != null) {

                // TODO: Event aus Datenbank lesen

                return null;
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventManager.class, EVENT_NOT_READ + exception.getMessage());
        }

        return null;
    }

    // Event ändern (UPDATE)
    public static boolean updateEvent(EventModel event) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            int rowsUpdated = 0;

            // TODO: Event in Datenbank ändern

            if (rowsUpdated > 0) {
                LoggerHelper.logInfoMessage(EventManager.class, EVENT_UPDATED);

                return true;
            } else {
                LoggerHelper.logInfoMessage(EventManager.class, EVENT_NOT_FOUND);

                return false;
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventManager.class, EVENT_NOT_UPDATED + exception.getMessage());

            return false;
        }
    }

    // Event löschen (DELETE)
    public static boolean deleteEventByID(String eventID) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            int rowsAffected = 0;

            // TODO: Event aus Datenbank löschen

            if (rowsAffected > 0) {
                LoggerHelper.logInfoMessage(EventManager.class, EVENT_DELETED);
            } else {
                LoggerHelper.logInfoMessage(EventManager.class, EVENT_NOT_FOUND);
            }

            return rowsAffected > 0;

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventManager.class, EVENT_NOT_DELETED + exception.getMessage());

            return false;
        }
    }

}
