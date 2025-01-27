package helper;

import de.eventmanager.core.database.Communication.DatabaseConnector;
import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.impl.DSL;
import java.sql.Connection;
import org.jooq.Record;
import static org.jooq.generated.Tables.EVENTS;

public class DateOperationsHelper {

    private static final String CONNECTION_FAIL = "The Databaseconnection fails!";

    public void validateTheAgeProbe(String eventName) {

        try (Connection connection = DatabaseConnector.connect()){

            DSLContext create = DSL.using(connection);

                  Record record = create.select(EVENTS.EVENTSTART)
                    .from(EVENTS)
                    .where(EVENTS.EVENTNAME.eq(eventName))
                    .fetchOne();

                  record.getValue(EVENTS.EVENTSTART);

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }
    }
}
