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
}
