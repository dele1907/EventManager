package EventManagementCoreTests.UserManagementTests;

import EventManagementCore.UserManagement.User;
import Helper.IDGenerationHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    User testUser = new User("firstName", "LastName", "dateOfBirth",
            "firstName.lastName@testmail.com", "eventManager123", 5566778);



    @Test
    @DisplayName("CreateUserTest")
    void createNewUserTest() {


        assertTrue(user != null);
    }

    User user = new User("", "", "", "", "", 0123);
    String validTestPassword = "Hello";
    String inValidTestPassword = "HellÖ";

    @Test
    void isValidRegistrationPasswordTest() {
        assertTrue(user.isValidRegistrationPassword(validTestPassword, "Hello"));

        assertFalse(user.isValidRegistrationPassword(inValidTestPassword, "HellÖ"));
    }

    @Test
    @DisplayName("isUserAdminTest")
    void isUserAdminAndIsNoAdminTest() {
        testUser.addAdminStatusToUser();
        assertTrue(testUser.isAdmin());
        testUser.removeAdminStatusFromUser();
        assertFalse(testUser.isAdmin());
    }
}
