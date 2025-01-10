package EventManagementCoreTests.DatabaseCommunicationTests;

import EventManagementCore.DatabaseCommunication.UserManager;
import EventManagementCore.UserManagement.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTestDrive {

    private UserManager testUserManager;
    private User testUser;

    @BeforeEach
    public void setUp() {
        // Vor jedem Test eine Instanz von UserManager erstellen
        testUserManager = new UserManager();
        testUser = new User("Max", "Mustermann", "1980-01-01",
                "max.mustermann@mail.com", "password123", 1234567890, true);
    }

    @Test
    public void testCreateDeleteUser() {
        boolean userCreated = testUserManager.createNewUser(testUser);
        assertTrue(userCreated, "User creation failed but should not.");

        boolean userDeleted = testUserManager.deleteUserByID(testUser.getUserID());
        assertTrue(userDeleted, "User deletion failed but should not.");
    }

    @Test
    public void testDeleteUserFailed() {
        boolean userDeleted = testUserManager.deleteUserByID("invalidID");
        assertFalse(userDeleted, "User deletion was successful but should not.");
    }

    @AfterEach
    public void cleanUp() {
        // ...
    }

}
