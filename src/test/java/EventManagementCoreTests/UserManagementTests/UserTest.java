package EventManagementCoreTests.UserManagementTests;

import EventManagementCore.UserManagement.User;
import Helper.IDGenerationHelper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void createNewUserTest() {
        User user = new User("", "", "", "", "",
                01223, true);

        assertTrue(user != null);
    }

    User user = new User("", "", "", "", "", 0123, true);
    String validTestPassword = "Hello";
    String inValidTestPassword = "HellÖ";

    @Test
    void isValidRegistrationPasswordTest() {
        assertTrue(user.isValidRegistrationPassword(validTestPassword, "Hello"));

        assertFalse(user.isValidRegistrationPassword(inValidTestPassword, "HellÖ"));
    }
}
