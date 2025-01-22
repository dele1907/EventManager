package de.eventmanager.core.events.Management;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.eventmanager.core.database.Communication.DatabaseConnector;
import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import helper.LoggerHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

import static org.jooq.generated.Tables.CITIES;
import static org.jooq.generated.tables.Events.EVENTS;
import static org.jooq.generated.tables.Created.CREATED;

public class EventManager {

    //#region constants

    private static final String EVENT_ADDED = "Event added successfully";
    private static final String EVENT_NOT_ADDED = "Error adding event: ";
    private static final String EVENT_NOT_READ = "Error reading event: ";
    private static final String EVENT_UPDATED = "Event updated successfully";
    private static final String EVENT_NOT_UPDATED = "Error updating event: ";
    private static final String EVENT_NOT_FOUND = "No event found with the given ID";
    private static final String EVENT_DELETED = "Event deleted successfully";
    private static final String EVENT_NOT_DELETED = "Error deleting event: ";

    //#endregion constants

    //#region CRUD operations

    /**
     * CREATE a new event
     * */
    public static boolean createNewEvent(EventModel event) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            int rowsAffected = 0;

            if (event.isPrivateEvent()) {
                PrivateEvent privateEvent = (PrivateEvent) event;

                rowsAffected = create.insertInto(EVENTS,
                                EVENTS.EVENTID,
                                EVENTS.EVENTNAME,
                                EVENTS.EVENTSTART,
                                EVENTS.EVENTEND,
                                EVENTS.NUMBEROFBOOKEDUSERSONEVENT,
                                EVENTS.CATEGORY,
                                EVENTS.PRIVATEEVENT,
                                EVENTS.POSTALCODE,
                                EVENTS.ADDRESS,
                                EVENTS.EVENTLOCATION,
                                EVENTS.DESCRIPTION)
                        .values(
                                privateEvent.getEventID(),
                                privateEvent.getEventName(),
                                privateEvent.getEventStart(),
                                privateEvent.getEventEnd(),
                                privateEvent.getNumberOfBookedUsersOnEvent(),
                                privateEvent.getCategory(),
                                privateEvent.isPrivateEvent(),
                                privateEvent.getPostalCode(),
                                privateEvent.getAddress(),
                                privateEvent.getEventLocation(),
                                privateEvent.getDescription())
                        .execute();
            } else {
                PublicEvent publicEvent = (PublicEvent) event;

                rowsAffected = create.insertInto(EVENTS,
                                EVENTS.EVENTID,
                                EVENTS.EVENTNAME,
                                EVENTS.EVENTSTART,
                                EVENTS.EVENTEND,
                                EVENTS.NUMBEROFBOOKEDUSERSONEVENT,
                                EVENTS.CATEGORY,
                                EVENTS.PRIVATEEVENT,
                                EVENTS.MAXIMUMCAPACITY,
                                EVENTS.POSTALCODE,
                                EVENTS.ADDRESS,
                                EVENTS.EVENTLOCATION,
                                EVENTS.DESCRIPTION)
                        .values(
                                publicEvent.getEventID(),
                                publicEvent.getEventName(),
                                publicEvent.getEventStart(),
                                publicEvent.getEventEnd(),
                                publicEvent.getNumberOfBookedUsersOnEvent(),
                                publicEvent.getCategory(),
                                publicEvent.isPrivateEvent(),
                                publicEvent.getMaximumCapacity(),
                                publicEvent.getPostalCode(),
                                publicEvent.getAddress(),
                                publicEvent.getEventLocation(),
                                publicEvent.getDescription())
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

    /**
     * READ a private event by ID
     * */
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
                        record.get(EVENTS.EVENTSTART),
                        record.get(EVENTS.EVENTEND),
                        record.get((EVENTS.NUMBEROFBOOKEDUSERSONEVENT)),
                        // TODO: Rückgabe der Userliste aus Relation "booked"
                        record.get(EVENTS.CATEGORY),
                        record.get(EVENTS.PRIVATEEVENT),
                        record.get(EVENTS.POSTALCODE),
                        record.get(EVENTS.ADDRESS),
                        record.get(EVENTS.EVENTLOCATION),
                        record.get(EVENTS.DESCRIPTION)
                    );

