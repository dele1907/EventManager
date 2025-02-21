package de.eventmanager.core.database.Communication;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.users.User;
import helper.LoggerHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

import static org.jooq.generated.tables.Events.EVENTS;
import static org.jooq.generated.tables.Booked.BOOKED;
import static org.jooq.generated.tables.User.USER;

public class BookingDatabaseConnector {

    //#region constants

    private static final String BOOKING_ADDED = "Booking added successfully";
    private static final String BOOKING_NOT_ADDED = "Error adding booking: ";
    private static final String BOOKING_REMOVED = "Booking removed successfully";
    private static final String BOOKING_NOT_REMOVED = "Error removing booking: ";
    private static final String NO_USER_LIST_AVAILABLE = "Error getting a list of booked users: ";
    private static final String NO_EVENT_LIST_AVAILABLE = "Error getting a list of booked events: ";

    //#endregion constants

    //#region booking methods

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

                    LoggerHelper.logInfoMessage(BookingDatabaseConnector.class, BOOKING_ADDED);

                    return updatedRows > 0;
                }

                return false;
            });

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(BookingDatabaseConnector.class, BOOKING_NOT_ADDED +
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

                    LoggerHelper.logInfoMessage(BookingDatabaseConnector.class, BOOKING_REMOVED);

                    return updatedRows > 0;
                }

                return false;
            });

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(BookingDatabaseConnector.class, BOOKING_NOT_REMOVED +
                    exception.getMessage());

            return false;
        }
    }

    /**
     * Return a list of user email addresses
     * */
    public static ArrayList<String> getBookedUsersInformationOnEvent(String eventID) {
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
            LoggerHelper.logErrorMessage(BookingDatabaseConnector.class, NO_USER_LIST_AVAILABLE + exception.getMessage());
        }

        return bookedUsers;
    }

    public static List<User> getBookedUsersOnEvent(String eventID) {
        List<User> bookedUsers = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record> records = create.select()
                    .from(USER)
                    .join(BOOKED).on(USER.USERID.eq(BOOKED.USERID))
                    .where(BOOKED.EVENTID.eq(eventID))
                    .fetch();

            bookedUsers.addAll(records.stream()
                    .map(record -> new User(
                            record.get(USER.USERID),
                            record.get(USER.FIRSTNAME),
                            record.get(USER.LASTNAME),
                            record.get(USER.BIRTHDATE),
                            record.get(USER.EMAIL),
                            record.get(USER.PASSWORD),
                            record.get(USER.PHONENUMBER),
                            record.get(USER.ISADMIN)
                    ))
                    .collect(Collectors.toList()));

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(BookingDatabaseConnector.class, NO_USER_LIST_AVAILABLE + exception.getMessage());
        }

        return bookedUsers;
    }

    /**
     * Return a list of event IDs
     * */
    public static ArrayList<Optional<? extends EventModel>> getEventsBookedByUser(String userID) {
        ArrayList<String> bookedEventIDs = new ArrayList<>();
        ArrayList<Optional<? extends EventModel>> bookedEvents = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record> records = create.select()
                    .from(EVENTS)
                    .join(BOOKED).on(EVENTS.EVENTID.eq(BOOKED.EVENTID))
                    .where(BOOKED.USERID.eq(userID))
                    .fetch();

            bookedEventIDs.addAll(records.stream()
                    .map(record -> record.get(EVENTS.EVENTID))
                    .collect(Collectors.toList()));

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(BookingDatabaseConnector.class, NO_EVENT_LIST_AVAILABLE + exception.getMessage());
        }

        for (String eventID : bookedEventIDs) {
            bookedEvents.add(EventDatabaseConnector.readEventByID(eventID));
        }

        return bookedEvents;
    }

    //#endregion booking methods

}
