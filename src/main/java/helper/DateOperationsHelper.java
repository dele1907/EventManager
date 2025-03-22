package helper;

import de.eventmanager.core.database.Communication.DatabaseConnector;
import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import org.jooq.impl.DSL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.jooq.impl.SQLDataType;

public class DateOperationsHelper {
    public static final String FORMAT_SPECIFIER_YEAR = "G";
    public static final String FORMAT_SPECIFIER_MONTH = "m";
    public static final String FORMAT_SPECIFIER_DAY = "d";
    public static final String FORMAT_SPECIFIER_HOUR = "H";
    public static final String FORMAT_SPECIFIER_MINUTE = "M";

    private static final String CONNECTION_FAIL = "The Database-Connection failed!";
    private static final String NO_BIRTHDAY_FOUND = "Wrong eMail Address or no Birthday found!";
    private static final String NO_EVENT_START_FOUND = "Wrong Event Name or no start day found";

    public static int getTheAgeFromDatabase(String eMailAddress) {
        int years = 0;

        try (var connection = DatabaseConnector.connect()){
            var result = DSL.using(connection)
                    .select(DSL.timestampDiff(DSL.field("birthDate", SQLDataType.TIMESTAMP), DSL.currentTimestamp()))
                    .from("user")
                    .where(DSL.field("eMail").eq(eMailAddress))
                    .fetch();

            if(result.isNotEmpty()) {
                for (var record : result) {
                    if (record.value1().toString().startsWith("+")) {
                        LoggerHelper.logErrorMessage(DateOperationsHelper.class,
                                "Invalid birthdate: it is in the future"
                        );

                        return 0;
                    }

                    years = record.value1().getDays() / 365;
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_BIRTHDAY_FOUND);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class,
                    CONNECTION_FAIL + exception.getMessage()
            );
        }

        return years;
    }

    public static Map<String, Integer> getDaysHoursMinutesToEvent(String eventID) {
        var timeToEventMap = new HashMap<String, Integer>();

        try (Connection connection = DatabaseConnector.connect()) {
            var result = DSL.using(connection)
                    .select(DSL.timestampDiff(DSL.field("datetime(eventStart)", SQLDataType.TIMESTAMP),
                            DSL.currentTimestamp())
                    )
                    .from("events")
                    .where(DSL.field("eventID").eq(eventID))
                    .fetch();

            if (result.isNotEmpty()) {
                for (var record : result) {
                    var differenceToCurrentTime = record.value1();

                    timeToEventMap.put("days", differenceToCurrentTime.getDays());
                    timeToEventMap.put("hours", differenceToCurrentTime.getHours());
                    timeToEventMap.put("minutes", differenceToCurrentTime.getMinutes());
                }
            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL +
                    exception.getMessage()
            );
        }

        return timeToEventMap;
    }

    public static ArrayList<String> getAllExpiredEvents() {
        var eventsToDelete = new ArrayList<String>();

        try (var connection = DatabaseConnector.connect()) {

            var result = DSL.using(connection)
                    .select(DSL.field("eventID"))
                    .from("events")
                    .where(DSL.field("datetime(eventEnd)").lt(DSL.field("datetime('now', '-14 days')")))
                    .fetch();

            if (result.isNotEmpty()) {
                for (var record : result) {
                    eventsToDelete.add(record.value1().toString());
                }
            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class,
                    CONNECTION_FAIL + exception.getMessage()
            );
        }

        return eventsToDelete;
    }

    public static int getEventEndValue(String eventId, String formatSpecifier) {
        int value = 0;

        try (var connection = DatabaseConnector.connect()) {

            var result = DSL.using(connection)
                    .select(DSL.field( String.format("strftime('%%%s', eventEnd)", formatSpecifier), Integer.class))
                    .from("events")
                    .where(DSL.field("eventId").eq(eventId))
                    .fetch();

            if(result.isNotEmpty()) {

                for (var record : result) {
                    value = record.value1();
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }
        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL +
                    exception.getMessage()
            );
        }

        return value;
    }

    public static int getEventStartValue(String eventId, String formatSpecifier) {
        int value = 0;

        try (var connection = DatabaseConnector.connect()) {

            var result =  DSL.using(connection)
                    .select(DSL.field(String.format("strftime('%%%s', eventStart)", formatSpecifier), Integer.class))
                    .from("events")
                    .where(DSL.field("eventId").eq(eventId))
                    .fetch();

            if(result.isNotEmpty()) {
                for (var record : result) {
                    value = record.value1();
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }
        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class,
                    CONNECTION_FAIL + exception.getMessage()
            );
        }

        return value;
    }
}
