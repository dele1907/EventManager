package helper;

import de.eventmanager.core.users.User;
import java.util.Optional;
import org.jooq.Record;

import static org.jooq.generated.tables.User.USER;

public class UserDatabaseHelper {

    public static Optional<User> createUserFromRecord(Record record) {
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
        return Optional.empty();
    }
}