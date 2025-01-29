package helper;

import de.eventmanager.core.database.Communication.DatabaseConnector;
import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import java.sql.Connection;

import org.jooq.*;
import org.jooq.impl.SQLDataType;
import org.jooq.types.DayToSecond;

public class DateOperationsHelper {

    private static final String CONNECTION_FAIL = "The Database-Connection failed!";
    private static final String NO_BIRTHDAY_FOUND = "Wrong eMail Address or no Birthday found!";
    private static final String NO_EVENT_START_FOUND = "Wrong Event Name or no start day found";

    //Todo Review: @Timo add comments to methods

    public int validateTheAge(String eMailAddresse) {
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

    public int timeToEvent(String eventName) {
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

    public void getDayTime(String eventName) {

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

    public void whichWeekIsTheEvent(String eventName) {

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
    }
}
