package EventManagementCoreTests.UserManagementTests;

import EventManagementCore.UserManagement.UserManager;
import EventManagementCore.UserManagement.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTestDrive {

    private UserManager testUserManager;
    private User testUser;
    private User testUserUpdated;

    // Vor jedem Test eine Instanz von UserManager und zwei Test-User erstellen:
    @BeforeEach
    public void setUp() {
        testUserManager = new UserManager();
        testUser = new User("testUserID", "Max", "Mustermann", "1980-01-10",
                "max.mustermann@mail.com", "password123", 1234567890, false);
        testUserUpdated = new User("testUserID", "Maximilian", "Mustermann-Meyer", "1980-10-01",
                "max.m@mail.com", "password987", 1357902468, false);
    }

    // Testen von Erstellen, Updaten und Löschen eines Users in der Datenbank:
    @Test
    public void testCreateUpdateDeleteUser() {
        boolean userCreated = testUserManager.createNewUser(testUser);
        assertTrue(userCreated, "User creation failed but should not.");

        boolean userUpdated = testUserManager.updateUser(testUserUpdated);
        assertTrue(userUpdated, "User update failed but should not.");

        boolean userDeleted = testUserManager.deleteUserByID(testUser.getUserID());
        assertTrue(userDeleted, "User deletion failed but should not.");
    }

    // Testen, ob ein User nicht mehrmals erstellt werden kann:
    @Test
    public void testCreateUserFailed() {
        testUserManager.createNewUser(testUser);
        boolean userCreated = testUserManager.createNewUser(testUser);
        assertFalse(userCreated, "User creation was successful but should not.");
    }

    // Testen, ob ein Update nur möglich ist, wenn der entsprechende Datensatz vorliegt:
    @Test
    public void testUpdateUserFailed() {
        boolean userUpdated = testUserManager.updateUser(testUserUpdated);
        assertFalse(userUpdated, "User update was successful but should not.");
    }

    // Testen, ob Löschen nur möglich ist, wenn der entsprechende Datensatz vorliegt:
    @Test
    public void testDeleteUserFailed() {
        boolean userDeleted = testUserManager.deleteUserByID("invalidID");
        assertFalse(userDeleted, "User deletion was successful but should not.");
    }

    // Testen, ob ein User anhand der ID korrekt ausgelesen werden kann:
    @Test
    public void testReadUserByID() {
        testUserManager.createNewUser(testUser);
        User userFromDatabase = testUserManager.readUserByID("testUserID");
        assertEquals("testUserID", userFromDatabase.getUserID());
        assertEquals("Max", userFromDatabase.getFirstName());
        assertEquals("Mustermann", userFromDatabase.getLastName());
        assertEquals("1980-01-10", userFromDatabase.getDateOfBirth());
        assertEquals("max.mustermann@mail.com", userFromDatabase.getEMailAddress());
        assertEquals("password123", userFromDatabase.getPassword());
        assertEquals(1234567890, userFromDatabase.getPhoneNumber());
        assertEquals(false, userFromDatabase.isAdmin());
    }

    // Testen, ob ein User anhand der E-Mail korrekt ausgelesen werden kann:
    @Test
    public void testReadUserByEMail() {
        testUserManager.createNewUser(testUser);
        User userFromDatabase = testUserManager.readUserByEMail("max.mustermann@mail.com");
        assertEquals("testUserID", userFromDatabase.getUserID());
        assertEquals("Max", userFromDatabase.getFirstName());
        assertEquals("Mustermann", userFromDatabase.getLastName());
        assertEquals("1980-01-10", userFromDatabase.getDateOfBirth());
        assertEquals("max.mustermann@mail.com", userFromDatabase.getEMailAddress());
        assertEquals("password123", userFromDatabase.getPassword());
        assertEquals(1234567890, userFromDatabase.getPhoneNumber());
        assertEquals(false, userFromDatabase.isAdmin());
    }

    // Nach jedem Test die Datenbank bereinigen:
    @AfterEach
    public void cleanUp() {
        testUserManager.deleteUserByID("testUserID");
    }

}