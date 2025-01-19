package de.eventmanager.core.events.Management;

import java.sql.Connection;
import java.util.Optional;

import de.eventmanager.core.database.Communication.DatabaseConnector;
import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import helper.LoggerHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;

import static org.jooq.generated.tables.Events.EVENTS;

public class EventManager {

    private static final String EVENT_ADDED = "Event added successfully";
    private static final String EVENT_NOT_ADDED = "Error adding event: ";
    private static final String EVENT_NOT_READ = "Error reading event: ";
    private static final String EVENT_UPDATED = "Event updated successfully";
    private static final String EVENT_NOT_UPDATED = "Error updating event: ";
    private static final String EVENT_NOT_FOUND = "No event found with the given ID";
    private static final String EVENT_DELETED = "Event deleted successfully";
    private static final String EVENT_NOT_DELETED = "Error deleting event: ";

    public static boolean createNewEvent(EventModel event) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            int rowsAffected = 0;

            if (event.isPrivateEvent()) {
                PrivateEvent privateEvent = (PrivateEvent) event;

                rowsAffected = create.insertInto(EVENTS,
                                EVENTS.EVENTID,
                                EVENTS.EVENTNAME,
                                EVENTS.EVENTDATETIME,
                                EVENTS.NUMBEROFBOOKEDUSERSONEVENT,
                                EVENTS.CATEGORY,
                                EVENTS.PRIVATEEVENT)
                        .values(
                                privateEvent.getEventID(),
                                privateEvent.getEventName(),
                                privateEvent.getEventDateTime(),
                                privateEvent.getNumberOfBookedUsersOnEvent(),
                                privateEvent.getCategory(),
                                privateEvent.isPrivateEvent())
                        .execute();
            } else {
                PublicEvent publicEvent = (PublicEvent) event;

                rowsAffected = create.insertInto(EVENTS,
                                EVENTS.EVENTID,
                                EVENTS.EVENTNAME,
                                EVENTS.EVENTDATETIME,
                                EVENTS.NUMBEROFBOOKEDUSERSONEVENT,
                                EVENTS.CATEGORY,
                                EVENTS.PRIVATEEVENT,
                                EVENTS.MAXIMUMCAPACITY)
                        .values(
                                publicEvent.getEventID(),
                                publicEvent.getEventName(),
                                publicEvent.getEventDateTime(),
                                publicEvent.getNumberOfBookedUsersOnEvent(),
                                publicEvent.getCategory(),
                                publicEvent.isPrivateEvent(),
                                publicEvent.getMaximumCapacity())
                        .execute();
            }

            if (rowsAffected > 0) {
                LoggerHelper.logInfoMessage(EventManager.class, EVENT_ADDED);

                return true;
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventManager.class, EVENT_NOT_ADDED + exception.getMessage());
        }

        return false;
    }

    // privates Event laden (READ) anhand der ID
    public static Optional<PrivateEvent> readPrivateEventByID(String eventID) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Record record = create.select()
                    .from(EVENTS)
                    .where(EVENTS.EVENTID.eq(eventID))
                    .fetchOne();

            if (record != null) {

                PrivateEvent privateEvent = new PrivateEvent(
                        record.get(EVENTS.EVENTID),
                        record.get(EVENTS.EVENTNAME),
                        record.get(EVENTS.EVENTDATETIME),
                        record.get((EVENTS.NUMBEROFBOOKEDUSERSONEVENT)),
                        // TODO: Rückgabe der Userliste aus Relation "booked"
                        record.get(EVENTS.CATEGORY),
                        record.get(EVENTS.PRIVATEEVENT)
                    );

                return Optional.of(privateEvent);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventManager.class, EVENT_NOT_READ + exception.getMessage());
        }

        return Optional.empty();
    }

    // öffentliches Event laden (READ) anhand der ID
    public static Optional<PublicEvent> readPublicEventByID(String eventID) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Record record = create.select()
                    .from(EVENTS)
                    .where(EVENTS.EVENTID.eq(eventID))
                    .fetchOne();

            if (record != null) {

                PublicEvent publicEvent = new PublicEvent(
                        record.get(EVENTS.EVENTID),
                        record.get(EVENTS.EVENTNAME),
                        record.get(EVENTS.EVENTDATETIME),
                        record.get((EVENTS.NUMBEROFBOOKEDUSERSONEVENT)),
                        // TODO: Rückgabe der Userliste aus Relation "booked"
                        record.get(EVENTS.CATEGORY),
                        record.get(EVENTS.PRIVATEEVENT),
                        record.get(EVENTS.MAXIMUMCAPACITY)
                );

                return Optional.of(publicEvent);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventManager.class, EVENT_NOT_READ + exception.getMessage());
        }

        return Optional.empty();
    }

    // Event ändern (UPDATE)
    public static boolean updateEvent(EventModel event) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            int rowsUpdated = 0;

            if (event.isPrivateEvent()) {
                PrivateEvent privateEvent = (PrivateEvent) event;

                rowsUpdated = create.update(EVENTS)
                        .set(EVENTS.EVENTNAME, privateEvent.getEventName())
                        .set(EVENTS.EVENTDATETIME, privateEvent.getEventDateTime())
                        .set(EVENTS.NUMBEROFBOOKEDUSERSONEVENT, privateEvent.getNumberOfBookedUsersOnEvent())
                        .set(EVENTS.CATEGORY, privateEvent.getCategory())
                        .set(EVENTS.PRIVATEEVENT, privateEvent.isPrivateEvent())
                        .execute();
            } else {
                PublicEvent publicEvent = (PublicEvent) event;

                rowsUpdated = create.update(EVENTS)
                        .set(EVENTS.EVENTNAME, publicEvent.getEventName())
                        .set(EVENTS.EVENTDATETIME, publicEvent.getEventDateTime())
                        .set(EVENTS.NUMBEROFBOOKEDUSERSONEVENT, publicEvent.getNumberOfBookedUsersOnEvent())
                        .set(EVENTS.CATEGORY, publicEvent.getCategory())
                        .set(EVENTS.PRIVATEEVENT, publicEvent.isPrivateEvent())
                        .set(EVENTS.MAXIMUMCAPACITY, publicEvent.getMaximumCapacity())
                        .execute();
            }

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

            int rowsAffected = create.deleteFrom(EVENTS)
                    .where(EVENTS.EVENTID.eq(eventID))
                    .execute();

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
