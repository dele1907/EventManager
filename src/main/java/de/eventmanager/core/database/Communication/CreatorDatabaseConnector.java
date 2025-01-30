package de.eventmanager.core.database.Communication;

import java.sql.Connection;;
import java.util.Optional;

import de.eventmanager.core.users.User;
import helper.LoggerHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;

import static org.jooq.generated.tables.Created.CREATED;
import static org.jooq.generated.tables.User.USER;

public class CreatorDatabaseConnector {

    //#region constants

    private static final String EVENT_CREATOR_ASSIGNED = "User assigned as event creator successfully";
    private static final String EVENT_CREATOR_NOT_ASSIGNED = "Error assigning user as event creator: ";
    private static final String EVENT_CREATOR_REMOVED = "User removed as event creator successfully";
    private static final String EVENT_CREATOR_NOT_REMOVED = "Error removing user as event creator: ";
    private static final String CHECK_IF_CREATOR_FAILED = "Checking if user is event creator failed: ";
    private static final String EVENT_CREATOR_NOT_FOUND = "Error finding event creator: ";
    private static final String ID_NOT_FOUND = "Error finding UserID or EventID";

    //#endregion constants

    //#region creator methods

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

    //#endregion creator methods

}
