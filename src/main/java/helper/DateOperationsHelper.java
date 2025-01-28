package helper;

import de.eventmanager.core.database.Communication.DatabaseConnector;
import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.impl.DSL;
import java.sql.Connection;
import org.jooq.Record;
import static org.jooq.generated.Tables.EVENTS;
import static org.jooq.impl.DSL.*;
import org.jooq.*;
import org.jooq.impl.SQLDataType;
import org.jooq.types.DayToSecond;

public class DateOperationsHelper {

    private static final String CONNECTION_FAIL = "The Databaseconnection fails!";
    private static final String NO_BIRTHDAY_FOUND = "Wrong eMail Address or no Birthday found!";
    private static final String NO_EVENTSART_FOUND = "Wrong Event Name or no start day found";

    public int validateTheAge(String eMailAddresse) {
        int days = 0;

        try (Connection connection = DatabaseConnector.connect()){

            DSLContext create = DSL.using(connection);

            Result<Record1<DayToSecond>> result = create.select(
                            DSL.timestampDiff(DSL.currentTimestamp(),
                                    DSL.field("birthDate", SQLDataType.TIMESTAMP)))
                                    .from("user")
                                    .where(DSL.field("eMail").eq(eMailAddresse))
                                    .fetch();

            if(!result.isEmpty()) {

                for (Record1<DayToSecond> record : result) {
                    days = record.value1().getDays();
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_BIRTHDAY_FOUND);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }
        return days;
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

            if(!result.isEmpty()) {

                for (Record1<DayToSecond> record : result) {
                    days = record.value1().getDays();
                    hours = record.value1().getHours();
                    minutes = record.value1().getMinutes();
                }

            } else {
                LoggerHelper.logErrorMessage(EventDatabaseConnector.class, NO_EVENTSART_FOUND);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }
        return days;
    }

    /**
     * TODO @TIMO, @Dennis: comment in if we want to use jooq queries
     * package helper;
     *
     * import de.eventmanager.core.database.Communication.DatabaseConnector;
     * import de.eventmanager.core.database.Communication.EventDatabaseConnector;
     * import org.jooq.DSLContext;
     * import org.jooq.Record1;
     * import org.jooq.Result;
     * import org.jooq.impl.DSL;
     * import org.jooq.impl.SQLDataType;
     * import org.jooq.types.DayToSecond;
     *
     * import java.sql.Connection;
     *
     * public class DateOperationsHelper {
     *
     *     private static final String CONNECTION_FAIL = "The Database connection fails!";
     *
     *     public void validateTheAge(String name) {
     *         try (Connection connection = DatabaseConnector.connect()) {
     *             DSLContext create = DSL.using(connection);
     *             Result<Record1<DayToSecond>> result = create.select(
     *                     DSL.timestampDiff(DSL.currentTimestamp(),
     *                     DSL.field("birthDate", SQLDataType.TIMESTAMP)))
     *                     .from("user")
     *                     .where(DSL.field("firstName").eq(name))
     *                     .fetch();
     *
     *             for (Record1<DayToSecond> record : result) {
     *                 int rechnung = record.value1().getDays() / 365;
     *                 System.out.println("Jahr: " + rechnung);
     *                 if (rechnung >= 18) {
     *                     System.out.println("Ueber 18: " + (rechnung > 18));
     *                 } else {
     *                     System.out.println("Er ist keine 18!");
     *                 }
     *             }
     *         } catch (Exception exception) {
     *             LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
     *         }
     *     }
     *
     *     public void timeToEvent(String eventName) {
     *         try (Connection connection = DatabaseConnector.connect()) {
     *             DSLContext create = DSL.using(connection);
     *             Result<Record1<DayToSecond>> result = create.select(DSL.timestampDiff(DSL.currentTimestamp(), DSL.field("eventStart", SQLDataType.TIMESTAMP)))
     *                     .from("events")
     *                     .where(DSL.field("eventName").eq(eventName))
     *                     .fetch();
     *
     *             for (Record1<DayToSecond> record : result) {
     *                 int months = record.value1().getDays() / 30;
     *                 System.out.println("months: " + months);
     *                 System.out.println("days: " + (record.value1().getDays() - (months * 30)));
     *                 System.out.println("hours: " + record.value1().getHours());
     *                 System.out.println("minutes: " + record.value1().getMinutes());
     *             }
     *         } catch (Exception exception) {
     *             LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
     *         }
     *     }
     *
     *     public static void main(String[] args) {
     *         DateOperationsHelper dateOperationsHelper = new DateOperationsHelper();
     *         dateOperationsHelper.validateTheAge("Timo");
     *         dateOperationsHelper.timeToEvent("Genetikk Tour");
     *     }
     * }
     * */
}
