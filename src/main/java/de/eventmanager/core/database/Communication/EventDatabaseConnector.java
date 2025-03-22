package de.eventmanager.core.database.Communication;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import helper.LoggerHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

import static org.jooq.generated.Tables.*;
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
    public static boolean createNewEvent(EventModel event, String userID) {

        try (Connection connection = DatabaseConnector.connect()) {

            assert connection != null;
            DSLContext create = DSL.using(connection);
            connection.setAutoCommit(false);

            int rowsAffected = event.isPrivateEvent()
                    ? insertPrivateEvent(create, (PrivateEvent) event)
                    : insertPublicEvent(create, (PublicEvent) event);

            if (rowsAffected > 0) {
                boolean creatorAssigned = CreatorDatabaseConnector.assignUserAsEventCreator(event.getEventID(), userID, create);

                if (creatorAssigned) {
                    connection.commit();
                    LoggerHelper.logInfoMessage(EventDatabaseConnector.class, EVENT_ADDED);
                    return true;
                }
            }
            connection.rollback();

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_ADDED + exception.getMessage());
        }

        return false;
    }

    public static String getCityNameByPostalCode(String postalCode) {
        try (var connection = DatabaseConnector.connect()) {

            var create = DSL.using(connection);

            var record = create.select()
                    .from(CITIES)
                    .where(CITIES.POSTALCODE.eq(postalCode))
                    .fetchOne();

            if (record != null) {
                return record.get(CITIES.CITYNAME);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_READ + exception.getMessage());
        }

        return "";
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
                            EVENTS.MAXIMUMCAPACITY,
                            EVENTS.MINIMUMAGE)
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
                            publicEvent.getMaximumCapacity(),
                            publicEvent.getMinimumAge())
                    .execute();

            return insertedCities + insertedEvents;
        });
    }

    private static Optional<PrivateEvent> getPrivateEventFromRecord(Record record) {

        return Optional.of(new PrivateEvent(
                record.get(EVENTS.EVENTID),
                record.get(EVENTS.EVENTNAME),
                record.get(EVENTS.EVENTSTART),
                record.get(EVENTS.EVENTEND),
                record.get((EVENTS.NUMBEROFBOOKEDUSERSONEVENT)),
                BookingDatabaseConnector.getBookedUsersInformationOnEvent(record.get(EVENTS.EVENTID)),
                record.get(EVENTS.CATEGORY),
                record.get(EVENTS.PRIVATEEVENT),
                record.get(EVENTS.POSTALCODE),
                record.get(CITIES.CITYNAME),
                record.get(EVENTS.ADDRESS),
                record.get(EVENTS.EVENTLOCATION),
                record.get(EVENTS.DESCRIPTION)
        ));
    }

    private static Optional<PublicEvent> getPublicEventFromRecord(Record record) {

        return Optional.of(new PublicEvent(
                record.get(EVENTS.EVENTID),
                record.get(EVENTS.EVENTNAME),
                record.get(EVENTS.EVENTSTART),
                record.get(EVENTS.EVENTEND),
                record.get((EVENTS.NUMBEROFBOOKEDUSERSONEVENT)),
                BookingDatabaseConnector.getBookedUsersInformationOnEvent(record.get(EVENTS.EVENTID)),
                record.get(EVENTS.CATEGORY),
                record.get(EVENTS.PRIVATEEVENT),
                record.get(EVENTS.POSTALCODE),
                record.get(CITIES.CITYNAME),
                record.get(EVENTS.ADDRESS),
                record.get(EVENTS.EVENTLOCATION),
                record.get(EVENTS.DESCRIPTION),
                record.get(EVENTS.MAXIMUMCAPACITY),
                record.get(EVENTS.MINIMUMAGE)
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

    public static List<PublicEvent> getAllPublicEventsUserHasNotBookedAlready(String userID) {
        var publicEvents = new ArrayList<PublicEvent>();

        try (var connection = DatabaseConnector.connect()) {
            var create = DSL.using(connection);

            var records = create.select()
                    .from(EVENTS)
                    .join(CITIES).on(EVENTS.POSTALCODE.eq(CITIES.POSTALCODE))
                    .where(EVENTS.PRIVATEEVENT.eq(false))
                    .andNotExists(create.selectOne()
                        .from(BOOKED)
                        .where(BOOKED.EVENTID.eq(EVENTS.EVENTID))
                        .and(BOOKED.USERID.eq(userID))
                    )
                    .fetch();

            records.forEach(record -> {
                PublicEvent publicEvent = getPublicEventFromRecord(record).orElse(null);

                if (publicEvent != null) {
                    publicEvents.add(publicEvent);
                }
            });
        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_READ + exception.getMessage());
        }

        return publicEvents;
    }

    //# region eventSearch

    /**
     * READ a list of public events by name
     * */
    public static ArrayList<PublicEvent> readPublicEventsByName(String eventName) {
        ArrayList <PublicEvent> publicEvents = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record> records = create.select()
                    .from(EVENTS)
                    .join(CITIES).on(EVENTS.POSTALCODE.eq(CITIES.POSTALCODE))
                    .where(EVENTS.EVENTNAME.eq(eventName))
                    .and(EVENTS.PRIVATEEVENT.eq(false))
                    .fetch();

            readPublicEventsFromDatabase(publicEvents, records);

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_READ + exception.getMessage());
        }

        return publicEvents;
    }

    /**
    * READ a list of public events by location
    * */
    public static ArrayList<PublicEvent> readPublicEventsByLocation(String eventLocation) {
        ArrayList<PublicEvent> publicEvents = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record> records = create.select()
                    .from(EVENTS)
                    .join(CITIES).on(EVENTS.POSTALCODE.eq(CITIES.POSTALCODE))
                    .where(EVENTS.EVENTLOCATION.eq(eventLocation))
                    .and(EVENTS.PRIVATEEVENT.eq(false))
                    .fetch();

            readPublicEventsFromDatabase(publicEvents, records);

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_READ + exception.getMessage());
        }

        return publicEvents;
    }

    /**
     * READ a list of public events by city
     * */
    public static ArrayList<PublicEvent>readPublicEventByCity(String eventCity) {
        ArrayList<PublicEvent> publicEvents = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record> records = create.select()
                    .from(EVENTS)
                    .join(CITIES).on(EVENTS.POSTALCODE.eq(CITIES.POSTALCODE))
                    .where(CITIES.CITYNAME.eq(eventCity))
                    .and(EVENTS.PRIVATEEVENT.eq(false))
                    .fetch();

            readPublicEventsFromDatabase(publicEvents, records);

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_READ + exception.getMessage());
        }

        return publicEvents;
    }

    public static List<EventModel> getUsersBookedEventsByUserID(String userID) {
        var events = new ArrayList<EventModel>();

        try (var connection = DatabaseConnector.connect()) {
            var create = DSL.using(connection);

            var records = create.select()
                    .from(EVENTS)
                    .join(BOOKED).on(EVENTS.EVENTID.eq(BOOKED.EVENTID))
                    .join(CITIES).on(EVENTS.POSTALCODE.eq(CITIES.POSTALCODE))
                    .where(BOOKED.USERID.eq(userID))
                    .fetch();

            for (var record : records) {
                var event = record.get(EVENTS.PRIVATEEVENT) ?
                        getPrivateEventFromRecord(record).orElse(null) :
                        getPublicEventFromRecord(record).orElse(null);

                if (event != null) {
                    events.add(event);
                }
            }
        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_READ + exception.getMessage());
        }

        return events;
    }

    /**
     * READ a list of public events
     * */
    private static void readPublicEventsFromDatabase(ArrayList<PublicEvent> publicEvents, Result<Record> records) {
        for (var record : records) {
            var publicEvent = getPublicEventFromRecord(record);

            if (publicEvent.isPresent()) {
                publicEvents.add(publicEvent.get());
            }
        }
    }

    public static List<EventModel> getEventsByCreatorID(String creatorID) {
        List<EventModel> events = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {
            DSLContext create = DSL.using(connection);

            Result<Record> records = create.select()
                    .from(EVENTS)
                    .join(CREATED).on(EVENTS.EVENTID.eq(CREATED.EVENTID))
                    .join(CITIES).on(EVENTS.POSTALCODE.eq(CITIES.POSTALCODE))
                    .where(CREATED.USERID.eq(creatorID))
                    .fetch();

            for (Record record : records) {
                EventModel event = record.get(EVENTS.PRIVATEEVENT) ?
                        getPrivateEventFromRecord(record).orElse(null) :
                        getPublicEventFromRecord(record).orElse(null);

                if (event != null) {
                    events.add(event);
                }
            }
        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_READ + exception.getMessage());
        }

        return events;
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
        return setEvent(create, privateEvent);
    }

    private static int setPublicEvent(DSLContext create, PublicEvent publicEvent) {
        return setEvent(create, publicEvent);
    }

    private static int setEvent(DSLContext create, EventModel event) {
        return create.transactionResult(configuration -> {
            DSLContext ctx = DSL.using(configuration);

            boolean postalCodeExists = ctx.fetchCount(ctx.selectFrom(CITIES)
                    .where(CITIES.POSTALCODE.eq(event.getPostalCode()))) > 0;

            if (!postalCodeExists) {
                ctx.insertInto(CITIES, CITIES.CITYNAME, CITIES.POSTALCODE)
                        .values(event.getCity(), event.getPostalCode())
                        .execute();
            }

            var updateQuery = ctx.update(EVENTS)
                    .set(EVENTS.EVENTNAME, event.getEventName())
                    .set(EVENTS.EVENTSTART, event.getEventStart())
                    .set(EVENTS.EVENTEND, event.getEventEnd())
                    .set(EVENTS.NUMBEROFBOOKEDUSERSONEVENT, event.getNumberOfBookedUsersOnEvent())
                    .set(EVENTS.CATEGORY, event.getCategory())
                    .set(EVENTS.POSTALCODE, event.getPostalCode())
                    .set(EVENTS.ADDRESS, event.getAddress())
                    .set(EVENTS.EVENTLOCATION, event.getEventLocation())
                    .set(EVENTS.DESCRIPTION, event.getDescription());

            if (event instanceof PublicEvent) {
                updateQuery.set(EVENTS.MAXIMUMCAPACITY, ((PublicEvent) event).getMaximumCapacity());
            }

            int updatedEvents = updateQuery
                    .where(EVENTS.EVENTID.eq(event.getEventID()))
                    .execute();

            return updatedEvents;
        });
    }

    /**
     * DELETE an event
     * */
    public static boolean deleteEventByID(String eventID, String creatorID) {

        try (Connection connection = DatabaseConnector.connect()) {

            assert connection != null;
            DSLContext create = DSL.using(connection);
            connection.setAutoCommit(false);

            int rowsAffected = create.deleteFrom(EVENTS)
                    .where(EVENTS.EVENTID.eq(eventID))
                    .execute();

            if (rowsAffected > 0) {
                boolean creatorRemoved = CreatorDatabaseConnector.removeUserAsEventCreator(eventID, creatorID, create);

                if (creatorRemoved) {
                    connection.commit();
                    LoggerHelper.logInfoMessage(EventDatabaseConnector.class, EVENT_DELETED);
                    return true;
                } else {
                    LoggerHelper.logInfoMessage(EventDatabaseConnector.class, EVENT_NOT_FOUND);
                }
            }
            connection.rollback();

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_NOT_DELETED + exception.getMessage());
        }

        return false;
    }

    //#endregion CRUD operations

}
