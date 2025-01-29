package de.eventmanager.core.database.Communication;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.users.User;
import helper.LoggerHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

import static org.jooq.generated.Tables.CITIES;
import static org.jooq.generated.tables.Events.EVENTS;
import static org.jooq.generated.tables.Created.CREATED;
import static org.jooq.generated.tables.Booked.BOOKED;
import static org.jooq.generated.tables.User.USER;

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
    private static final String EVENT_CREATOR_ASSIGNED = "User assigned as event creator successfully";
    private static final String EVENT_CREATOR_NOT_ASSIGNED = "Error assigning user as event creator: ";
    private static final String EVENT_CREATOR_REMOVED = "User removed as event creator successfully";
    private static final String EVENT_CREATOR_NOT_REMOVED = "Error removing user as event creator: ";
    private static final String CHECK_IF_CREATOR_FAILED = "Checking if user is event creator failed: ";
    private static final String EVENT_CREATOR_NOT_FOUND = "Error finding event creator: ";
    private static final String ID_NOT_FOUND = "Error finding UserID or EventID";
    private static final String BOOKING_ADDED = "Booking added successfully";
    private static final String BOOKING_NOT_ADDED = "Error adding booking: ";
    private static final String BOOKING_REMOVED = "Booking removed successfully";
    private static final String BOOKING_NOT_REMOVED = "Error removing booking: ";
    private static final String NO_USER_LIST_AVAILABLE = "Error getting a list of booked users: ";

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
        return create.insertInto(EVENTS,
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
    }

    private static int insertPublicEvent(DSLContext create, PublicEvent publicEvent) {
        return create.insertInto(EVENTS,
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

    public static Optional<PrivateEvent> getPrivateEventFromRecord(Record record) {
        return Optional.of(new PrivateEvent(
                record.get(EVENTS.EVENTID),
                record.get(EVENTS.EVENTNAME),
                record.get(EVENTS.EVENTSTART),
                record.get(EVENTS.EVENTEND),
                record.get((EVENTS.NUMBEROFBOOKEDUSERSONEVENT)),
                getBookedUsersOnEvent(record.get(EVENTS.EVENTID)),
                record.get(EVENTS.CATEGORY),
                record.get(EVENTS.PRIVATEEVENT),
                record.get(EVENTS.POSTALCODE),
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
                getBookedUsersOnEvent(record.get(EVENTS.EVENTID)),
                record.get(EVENTS.CATEGORY),
                record.get(EVENTS.PRIVATEEVENT),
                record.get(EVENTS.POSTALCODE),
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
                    getBookedUsersOnEvent(record.get(EVENTS.EVENTID)),
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
        return create.update(EVENTS)
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
    }

    private static int setPublicEvent(DSLContext create, PublicEvent publicEvent) {
        return create.update(EVENTS)
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

    //#region createdByUser

    /**
     * Assign a user to an event as the creator
     * */
    public static boolean assignUserAsEventCreator(String eventID, String userID) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            int rowsAffected = create.insertInto(CREATED, CREATED.EVENTID, CREATED.USERID)
                    .values(eventID, userID)
                    .execute();

            if (rowsAffected > 0) {
                LoggerHelper.logInfoMessage(EventDatabaseConnector.class, EVENT_CREATOR_ASSIGNED);
            }
            else {
                LoggerHelper.logInfoMessage(EventDatabaseConnector.class, ID_NOT_FOUND);
            }

            return rowsAffected > 0;

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_CREATOR_NOT_ASSIGNED +
                    exception.getMessage());

            return false;
        }
    }

    /**
     * Unlink a user from an event as the creator
     * */
        public static boolean removeUserAsEventCreator(String eventID, String userID) {

            try (Connection connection = DatabaseConnector.connect()) {

                DSLContext create = DSL.using(connection);

                int rowsAffected = create.deleteFrom(CREATED)
                        .where(CREATED.EVENTID.eq(eventID))
                        .and(CREATED.USERID.eq(userID))
                        .execute();

                if (rowsAffected > 0) {
                    LoggerHelper.logInfoMessage(EventDatabaseConnector.class, EVENT_CREATOR_REMOVED);
                }
                else {
                    LoggerHelper.logInfoMessage(EventDatabaseConnector.class, ID_NOT_FOUND);
                }

                return rowsAffected > 0;

            } catch (Exception exception) {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_CREATOR_NOT_REMOVED +
                        exception.getMessage());

                return false;
            }
        }

    /**
     * Check if a user is the creator of an event
     * */
        public static boolean checkIfUserIsEventCreator(String eventID, String userID) {

            try (Connection connection = DatabaseConnector.connect()){

                DSLContext create = DSL.using(connection);

                Record record = create.select()
                        .from(CREATED)
                        .where(CREATED.USERID.eq(userID))
                        .and(CREATED.EVENTID.eq(eventID))
                        .fetchOne();

                if (record == null) {
                    LoggerHelper.logInfoMessage(EventDatabaseConnector.class, ID_NOT_FOUND);

                    return false;
                }

                return true;
            } catch (Exception exception) {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CHECK_IF_CREATOR_FAILED);

                return false;
            }
        }

    /**
     * Get the user who is the event creator
     * */
        public static Optional<User> getEventCreator(String eventID) {

            try (Connection connection = DatabaseConnector.connect()) {

                DSLContext create = DSL.using(connection);

                Record record = create.select()
                        .from(USER)
                        .join(CREATED).on(USER.USERID.eq(CREATED.USERID))
                        .where(CREATED.EVENTID.eq(eventID))
                        .fetchOne();

                if (record != null) {

                    User user = new User(
                            record.get(USER.USERID),
                            record.get(USER.FIRSTNAME),
                            record.get(USER.LASTNAME),
                            record.get(USER.BIRTHDATE),
                            record.get(USER.EMAIL),
                            record.get(USER.PASSWORD),
                            record.get(USER.PHONENUMBER),
                            record.get(USER.ISADMIN)
                    );

                    return Optional.of(user);
                }

            } catch (Exception exception) {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, EVENT_CREATOR_NOT_FOUND + exception.getMessage());
            }

            return Optional.empty();
        }

    //#endregion createdByUser

    //#region booking

    /**
     * Relate an event to a user as booked
     * */
    public static boolean addBooking(String eventID, String userID) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            return create.transactionResult(configuration -> {

                DSLContext ctx = DSL.using(configuration);

                int rowsAffected = ctx.insertInto(BOOKED, BOOKED.EVENTID, BOOKED.USERID)
                        .values(eventID, userID)
                        .execute();

                if (rowsAffected > 0) {
                    int updatedRows = ctx.update(EVENTS)
                            .set(EVENTS.NUMBEROFBOOKEDUSERSONEVENT, EVENTS.NUMBEROFBOOKEDUSERSONEVENT.plus(1))
                            .where(EVENTS.EVENTID.eq(eventID))
                            .execute();

                    LoggerHelper.logInfoMessage(EventDatabaseConnector.class, BOOKING_ADDED);

                    return updatedRows > 0;
                }

                return false;
            });

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, BOOKING_NOT_ADDED +
                    exception.getMessage());

            return false;
        }
    }

    /**
     * Unlink an event from a user as booked
     * */
    public static boolean removeBooking(String eventID, String userID) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            return create.transactionResult(configuration -> {
                DSLContext ctx = DSL.using(configuration);

                int rowsAffected = ctx.deleteFrom(BOOKED)
                        .where(BOOKED.EVENTID.eq(eventID))
                        .and(BOOKED.USERID.eq(userID))
                        .execute();

                if (rowsAffected > 0) {
                    int updatedRows = ctx.update(EVENTS)
                            .set(EVENTS.NUMBEROFBOOKEDUSERSONEVENT, EVENTS.NUMBEROFBOOKEDUSERSONEVENT.minus(1))
                            .where(EVENTS.EVENTID.eq(eventID))
                            .execute();

                    LoggerHelper.logInfoMessage(EventDatabaseConnector.class, BOOKING_REMOVED);

                    return updatedRows > 0;
                }

                return false;
            });

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, BOOKING_NOT_REMOVED +
                    exception.getMessage());

            return false;
        }
    }

    /**
     * Return a list of user email addresses
     * */
    public static ArrayList<String> getBookedUsersOnEvent(String eventID) {
        ArrayList<String> bookedUsers = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record> records = create.select()
                    .from(USER)
                    .join(BOOKED).on(USER.USERID.eq(BOOKED.USERID))
                    .where(BOOKED.EVENTID.eq(eventID))
                    .fetch();

            bookedUsers.addAll(records.stream()
                    .map(record -> record.get(USER.EMAIL))
                    .collect(Collectors.toList()));

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(UserDatabaseConnector.class, NO_USER_LIST_AVAILABLE + exception.getMessage());
        }

        return bookedUsers;
    }

    //#endregion booking

}
