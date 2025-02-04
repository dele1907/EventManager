package helper;

import de.eventmanager.core.database.Communication.DatabaseConnector;
import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import java.sql.Connection;

import org.jooq.*;
import org.jooq.impl.SQLDataType;
import org.jooq.meta.derby.sys.Sys;
import org.jooq.types.DayToSecond;
import org.jooq.types.YearToMonth;

public class DateOperationsHelper {

    private static final String CONNECTION_FAIL = "The Database-Connection failed!";
    private static final String NO_BIRTHDAY_FOUND = "Wrong eMail Address or no Birthday found!";
    private static final String NO_EVENT_START_FOUND = "Wrong Event Name or no start day found";


    public static int getTheAgeFromDatabase(String eMailAddresse) {
        int years = 0;

        try (Connection connection = DatabaseConnector.connect()){

            DSLContext create = DSL.using(connection);

            Result<Record1<DayToSecond>> result = create.select(
                            DSL.timestampDiff(DSL.currentTimestamp(),
                                    DSL.field("birthDate", SQLDataType.TIMESTAMP)))
                                    .from("user")
                                    .where(DSL.field("eMail").eq(eMailAddresse))
                                    .fetch();

            if(result.isNotEmpty()) {

                for (Record1<DayToSecond> record : result) {

                    String test = record.value1().toString();

                    if (test.startsWith("-")) {
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

    public static int timeToEvent(String eventName) {
        int days = 0;
        int hours = 0;
        int minutes = 0;

        try (Connection connection = DatabaseConnector.connect()){

            DSLContext create = DSL.using(connection);

            Result<Record1<DayToSecond>> result = create.select(DSL.timestampDiff(DSL.currentTimestamp(), DSL.field("eventStart", SQLDataType.TIMESTAMP)))
                    .from("events")
                    .where(DSL.field("eventName").eq(eventName))
                    .fetch();

            if(result.isNotEmpty()) {

                for (Record1<DayToSecond> record : result) {
                    days = record.value1().getDays();
                    hours = record.value1().getHours();
                    minutes = record.value1().getMinutes();
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }

        return days;
    }

    public static void getDayTime(String eventName) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record1<String>> result = create.select(DSL.field("(strftime('%P', eventStart))", String.class))
                    .from("events")
                    .where(DSL.field("eventName").eq(eventName))
                    .fetch();

            if(result.isNotEmpty()) {

                for (Record1<String> record : result) {
                    System.out.println(record.value1());
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }
        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }
    }

    public static int checkIsAEventOver() {
        int days = 0;

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record1<DayToSecond>> result = create.select(DSL.timestampDiff(DSL.field("eventEnd", SQLDataType.TIMESTAMP), DSL.currentTimestamp()))
                    .from("events")
                    .fetch();

            if(result.isNotEmpty()) {

                for (Record1<DayToSecond> record : result) {
                    days = record.value1().getDays();
                    System.out.println(record.value1());
                    System.out.println(days);
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }

        return days;
    }

/*
    public static void main(String[] args) {
        DateOperationsHelper dateOperationsHelper = new DateOperationsHelper();
        //dateOperationsHelper.checkIsAEventOver();
        //System.out.println(dateOperationsHelper.getTheAgeFromDatabase("tisc00006@htwsaar.de"));
        System.out.println(dateOperationsHelper.getEventStartHour("Finch Tour 2025"));
    }
*/
    //Todo @Timo all methods return int = 0

    public static int getEventStartYear(String eventName) {
        int year = 0;

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record1<Integer>> result = create.select(DSL.field("(strftime('%G', eventStart))", Integer.class))
                    .from("events")
                    .where(DSL.field("eventName").eq(eventName))
                    .fetch();

            if(result.isNotEmpty()) {

                for (Record1<Integer> record : result) {
                    year = record.value1();
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }
        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }

        return year;
    }

    public static int getEventStartMonth(String eventName) {
        int month = 0;

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record1<Integer>> result = create.select(DSL.field("(strftime('%m', eventStart))", Integer.class))
                    .from("events")
                    .where(DSL.field("eventName").eq(eventName))
                    .fetch();

            if(result.isNotEmpty()) {

                for (Record1<Integer> record : result) {
                    month = record.value1();
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }
        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }

        return month;
    }

    public static int getEventStartDay(String eventName) {
        int
                day = 0;
        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record1<Integer>> result = create.select(DSL.field("(strftime('%d', eventStart))", Integer.class))
                    .from("events")
                    .where(DSL.field("eventName").eq(eventName))
                    .fetch();

            if(result.isNotEmpty()) {

                for (Record1<Integer> record : result) {
                    day = record.value1();
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }
        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }

        return day;
    }

    public static int getEventStartHour(String eventName) {
        int hour = 0;

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record1<Integer>> result = create.select(DSL.field("(strftime('%H', eventStart))", Integer.class))
                    .from("events")
                    .where(DSL.field("eventName").eq(eventName))
                    .fetch();

            if(result.isNotEmpty()) {

                for (Record1<Integer> record : result) {
                    hour = record.value1();
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }
        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }

        return hour;
    }

    public static int getEventStartMinute(String eventName) {
        int minute = 0;

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record1<Integer>> result = create.select(DSL.field("(strftime('%M', eventStart))", Integer.class))
                    .from("events")
                    .where(DSL.field("eventName").eq(eventName))
                    .fetch();

            if(result.isNotEmpty()) {

                for (Record1<Integer> record : result) {
                    minute = record.value1();
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }
        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }

        return minute;
    }


}

/*
Is in the comment because we will decide later whether we use the Method

* public void whichWeekIsTheEvent(String eventName) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record1<String>> result = create.select(DSL.field("(strftime('%V', eventStart))", String.class))
                    .from("events")
                    .where(DSL.field("eventName").eq(eventName))
                    .fetch();

            if(result.isNotEmpty()) {

                for (Record1<String> record : result) {
                    System.out.println(record.value1());
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENT_START_FOUND);
            }
        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }
    }*/