                return Optional.of(privateEvent);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventManager.class, EVENT_NOT_READ + exception.getMessage());
        }

        return Optional.empty();
    }

    /**
     * READ a public event by ID
     * */
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
                        record.get(EVENTS.EVENTSTART),
                        record.get(EVENTS.EVENTEND),
                        record.get((EVENTS.NUMBEROFBOOKEDUSERSONEVENT)),
                        // TODO: Rückgabe der Userliste aus Relation "booked"
                        record.get(EVENTS.CATEGORY),
                        record.get(EVENTS.PRIVATEEVENT),
                        record.get(EVENTS.POSTALCODE),
                        record.get(EVENTS.ADDRESS),
                        record.get(EVENTS.EVENTLOCATION),
                        record.get(EVENTS.DESCRIPTION),
                        record.get(EVENTS.MAXIMUMCAPACITY)
                );

                return Optional.of(publicEvent);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventManager.class, EVENT_NOT_READ + exception.getMessage());
        }

        return Optional.empty();
    }

    //# region eventSearch

    /**
     * READ a public event by name
     * */
    public static List<PublicEvent> readPublicEventsByName(String eventName) {
        List <PublicEvent> publicEvents = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record> records= create.select()
                    .from(EVENTS)
                    .where(EVENTS.EVENTNAME.eq(eventName))
                    .fetch();

            for (Record record : records) {
                PublicEvent publicEvent = new PublicEvent(
                        record.get(EVENTS.EVENTID),
                        record.get(EVENTS.EVENTNAME),
                        record.get(EVENTS.EVENTSTART),
                        record.get(EVENTS.EVENTEND),
                        record.get((EVENTS.NUMBEROFBOOKEDUSERSONEVENT)),
                        // TODO: Rückgabe der Userliste aus Relation "booked"
                        record.get(EVENTS.CATEGORY),
                        record.get(EVENTS.PRIVATEEVENT),
                        record.get(EVENTS.POSTALCODE),
                        record.get(EVENTS.ADDRESS),
                        record.get(EVENTS.EVENTLOCATION),
                        record.get(EVENTS.DESCRIPTION),
                        record.get(EVENTS.MAXIMUMCAPACITY)
                );

                publicEvents.add(publicEvent);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventManager.class, EVENT_NOT_READ + exception.getMessage());
        }

        return publicEvents;
    }

    /**
    * READ a public event by location
    * */
    public static List<PublicEvent> readPublicEventsByLocation(String eventLocation) {
        List<PublicEvent> publicEvents = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {
            DSLContext create = DSL.using(connection);


            Result<Record> records = create.select()
                    .from(EVENTS)
                    .where(EVENTS.EVENTLOCATION.eq(eventLocation))
                    .fetch();


            for (Record record : records) {
                PublicEvent publicEvent = new PublicEvent(
                        record.get(EVENTS.EVENTID),
                        record.get(EVENTS.EVENTNAME),
                        record.get(EVENTS.EVENTSTART),
                        record.get(EVENTS.EVENTEND),
                        record.get((EVENTS.NUMBEROFBOOKEDUSERSONEVENT)),
                        // TODO: Rückgabe der Userliste aus Relation "booked"
                        record.get(EVENTS.CATEGORY),
                        record.get(EVENTS.PRIVATEEVENT),
                        record.get(EVENTS.POSTALCODE),
                        record.get(EVENTS.ADDRESS),
                        record.get(EVENTS.EVENTLOCATION),
                        record.get(EVENTS.DESCRIPTION),
                        record.get(EVENTS.MAXIMUMCAPACITY)
                );
                publicEvents.add(publicEvent);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventManager.class, EVENT_NOT_READ + exception.getMessage());
        }

        return publicEvents;
    }

    /**
     * READ a public event by city
     * */
    public static List<PublicEvent>readPublicEventByCity(String eventCity) {
        List<PublicEvent> publicEvents = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record> records = create.select()
                    .from(EVENTS)
                    .join(CITIES).on(EVENTS.POSTALCODE.eq(CITIES.POSTALCODE))
                    .where(CITIES.CITYNAME.eq(eventCity))
                    .fetch();

            for (Record record : records) {
                PublicEvent publicEvent = new PublicEvent(
                        record.get(EVENTS.EVENTID),
                        record.get(EVENTS.EVENTNAME),
                        record.get(EVENTS.EVENTSTART),
                        record.get(EVENTS.EVENTEND),
                        record.get((EVENTS.NUMBEROFBOOKEDUSERSONEVENT)),
                        // TODO: Rückgabe der Userliste aus Relation "booked"
                        record.get(EVENTS.CATEGORY),
                        record.get(EVENTS.PRIVATEEVENT),
                        record.get(EVENTS.POSTALCODE),
                        record.get(EVENTS.ADDRESS),
                        record.get(EVENTS.EVENTLOCATION),
                        record.get(EVENTS.DESCRIPTION),
                        record.get(EVENTS.MAXIMUMCAPACITY)
                );
                publicEvents.add(publicEvent);
        }

    }catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventManager.class, EVENT_NOT_READ + exception.getMessage());
        }

        return publicEvents;
    }

    //# endregion eventSearch

    /**
     * UPDATE an event
     * */
    public static boolean updateEvent(EventModel event) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            int rowsUpdated = 0;

            if (event.isPrivateEvent()) {
                PrivateEvent privateEvent = (PrivateEvent) event;

                rowsUpdated = create.update(EVENTS)
                        .set(EVENTS.EVENTNAME, privateEvent.getEventName())
                        .set(EVENTS.EVENTSTART, privateEvent.getEventStart())
                        .set(EVENTS.EVENTEND, privateEvent.getEventEnd())
                        .set(EVENTS.NUMBEROFBOOKEDUSERSONEVENT, privateEvent.getNumberOfBookedUsersOnEvent())
                        .set(EVENTS.CATEGORY, privateEvent.getCategory())
                        .set(EVENTS.PRIVATEEVENT, privateEvent.isPrivateEvent())
                        .set(EVENTS.POSTALCODE, privateEvent.getPostalCode())
                        .set(EVENTS.ADDRESS, privateEvent.getAddress())
                        .set(EVENTS.EVENTLOCATION, privateEvent.getEventLocation())
                        .set(EVENTS.DESCRIPTION, privateEvent.getDescription())
                        .execute();
            } else {
                PublicEvent publicEvent = (PublicEvent) event;

                rowsUpdated = create.update(EVENTS)
                        .set(EVENTS.EVENTNAME, publicEvent.getEventName())
                        .set(EVENTS.EVENTSTART, publicEvent.getEventStart())
                        .set(EVENTS.EVENTEND, publicEvent.getEventEnd())
                        .set(EVENTS.NUMBEROFBOOKEDUSERSONEVENT, publicEvent.getNumberOfBookedUsersOnEvent())
                        .set(EVENTS.CATEGORY, publicEvent.getCategory())
                        .set(EVENTS.PRIVATEEVENT, publicEvent.isPrivateEvent())
                        .set(EVENTS.POSTALCODE, publicEvent.getPostalCode())
                        .set(EVENTS.ADDRESS, publicEvent.getAddress())
                        .set(EVENTS.EVENTLOCATION, publicEvent.getEventLocation())
                        .set(EVENTS.DESCRIPTION, publicEvent.getDescription())
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

    /**
     * DELETE an event
     * */
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

    //#endregion CRUD operations

    //#region createdByUser

    /**
     * Relate a user to an event as the creator
     * */
    public static boolean addUserCreatedEvent(String eventID, String userID) {
        try (Connection connection = DatabaseConnector.connect()) {
            DSLContext create = DSL.using(connection);

            int rowsAffected = create.insertInto(CREATED, CREATED.EVENTID, CREATED.USERID)
                    .values(eventID, userID)
                    .execute();

            return rowsAffected > 0;

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventManager.class, "Error adding user created event: " +
                    exception.getMessage());

            return false;
        }
    }

    /**
     * Unlink a user from an event as the creator
     * */
        public static boolean deleteUserCreatedEvent(String eventID, String userID) {
            try (Connection connection = DatabaseConnector.connect()) {
                DSLContext create = DSL.using(connection);

                int rowsAffected = create.deleteFrom(CREATED)
                        .where(CREATED.EVENTID.eq(eventID))
                        .and(CREATED.USERID.eq(userID))
                        .execute();

                return rowsAffected > 0;

            } catch (Exception exception) {
                LoggerHelper.logErrorMessage(EventManager.class, "Error deleting user created event: " +
                        exception.getMessage());

                return false;
            }
    }

    //#endregion createdByUser

}
