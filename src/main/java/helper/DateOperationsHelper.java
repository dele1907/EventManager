package helper;

import de.eventmanager.core.database.Communication.DatabaseConnector;
import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import java.sql.Connection;
import static org.jooq.generated.Tables.EVENTS;


public class DateOperationsHelper {

    private static final String CONNECTION_FAIL = "The Databaseconnection fails!";

    public void validateTheAge(String eventName) {

        try (Connection connection = DatabaseConnector.connect()){

            DSLContext create = DSL.using(connection);

            create.select()
                    .from(EVENTS)
                    .where(EVENTS.EVENTNAME.eq(eventName))
                    .fetchOne();

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(EventDatabaseConnector.class, CONNECTION_FAIL + exception.getMessage());
        }
    }
}
