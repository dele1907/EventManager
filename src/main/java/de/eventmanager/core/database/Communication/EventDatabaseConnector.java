package de.eventmanager.core.database.Communication;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;

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

public class EventDatabaseConnector {

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
                rowsAffected = insertPrivateEvent(create, privateEvent);
            } else {
                PublicEvent publicEvent = (PublicEvent) event;
                rowsAffected = insertPublicEvent(create, publicEvent);
            }

            if (rowsAffected > 0) {
                LoggerHelper.logInfoMessage(EventDatabaseConnector.class, EVENT_ADDED);

                return true;
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_ADDED + exception.getMessage());
        }

        return false;
    }

    private static int insertPrivateEvent(DSLContext create, PrivateEvent privateEvent) {

        return create.transactionResult(configuration -> {
            DSLContext ctx = DSL.using(configuration);

            int insertedCities = ctx.insertInto(CITIES, CITIES.POSTALCODE, CITIES.CITYNAME)
                    .values(privateEvent.getPostalCode(), privateEvent.getCity())
                    .onDuplicateKeyIgnore()
                    .execute();

            int insertedEvents = ctx.insertInto(EVENTS,
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

            return insertedCities + insertedEvents;
        });
    }

    private static int insertPublicEvent(DSLContext create, PublicEvent publicEvent) {

        return create.transactionResult(configuration -> {
            DSLContext ctx = DSL.using(configuration);

            int insertedCities = ctx.insertInto(CITIES, CITIES.POSTALCODE, CITIES.CITYNAME)
                    .values(publicEvent.getPostalCode(), publicEvent.getCity())
                    .onDuplicateKeyIgnore()
                    .execute();

            int insertedEvents = ctx.insertInto(EVENTS,
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
                            EVENTS.DESCRIPTION,
                            EVENTS.MAXIMUMCAPACITY)
                    .values(
                            publicEvent.getEventID(),
                            publicEvent.getEventName(),
                            publicEvent.getEventStart(),
                            publicEvent.getEventEnd(),
                            publicEvent.getNumberOfBookedUsersOnEvent(),
                            publicEvent.getCategory(),
                            publicEvent.isPrivateEvent(),
                            publicEvent.getPostalCode(),
                            publicEvent.getAddress(),
                            publicEvent.getEventLocation(),
                            publicEvent.getDescription(),
                            publicEvent.getMaximumCapacity())
                    .execute();

            return insertedCities + insertedEvents;
        });
    }

    public static Optional<PrivateEvent> getPrivateEventFromRecord(Record record) {

        return Optional.of(new PrivateEvent(
                record.get(EVENTS.EVENTID),
                record.get(EVENTS.EVENTNAME),
                record.get(EVENTS.EVENTSTART),
                record.get(EVENTS.EVENTEND),
                record.get((EVENTS.NUMBEROFBOOKEDUSERSONEVENT)),
                BookingDatabaseConnector.getBookedUsersOnEvent(record.get(EVENTS.EVENTID)),
                record.get(EVENTS.CATEGORY),
                record.get(EVENTS.PRIVATEEVENT),
                record.get(EVENTS.POSTALCODE),
                record.get(CITIES.CITYNAME),
                record.get(EVENTS.ADDRESS),
                record.get(EVENTS.EVENTLOCATION),
                record.get(EVENTS.DESCRIPTION)
        ));
    }

    public static Optional<PublicEvent> getPublicEventFromRecord(Record record) {

        return Optional.of(new PublicEvent(
                record.get(EVENTS.EVENTID),
                record.get(EVENTS.EVENTNAME),
                record.get(EVENTS.EVENTSTART),
                record.get(EVENTS.EVENTEND),
                record.get((EVENTS.NUMBEROFBOOKEDUSERSONEVENT)),
                BookingDatabaseConnector.getBookedUsersOnEvent(record.get(EVENTS.EVENTID)),
                record.get(EVENTS.CATEGORY),
                record.get(EVENTS.PRIVATEEVENT),
                record.get(EVENTS.POSTALCODE),
                record.get(CITIES.CITYNAME),
                record.get(EVENTS.ADDRESS),
                record.get(EVENTS.EVENTLOCATION),
                record.get(EVENTS.DESCRIPTION),
                record.get(EVENTS.MAXIMUMCAPACITY)
        ));
    }

    /**
     * READ private and public events by ID
     * */
    public static Optional<? extends EventModel> readEventByID(String eventID) {
        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Record record = create.select()
                    .from(EVENTS)
                    .join(CITIES).on(EVENTS.POSTALCODE.eq(CITIES.POSTALCODE))
                    .where(EVENTS.EVENTID.eq(eventID))
                    .fetchOne();

            if (record != null) {
                return record.get(EVENTS.PRIVATEEVENT) ?
                        getPrivateEventFromRecord(record) :
                        getPublicEventFromRecord(record);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_READ + exception.getMessage());
        }

        return Optional.empty();
    }

    /**
     * READ a private event by ID
     * */
    public static Optional<PrivateEvent> readPrivateEventByID(String eventID) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Record record = create.select()
                    .from(EVENTS)
                    .join(CITIES).on(EVENTS.POSTALCODE.eq(CITIES.POSTALCODE))
                    .where(EVENTS.EVENTID.eq(eventID))
                    .fetchOne();

            if (record != null) {
                return getPrivateEventFromRecord(record);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_READ + exception.getMessage());
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
                    .join(CITIES).on(EVENTS.POSTALCODE.eq(CITIES.POSTALCODE))
                    .where(EVENTS.EVENTID.eq(eventID))
                    .fetchOne();

            if (record != null) {
                return getPublicEventFromRecord(record);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_READ + exception.getMessage());
        }

        return Optional.empty();
    }

    //# region eventSearch

    /**
     * READ a public event by name
     * */
    public static ArrayList<PublicEvent> readPublicEventsByName(String eventName) {
        ArrayList <PublicEvent> publicEvents = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record> records = create.select()
                    .from(EVENTS)
                    .join(CITIES).on(EVENTS.POSTALCODE.eq(CITIES.POSTALCODE))
                    .where(EVENTS.EVENTNAME.eq(eventName))
                    .fetch();

            readPublicEventsFromDatabase(publicEvents, records);

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_READ + exception.getMessage());
        }

        return publicEvents;
    }

    /**
    * READ a public event by location
    * */
    public static ArrayList<PublicEvent> readPublicEventsByLocation(String eventLocation) {
        ArrayList<PublicEvent> publicEvents = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record> records = create.select()
                    .from(EVENTS)
                    .join(CITIES).on(EVENTS.POSTALCODE.eq(CITIES.POSTALCODE))
                    .where(EVENTS.EVENTLOCATION.eq(eventLocation))
                    .fetch();

            readPublicEventsFromDatabase(publicEvents, records);

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_READ + exception.getMessage());
        }

        return publicEvents;
    }

    /**
     * READ a public event by city
     * */
    public static ArrayList<PublicEvent>readPublicEventByCity(String eventCity) {
        ArrayList<PublicEvent> publicEvents = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record> records = create.select()
                    .from(EVENTS)
                    .join(CITIES).on(EVENTS.POSTALCODE.eq(CITIES.POSTALCODE))
                    .where(CITIES.CITYNAME.eq(eventCity))
                    .fetch();

            readPublicEventsFromDatabase(publicEvents, records);

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_READ + exception.getMessage());
        }

        return publicEvents;
    }

    /**
     * READ a list of public events
     * */
    private static void readPublicEventsFromDatabase(ArrayList<PublicEvent> publicEvents, Result<Record> records) {
        for (var record : records) {
            PublicEvent publicEvent = new PublicEvent(
                    record.get(EVENTS.EVENTID),
                    record.get(EVENTS.EVENTNAME),
                    record.get(EVENTS.EVENTSTART),
                    record.get(EVENTS.EVENTEND),
                    record.get((EVENTS.NUMBEROFBOOKEDUSERSONEVENT)),
                    BookingDatabaseConnector.getBookedUsersOnEvent(record.get(EVENTS.EVENTID)),
                    record.get(EVENTS.CATEGORY),
                    record.get(EVENTS.PRIVATEEVENT),
                    record.get(EVENTS.POSTALCODE),
                    record.get(CITIES.CITYNAME),
                    record.get(EVENTS.ADDRESS),
                    record.get(EVENTS.EVENTLOCATION),
                    record.get(EVENTS.DESCRIPTION),
                    record.get(EVENTS.MAXIMUMCAPACITY)
            );

            publicEvents.add(publicEvent);
        }
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
                rowsUpdated = setPrivateEvent(create, privateEvent);
            } else {
                PublicEvent publicEvent = (PublicEvent) event;
                rowsUpdated = setPublicEvent(create, publicEvent);
            }

            if (rowsUpdated > 0) {
                LoggerHelper.logInfoMessage(EventDatabaseConnector.class, EVENT_UPDATED);

                return true;
            } else {
                LoggerHelper.logInfoMessage(EventDatabaseConnector.class, EVENT_NOT_FOUND);

                return false;
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_UPDATED + exception.getMessage());

            return false;
        }
    }

    private static int setPrivateEvent(DSLContext create, PrivateEvent privateEvent) {

        return create.transactionResult(configuration -> {
            DSLContext ctx = DSL.using(configuration);

            boolean postalCodeExists = ctx.fetchCount(ctx.selectFrom(CITIES)
                    .where(CITIES.POSTALCODE.eq(privateEvent.getPostalCode()))) > 0;

            if (!postalCodeExists) {
                ctx.insertInto(CITIES, CITIES.CITYNAME, CITIES.POSTALCODE)
                        .values(privateEvent.getCity(), privateEvent.getPostalCode())
                        .execute();
            }

            int updatedEvents = ctx.update(EVENTS)
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
                    .where(EVENTS.EVENTID.eq(privateEvent.getEventID()))
                    .execute();

            return updatedEvents;
        });
    }

    private static int setPublicEvent(DSLContext create, PublicEvent publicEvent) {

        return create.transactionResult(configuration -> {
            DSLContext ctx = DSL.using(configuration);

            boolean postalCodeExists = ctx.fetchCount(ctx.selectFrom(CITIES)
                    .where(CITIES.POSTALCODE.eq(publicEvent.getPostalCode()))) > 0;

            if (!postalCodeExists) {
                ctx.insertInto(CITIES, CITIES.CITYNAME, CITIES.POSTALCODE)
                        .values(publicEvent.getCity(), publicEvent.getPostalCode())
                        .execute();
            }

            int updatedEvents = ctx.update(EVENTS)
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
                    .where(EVENTS.EVENTID.eq(publicEvent.getEventID()))
                    .execute();

            return updatedEvents;
        });
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
                LoggerHelper.logInfoMessage(EventDatabaseConnector.class, EVENT_DELETED);
            } else {
                LoggerHelper.logInfoMessage(EventDatabaseConnector.class, EVENT_NOT_FOUND);
            }

            return rowsAffected > 0;

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_DELETED + exception.getMessage());

            return false;
        }
    }

    //#endregion CRUD operations

}
