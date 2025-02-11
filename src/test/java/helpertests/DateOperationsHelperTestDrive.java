package helpertests;

import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.users.User;
import helper.DateOperationsHelper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DateOperationsHelperTestDrive {
    //TODO: Rework test class for independence of the Database
    User testUser;

    @Test
    void testCorrectEmail() {

        DateOperationsHelper dateOperationsHelper = new DateOperationsHelper();

        int testcase = dateOperationsHelper.getTheAgeFromDatabase("hallo");

        assertTrue(testcase == 0);
    }

    @Test
    void testAgeLogic() {

        testUser = new User("createTestUserDatabaseConnector", "Max", "Mustermann", "1999-01-10",
                "max.create@testmail.com", "Password123", "1234567890", false);

        UserDatabaseConnector.createNewUser(testUser);

        DateOperationsHelper dateOperationsHelper = new DateOperationsHelper();

        int correcttestcase = dateOperationsHelper.getTheAgeFromDatabase("max.create@testmail.com");

        assertFalse(correcttestcase > 27);
        assertTrue(correcttestcase > 18);

        UserDatabaseConnector.deleteUserByID("createTestUserDatabaseConnector");
    }
}
