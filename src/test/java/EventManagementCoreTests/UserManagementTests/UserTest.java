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
    String invalidTestPassword = "HellÖ";
    @Test
    void comparingPasswordTest() {
        assertTrue(user.comparingPassword(validTestPassword , "Hello"));
    }
    @Test
    void isValidPasswordTest() {
        assertTrue(user.isValidPassword(validTestPassword));
    }
    @Test
    void isValidRegistrationPasswordTest() {
        assertTrue(user.isValidRegistrationPassword(validTestPassword, "Hello"));
    }
}
