package EventManagementCoreTests.UserManagementTests;

import EventManagementCore.UserManagement.User;
import Helper.IDGenerationHelper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    //TODO @Timo (review): here could be whitespace
    @Test
    void createNewUserTest() {
        User user = new User("", "", "", "", "",
                01223, true);

        assertTrue(user != null);
    }
    //TODO @Timo (review): here could be whitespace
    User user = new User("", "", "", "", "", 0123, true);
    String validTestPassword = "Hello";
    String invalidTestPassword = "HellÖ";
    //TODO @Timo (review): here could be whitespace
    //TODO @Timo (review): what is for non valid passwords? Should be testes as well
    @Test
    void comparingPasswordTest() {
        assertTrue(user.comparingPassword(validTestPassword , "Hello"));
    }
    //TODO @Timo (review): here could be whitespace
    @Test
    void isValidPasswordTest() {
        assertTrue(user.isValidPassword(validTestPassword));
    }
    //TODO @Timo (review): here could be whitespace
    @Test
    void isValidRegistrationPasswordTest() {
        assertTrue(user.isValidRegistrationPassword(validTestPassword, "Hello"));
    }
}
