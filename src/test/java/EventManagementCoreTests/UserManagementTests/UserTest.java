package EventManagementCoreTests.UserManagementTests;

import EventManagementCore.UserManagement.User;
import Helper.IDGenerationHelper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void createNewUserTest() {
        User user = new User(IDGenerationHelper.generateRandomString(5), "", "", "", "", "",
                01223, true);

        assertTrue(user != null);
    }

}
