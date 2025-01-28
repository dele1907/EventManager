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

public class DateOperationsHelper {

    private static final String CONNECTION_FAIL = "The Databaseconnection fails!";

    public void validateTheAge(String name) {

        try (Connection connection = DatabaseConnector.connect()){

            DSLContext create = DSL.using(connection);

            Field<Integer> daytimeField = field(
                    "TIMEDIFF(datetime('now'), (SELECT birthDate FROM user WHERE firstName = {0}))",
                    Integer.class,
                    inline(name)
            ).as("age");

            Result<Record1<Integer>> result = create.select(daytimeField).fetch();

            for (Record1<Integer> record : result) {
                int alter = record.get(daytimeField);
                if (alter >= 18) {
                    System.out.println("Alter: " + alter);
                } else {
                    System.out.println("This person is not 18!");
                }
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }
    }

    public void timeToEvent(String eventName) {

        try (Connection connection = DatabaseConnector.connect()){

            DSLContext create = DSL.using(connection);

            Field<String> daytimeField = field(
                    "TIMEDIFF(datetime('now'), (SELECT eventStart FROM events WHERE eventName = {0}))",
                    String.class,
                    inline(eventName)
            ).as("timetoEvent");

            Result<Record1<String>> result = create.select(daytimeField).fetch();

            for (Record1<String> record : result) {
                System.out.println("days: " + record.get("timetoEvent"));
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }
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
