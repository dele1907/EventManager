package helper;

import de.eventmanager.core.database.Communication.DatabaseConnector;
import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jooq.*;
import org.jooq.impl.SQLDataType;
import org.jooq.types.DayToSecond;

public class DateOperationsHelper {
    public static final String FORMAT_SPECIFIER_YEAR = "G";
    public static final String FORMAT_SPECIFIER_MONTH = "m";
    public static final String FORMAT_SPECIFIER_DAY = "d";
    public static final String FORMAT_SPECIFIER_HOUR = "H";
    public static final String FORMAT_SPECIFIER_MINUTE = "M";

    private static final String CONNECTION_FAIL = "The Database-Connection failed!";
    private static final String NO_BIRTHDAY_FOUND = "Wrong eMail Address or no Birthday found!";
    private static final String NO_EVENT_START_FOUND = "Wrong Event Name or no start day found";

    //TODO @Timo - Correct the error Message to english
    public static int getTheAgeFromDatabase(String eMailAddress) {
        int years = 0;

        try (Connection connection = DatabaseConnector.connect()){

            DSLContext create = DSL.using(connection);

            Result<Record1<DayToSecond>> result = create.select(
                            DSL.timestampDiff(DSL.field("birthDate", SQLDataType.TIMESTAMP),
                                    DSL.currentTimestamp()))
                                    .from("user")
                                    .where(DSL.field("eMail").eq(eMailAddress))
                                    .fetch();

            if(result.isNotEmpty()) {

                for (Record1<DayToSecond> record : result) {

                    String test = record.value1().toString();

                    if (test.startsWith("+")) {
                        LoggerHelper.logErrorMessage(DateOperationsHelper.class, "Das eingegebene Alter liegt in der Zukunft, bitte gib ein richtigs Geburtsdatum an!");
                        return 0;
                    }

                    years = record.value1().getDays() / 365;

                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_BIRTHDAY_FOUND);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }

        return years;
    }

    //TODO @Timo - correct the method until saturday afternoon
    //TODO @Timo - test the function of the method
    public static Map<String, Integer> timeToEvent(String eventID) {
        Map<String, Integer> timeMap = new HashMap<>();

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record1<DayToSecond>> result = create.select(DSL.timestampDiff(DSL.field("eventStart", SQLDataType.TIMESTAMP), DSL.currentTimestamp()))
                    .from("events")
                    .where(DSL.field("eventID").eq(eventID))
                    .fetch();

            if (result.isNotEmpty()) {
                for (Record1<DayToSecond> record : result) {
                    DayToSecond diff = record.value1();

                    int days = diff.getDays();
                    int hours = diff.getHours();
                    int minutes = diff.getMinutes();

                    timeMap.put("days", days);
                    timeMap.put("hours", hours);
                    timeMap.put("minutes", minutes);
                }
            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }

        return timeMap;
    }

    //TODO @Timo - correct the method until saturday afternoon
    public static ArrayList<String> checkIfEventIsOver() {
        ArrayList<String> eventsToDelete = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Field<Timestamp> fourteenDaysOrMoreAgo = DSL.field("datetime('now', '-14 days')", SQLDataType.TIMESTAMP);

            Result<Record1<Object>> result = create.select(DSL.field("eventID"))
                    .from("events")
                    .where(DSL.field("eventEnd", SQLDataType.TIMESTAMP).lt(fourteenDaysOrMoreAgo))
                    .fetch();

            if(result.isNotEmpty()) {

                for (Record1<Object> record : result) {
                    String eventID = record.value1().toString();
                    eventsToDelete.add(eventID);
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }

        return eventsToDelete;
    }

    //TODO @Timo - correct the method to use eventId
    public static int getEventStartValue(String eventName, String formatSpecifier) {
        int value = 0;

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);
            String expression = String.format("strftime('%%%s', eventStart)", formatSpecifier);

            Result<Record1<Integer>> result = create.select(DSL.field(expression, Integer.class))
                    .from("events")
                    .where(DSL.field("eventName").eq(eventName))
                    .fetch();

            if(result.isNotEmpty()) {

                for (Record1<Integer> record : result) {
                    value = record.value1();
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }
        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }

        return value;
    }

    //TODO @Timo - correct the method to use eventId
    public static int getEventEndValue(String eventName, String formatSpecifier) {
        int value = 0;

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);
            String expression = String.format("strftime('%%%s', eventEnd)", formatSpecifier);

            Result<Record1<Integer>> result = create.select(DSL.field(expression, Integer.class))
                    .from("events")
                    .where(DSL.field("eventName").eq(eventName))
                    .fetch();

            if(result.isNotEmpty()) {

                for (Record1<Integer> record : result) {
                    value = record.value1();
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }
        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }

        return value;
    }
}
